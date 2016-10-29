package com.networkedassets.condoc.githubSourcePlugin

import com.mashape.unirest.http.JsonNode
import com.networkedassets.condoc.githubSourcePlugin.core.RepositoryJsonConverter
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import spock.lang.Specification

class RepositoryJsonConverterSpec extends Specification {

    def json = new JsonNode("[{" +
            " \"id\" : 5944245245425, " +
            "\"name\" : \"ATC\", " +
            "\"full_name\" : \"NetworkedAssets/atc\", " +
            "},{" +
            " \"id\" : 34124124124, " +
            "\"name\" : \"ATC2\", " +
            "\"full_name\" : \"NetworkedAssets/atc2\"," +
            "}]"
    );


    def "should create list of two repository objects which name is TEST_1 and TEST_2"() {

        given:

        SourceNodeIdentifier parentSourceNodeIdentifier = new SourceNodeIdentifier(0, "proj")

        when:

        def repositoryJsonConverter = new RepositoryJsonConverter()
        repositoryJsonConverter.setParentNodeIdentifier(parentSourceNodeIdentifier)


        then:
        repositoryJsonConverter.convert(json).get(0).getSourceNodeIdentifier()
                .getLastPartOfUnitIdentifier() == "NetworkedAssets/atc"
        repositoryJsonConverter.convert(json).get(1).getSourceNodeIdentifier()
                .getLastPartOfUnitIdentifier() == "NetworkedAssets/atc2"

    }


}
