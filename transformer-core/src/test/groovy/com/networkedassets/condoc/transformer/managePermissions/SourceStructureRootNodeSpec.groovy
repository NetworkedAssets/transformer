package com.networkedassets.condoc.transformer.managePermissions

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureNode
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode
import spock.lang.Specification

class SourceStructureRootNodeSpec extends Specification {

    def "it's name is always 'root'"() {
        given:
        SourceStructureRootNode structureRootNode = new SourceStructureRootNode(1)

        expect:
        structureRootNode.getNodeName() == "root"
    }

    def "isRoot() always returns true"() {
        given:
        SourceStructureRootNode structureRootNode = new SourceStructureRootNode(1)
        expect:
        structureRootNode.isRoot()
    }

    def "a try to set parent raises an exception"() {
        given:
        SourceStructureRootNode structureRootNode = new SourceStructureRootNode(1)
        SourceStructureNode parent = new SourceStructureNode(new SourceNodeIdentifier(1, "BBB"))

        when:
        structureRootNode.setParent(parent)
        then:
        thrown(UnsupportedOperationException.class)
    }


}
