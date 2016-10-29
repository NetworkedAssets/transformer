package com.networkedassets.condoc.transformer.managePermissions

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import com.networkedassets.condoc.transformer.common.SourceUnit
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureNode
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode
import spock.lang.Specification

class SourceStructureNodeSpec extends Specification {
    def "returns last part of it's sourceNodeIdentifier as it's name"() {
        when:
        SourceStructureNode structureNode = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB|CCC|DDD"))

        then:
        structureNode.getNodeName() == "DDD"
    }

    def "is equal to any Node with the same SourceNodeIdentifier"() {
        given:
        SourceStructureNode structureNode1 = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB|CCC|DDD"))
        SourceStructureNode structureNode2 = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB|CCC|DDD"))

        expect:
        structureNode1 == structureNode2
    }

    def "adds itself as parent's children when the parent is set"() {
        given:
        SourceStructureNode parent = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB"))
        SourceStructureNode son = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB|CCC"))

        when:
        son.setParent(parent)
        then:
        parent.children.size() == 1
        parent.children.get(0) == son
        son.parent.isPresent()
        son.parent.get() == parent
    }

    def "adds itself as new children's parent when the child is added"() {
        given:
        SourceStructureNode parent = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB"))
        SourceStructureNode son = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB|CCC"))

        when:
        parent.addChildren(son)

        then:
        parent.children.size() == 1
        parent.children.get(0) == son
        son.parent.isPresent()
        son.parent.get() == parent
    }

    def "isLeaf() returns value basing on whether it has children"() {
        given:
        SourceStructureNode parent = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB"))
        SourceStructureNode son = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB|CCC"))

        expect:
        parent.isLeaf()

        when:
        parent.addChildren(son)
        then:
        !parent.isLeaf()
    }

    def "isRoot() returns value basing on whether it has parent"() {
        given:
        SourceStructureNode parent = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB"))
        SourceStructureNode son = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB|CCC"))

        expect:
        boolean isRoot = son.isRoot()

        when:
        son.setParent(parent)
        then:
        !son.isRoot()
    }

    def "doesn't accept node with different source identifier as child or parent"() {
        given:
        SourceStructureNode parent = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB"))
        SourceStructureNode son = new SourceStructureNode(new SourceNodeIdentifier(10, "BBB|CCC"))

        when:
        parent.addChildren(son)
        then:
        thrown(IllegalArgumentException.class)
        parent.isLeaf()
        son.isRoot()

        when:
        son.setParent(parent)
        then:
        thrown(IllegalArgumentException.class)
        parent.isLeaf()
        son.isRoot()
    }

    def "doesn't accept itself as a child or parent"() {
        given:
        SourceStructureNode structureNode = new SourceStructureNode(new SourceNodeIdentifier(0, "BBB"))

        when:
        structureNode.addChildren(structureNode)
        then:
        thrown(IllegalArgumentException.class)
        structureNode.isLeaf()
        structureNode.isRoot()

        when:
        structureNode.setParent(structureNode)
        then:
        thrown(IllegalArgumentException.class)
        structureNode.isLeaf()
        structureNode.isRoot()
    }

    def "returns leafs of the tree below"() {
        given:
        /*
        this structure
                    +--+
                    |N1|
                    ++-+
                     |
                     |
                +--+-+---+--+
                |N2|     |N3|
              +---++     +--+
              |   |
              |   |
            +-++  +---+
            |U1|   |U2|
            +--+   +--+

         */
        SourceStructureNode n1 = new SourceStructureRootNode(4)
        SourceStructureNode n2 = new SourceStructureNode(new SourceNodeIdentifier(4, "N2")).setParent(n1)
        SourceStructureNode n3 = new SourceStructureNode(new SourceNodeIdentifier(4, "N2")).setParent(n1)
        SourceUnit u1 = new SourceUnit(new SourceNodeIdentifier(4, "N2", "U1")).setParent(n2)
        SourceUnit u2 = new SourceUnit(new SourceNodeIdentifier(4, "N2", "U2")).setParent(n2)

        when:
        List<SourceUnit> units = n1.getBelongingSourceUnits()

        then:
        units.size() == 2
        units.contains(u1)
        units.contains(u2)
    }

}
