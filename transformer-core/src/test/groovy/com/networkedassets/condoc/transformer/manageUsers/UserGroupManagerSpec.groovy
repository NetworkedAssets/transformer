package com.networkedassets.condoc.transformer.manageUsers

import com.networkedassets.condoc.transformer.common.PersistenceTestingAbility
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup
import com.networkedassets.condoc.transformer.manageUsers.infrastructure.db.jpa.JpaUserGroupManager
import com.networkedassets.condoc.transformer.manageUsers.infrastructure.db.jpa.JpaUserManager
import spock.lang.Specification

class UserGroupManagerSpec extends Specification implements PersistenceTestingAbility {

    static UserGroupManager userGroupManager
    static UserManager userManager
    UserGroup group1
    UserGroup group2
    UserGroup allGroup
    User user1
    User user2

    @SuppressWarnings("GroovyAccessibility")
    def setupSpec() {
        userGroupManager = new JpaUserGroupManager()
        userGroupManager.em = entityManager
        userManager = new JpaUserManager()
        userManager.em = entityManager
        userManager.userGroupManager = userGroupManager
    }

    def setup() {
        group1 = new UserGroup("AAA")
        group2 = new UserGroup("BBB")
        user1 = new User("XXX").setPassword("X")
        user2 = new User("ZZZ").setPassword("Z")
        allGroup = new UserGroup("all").setAutomaticallyCreated(true)
    }

    def "Persists new groups"() {
        when:
        userGroupManager.persist(group1)
        userGroupManager.persist(group2)
        flushAndClearCache()

        then:
        List<UserGroup> list = userGroupManager.getAllGroups()
        list.size() == 2
        list.contains(group1)
        list.contains(group2)
    }

    def "Removes groups"() {
        given:
        userGroupManager.persist(group1)
        userGroupManager.persist(group2)
        flushAndClearCache()

        when:
        userGroupManager.removeById(group1.id)
        then:
        List<UserGroup> list = userGroupManager.getAllGroups()
        list.size() == 1
        list.contains(group2)

        when:
        flushAndClearCache()
        userGroupManager.removeById(group2.id)
        then:
        List<UserGroup> list2 = userGroupManager.getAllGroups()
        list2.size() == 0
    }

    def "Does not remove autocreated groups"() {
        given:
        userGroupManager.persist(allGroup)
        flushAndClearCache()

        when:
        userGroupManager.removeByIdIfNotAutocreated(allGroup.id)

        then:
        thrown Exception
        List<UserGroup> list = userGroupManager.getAllGroups()
        list.size() == 1
        list.contains(allGroup)
    }

    def "Gets group by name"() {
        given:
        userGroupManager.persist(group1)

        when:
        Optional<UserGroup> fetchedGroup = userGroupManager.getByName(group1.name)
        flushAndClearCache()

        then:
        fetchedGroup.isPresent()
        group1.equals(fetchedGroup.get())
    }

    def "Adds users to specified group"() {
        given:
        userGroupManager.persist(allGroup)
        userGroupManager.persist(group1)
        userManager.persist(user1)
        userManager.persist(user2)

        when:
        userGroupManager.addUserToGroup(user1, group1.id)
        userGroupManager.addUserToGroup(user2, group1.id)
        flushAndClearCache()

        then:
        UserGroup fetchedGroup = userGroupManager.getById(group1.id).get()
        Set<User> users = fetchedGroup.users
        users.size() == 2
        users.contains(user1)
        users.contains(user2)
    }

    def "Removes users from specified group"() {
        given:
        userGroupManager.persist(allGroup)
        userGroupManager.persist(group1)
        userManager.persist(user1)
        userGroupManager.addUserToGroup(user1, group1.id)
        flushAndClearCache()

        when:
        userGroupManager.removeUserFromGroup(user1, group1.getId())

        then:
        userGroupManager.getById(group1.id).get().users.size() == 0
    }
}
