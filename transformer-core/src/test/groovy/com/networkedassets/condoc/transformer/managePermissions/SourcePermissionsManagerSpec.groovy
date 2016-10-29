package com.networkedassets.condoc.transformer.managePermissions

import com.google.common.collect.ImmutableSet
import com.networkedassets.condoc.transformer.common.PersistenceTestingAbility
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import com.networkedassets.condoc.transformer.common.SourceUnit
import com.networkedassets.condoc.transformer.managePermissions.core.boundary.SourcePermissionsManager
import com.networkedassets.condoc.transformer.managePermissions.core.entity.Node
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureNode
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode
import com.networkedassets.condoc.transformer.managePermissions.infrastructure.db.jpa.JpaSourcePermissionsManager
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup
import com.networkedassets.condoc.transformer.manageUsers.infrastructure.db.jpa.JpaUserGroupManager
import com.networkedassets.condoc.transformer.manageUsers.infrastructure.db.jpa.JpaUserManager
import spock.lang.Specification

class SourcePermissionsManagerSpec extends Specification implements PersistenceTestingAbility{

    static UserGroupManager userGroupManager
    static UserManager userManager
    static SourcePermissionsManager sourcePermissionsManager

    def setupSpec() {
        sourcePermissionsManager = new JpaSourcePermissionsManager()
        userGroupManager = new JpaUserGroupManager()
        userManager = new JpaUserManager()

        sourcePermissionsManager.em = entityManager
        userGroupManager.em = entityManager
        userManager.em = entityManager

        sourcePermissionsManager.userGroupManager = userGroupManager
        sourcePermissionsManager.userManager = userManager
        userManager.userGroupManager = userGroupManager
        userGroupManager.userManager = userManager
    }

    def "refuses acces by default"() {
        given:
        UserGroup userGroup = new UserGroup("AAA");
        SourceNodeIdentifier sourceNodeIdentifier = new SourceNodeIdentifier(2, "CCC|DDD")
        userGroupManager.persist(userGroup);

        expect:
        !sourcePermissionsManager.groupHasAccess(userGroup, sourceNodeIdentifier)
        !sourcePermissionsManager.groupHasAccessById(userGroup.getId(), sourceNodeIdentifier)
    }


    def "grants permissions to SourceNodeIdentifier for UserGroup while being idempotent"() {
        given:
        UserGroup userGroup = new UserGroup("AAA");

        userGroupManager.persist(userGroup)
        SourceNodeIdentifier sourceNodeIdentifier = new SourceNodeIdentifier(2, "CCC|DDD")

        when:
        sourcePermissionsManager.grantAccess(userGroup, sourceNodeIdentifier)
        sourcePermissionsManager.grantAccess(userGroup, sourceNodeIdentifier)
        then:
        sourcePermissionsManager.groupHasAccess(userGroup, sourceNodeIdentifier)

        when:
        sourcePermissionsManager.revokeAccess(userGroup, sourceNodeIdentifier)
        then:
        !sourcePermissionsManager.groupHasAccess(userGroup, sourceNodeIdentifier)
    }

    def "grants permissions to one group while still refusing to the others"() {
        given:
        UserGroup group1 = new UserGroup("AAA")
        UserGroup group2 = new UserGroup("BBB")
        userGroupManager.persist(group1)
        userGroupManager.persist(group2)
        SourceNodeIdentifier sourceNodeIdentifier = new SourceNodeIdentifier(1, "MMM|VVV")

        when:
        sourcePermissionsManager.grantAccess(group1, sourceNodeIdentifier)
        flushAndClearCache()
        then:
        sourcePermissionsManager.groupHasAccess(group1, sourceNodeIdentifier)
        sourcePermissionsManager.groupHasAccessById(group1.getId(), sourceNodeIdentifier)
        !sourcePermissionsManager.groupHasAccess(group2, sourceNodeIdentifier)
        !sourcePermissionsManager.groupHasAccessById(group2.getId(), sourceNodeIdentifier)
    }

    def "checks access for specific users in groups"() {
        given:
        SourceNodeIdentifier sourceNodeIdentifier = new SourceNodeIdentifier(1, "NNNN|NNNNN")

        User user1 = new User("AAA").setPassword("A")
        User user2 = new User("BBB").setPassword("B")
        User user3 = new User("CCC").setPassword("C")

        UserGroup allGroup = new UserGroup("all")
        UserGroup group = new UserGroup("XXX")
        userGroupManager.persist(allGroup)
        userGroupManager.persist(group)
        userManager.persist(user1)
        userManager.persist(user2)
        userManager.persist(user3)
        userGroupManager.addUserToGroup(user1, group.getId())
        userGroupManager.addUserToGroup(user2, group.getId())
        flushAndClearCache()

        when:
        sourcePermissionsManager.grantAccess(group, sourceNodeIdentifier)
        then:
        sourcePermissionsManager.userHasAccess(userManager.getByUsername(user1.getUsername()).get(), sourceNodeIdentifier)
        sourcePermissionsManager.userHasAccess(userManager.getByUsername(user2.getUsername()).get(), sourceNodeIdentifier)
        !sourcePermissionsManager.userHasAccess(userManager.getByUsername(user3.getUsername()).get(), sourceNodeIdentifier)

        sourcePermissionsManager.userHasAccessByUsername(user1.getUsername(), sourceNodeIdentifier)
        sourcePermissionsManager.userHasAccessByUsername(user2.getUsername(), sourceNodeIdentifier)
        !sourcePermissionsManager.userHasAccessByUsername(user3.getUsername(), sourceNodeIdentifier)
    }

    def "filters structure tree by UserGroup permission"() {
        given:
        /* This structure
                    g
                +----+
                |ROOT|
                ++---+
           +-----+--------------------+
           |     |                    |
         +-++  +-++                 +-++
        2|P1|  |P2|                2|P3|
         +--+  +-++                 +-++
                 |          +---------+---------------+
                 |          |         |               |
              +--++       +-+-+     +-+-+           +-+-+
             1|R21|      2|R31|     |R32|          1|R33|
              +-+-+       +---+     +-+-+           +--++
                |                     |                |
                |             +-------------+          |
                |             |       |     |          |
              +-+--+       +--+-+ +---++ +--+-+     +--+-+
              |B211|       |B321| |B322| |B323|     |B331|
              +----+       +----+ +----+ +----+     +----+
                                    1      1           1
        */
        Integer sourceIdentifier = 0
        SourceStructureRootNode root = new SourceStructureRootNode(sourceIdentifier)
        Node p1 = new SourceStructureNode(new SourceNodeIdentifier(sourceIdentifier, "P1")).setParent(root)
        Node p2 = new SourceStructureNode(new SourceNodeIdentifier(sourceIdentifier, "P2")).setParent(root)
        Node p3 = new SourceStructureNode(new SourceNodeIdentifier(sourceIdentifier, "P3")).setParent(root)
        Node r21 = new SourceStructureNode(new SourceNodeIdentifier(sourceIdentifier, "P2|R21")).setParent(p2)
        Node r31 = new SourceStructureNode(new SourceNodeIdentifier(sourceIdentifier, "P3|R31")).setParent(p3)
        Node r32 = new SourceStructureNode(new SourceNodeIdentifier(sourceIdentifier, "P3|R32")).setParent(p3)
        Node r33 = new SourceStructureNode(new SourceNodeIdentifier(sourceIdentifier, "P3|R33")).setParent(p3)
        Node b211 = new SourceUnit(new SourceNodeIdentifier(sourceIdentifier, "P2|R21|B211")).setParent(r21)
        Node b321 = new SourceUnit(new SourceNodeIdentifier(sourceIdentifier, "P3|R32|B321")).setParent(r32)
        Node b322 = new SourceUnit(new SourceNodeIdentifier(sourceIdentifier, "P3|R32|B322")).setParent(r32)
        Node b323 = new SourceUnit(new SourceNodeIdentifier(sourceIdentifier, "P3|R32|B323")).setParent(r32)
        Node b331 = new SourceUnit(new SourceNodeIdentifier(sourceIdentifier, "P3|R33|B331")).setParent(r33)

        UserGroup group1 = new UserGroup("group1")
        UserGroup group2 = new UserGroup("group2")
        UserGroup godGroup = new UserGroup("godGroup")
        userGroupManager.persist(group1);
        userGroupManager.persist(group2);
        userGroupManager.persist(godGroup);

        sourcePermissionsManager.grantAccess(godGroup, root.getSourceNodeIdentifier())
        sourcePermissionsManager.grantAccess(group1, r21.getSourceNodeIdentifier())
        sourcePermissionsManager.grantAccess(group1, r33.getSourceNodeIdentifier())
        sourcePermissionsManager.grantAccess(group1, b322.getSourceNodeIdentifier())
        sourcePermissionsManager.grantAccess(group1, b323.getSourceNodeIdentifier())
        sourcePermissionsManager.grantAccess(group1, b331.getSourceNodeIdentifier())
        sourcePermissionsManager.grantAccess(group2, p1.getSourceNodeIdentifier())
        sourcePermissionsManager.grantAccess(group2, p3.getSourceNodeIdentifier())
        sourcePermissionsManager.grantAccess(group2, r31.getSourceNodeIdentifier())

        when:
        SourceStructureRootNode godGroupRootNode = sourcePermissionsManager.filterStructureByUserGroupsPermission(root, ImmutableSet.of(godGroup))

        then:
        godGroupRootNode == root
        godGroupRootNode.is(root)
        godGroupRootNode.children.size() == 3

        when:
        SourceStructureRootNode group1RootNode = sourcePermissionsManager.filterStructureByUserGroupsPermission(root, ImmutableSet.of(group1))

        then:
        group1RootNode.getChildren().size() == 2

        Node group1P2 = group1RootNode.getChildren().get(0)
        group1P2.nodeName == "P2"
        group1P2.children.size() == 1

        Node group1R21 = group1P2.getChildren().get(0)
        group1R21.nodeName == "R21"
        group1R21.children.size() == 1
        group1R21.children.get(0).nodeName == "B211"

        Node group1P3 = group1RootNode.getChildren().get(1)
        group1P3.nodeName == "P3"
        group1P3.children.size() == 2

        Node group1R32 = group1P3.getChildren().get(0)
        Node group1R33 = group1P3.getChildren().get(1)
        group1R32.nodeName == "R32"
        group1R32.getChildren().size() == 2
        group1R32.getChildren().each { it.isLeaf() }

        group1R33.nodeName == "R33"
        group1R33.getChildren().size() == 1
        group1R33.children.each { it.isLeaf() }
        group1R33.children.get(0).nodeName == "B331"
        group1R32.children.get(0).nodeName == "B322"
        group1R32.children.get(1).nodeName == "B323"

        when:
        SourceStructureRootNode group2RootNode = sourcePermissionsManager.filterStructureByUserGroupsPermission(root, ImmutableSet.of(group2))

        then:
        group2RootNode.children.size() == 2

        Node group2P2 = group2RootNode.children.get(0)
        group2P2.nodeName == "P1"
        group2P2.children.size() == 0

        Node group2P3 = group2RootNode.children.get(1)
        group2P3.children.size() == 3

        Node group2R31 = group2P3.children.get(0)
        group2R31.nodeName == "R31"
        group2R31.children.size() == 0

        Node group2R32 = group2P3.children.get(1)
        group2R32.nodeName == "R32"
        group2R32.children.size() == 3
        group2R32.children.each { it.isLeaf() }
        group2R32.children.get(0).nodeName == "B321"
        group2R32.children.get(1).nodeName == "B322"
        group2R32.children.get(2).nodeName == "B323"

        Node group2R33 = group2P3.children.get(2)
        group2R33.nodeName == "R33"
        group2R33.children.size() == 1
        group2R33.children.each { it.isLeaf() }
        group2R33.children.get(0).nodeName == "B331"
    }

}
