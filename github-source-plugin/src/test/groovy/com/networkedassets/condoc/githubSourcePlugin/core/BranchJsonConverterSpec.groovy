package com.networkedassets.condoc.githubSourcePlugin

import com.mashape.unirest.http.JsonNode
import com.networkedassets.condoc.githubSourcePlugin.core.BranchJsonConverter
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import spock.lang.Specification

class BranchJsonConverterSpec extends Specification {

    def json = new JsonNode("[{" +
            " \"name\" : \"master\", " +
            "},{" + " \"name\" : \"develop\", " + "}]"
    );


    def "should create list of two branch objects which displayId is master and develop"() {


        given:

        SourceNodeIdentifier parentSourceNodeIdentifier = new SourceNodeIdentifier(0, "proj", "repo")


        when:

        def branchJsonConverter = new BranchJsonConverter()
        branchJsonConverter.setParentNodeIdentifier(parentSourceNodeIdentifier)


        then:
        branchJsonConverter.convert(json).get(0).getSourceNodeIdentifier().lastPartOfUnitIdentifier == "master"
        branchJsonConverter.convert(json).get(1).getSourceNodeIdentifier().lastPartOfUnitIdentifier == "develop"
    }


}
