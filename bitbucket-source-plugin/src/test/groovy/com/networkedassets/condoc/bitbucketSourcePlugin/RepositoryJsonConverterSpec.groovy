package com.networkedassets.condoc.bitbucketSourcePlugin

import com.networkedassets.condoc.bitbucketSourcePlugin.core.RepositoryJsonConverter
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import org.json.JSONObject
import spock.lang.Specification

class RepositoryJsonConverterSpec extends Specification {

    JSONObject object = new JSONObject("{" +
            " \"size\" : 16, " +
            "\"limit\" : 25, " +
            "\"isLastPage\" : true, " +
            "\"values\" : [" +
            "{ " +
            "\"slug\" : \"TEST1\", " +
            "\"name\" : \"TEST_1\", " +
            "\"id\" : 241, " +
            "\"statusMessage\" : \"Available\", " +
            "\"scmId\" : \"git\", " +
            "\"forkable\" : true}, " +
            "{ " +
            "\"slug\" : \"TEST2\", " +
            "\"name\" : \"TEST_2\", " +
            "\"id\" : 242, " +
            "\"statusMessage\" : \"Available\", " +
            "\"scmId\" : \"git\"," +
            " \"forkable\" : true}], " +
            "\"start\" : 0}"
    );


    def "should create list of two repository objects which name is TEST_1 and TEST_2"() {

        given:

        SourceNodeIdentifier parentSourceNodeIdentifier=new SourceNodeIdentifier(0,"proj")

        when:

        def repositoryJsonConverter = new RepositoryJsonConverter()
        repositoryJsonConverter.setParentNodeIdentifier(parentSourceNodeIdentifier)


        then:
        repositoryJsonConverter.convert(object).get(0).getSourceNodeIdentifier().getLastPartOfUnitIdentifier() == "TEST1"
        repositoryJsonConverter.convert(object).get(1).getSourceNodeIdentifier().getLastPartOfUnitIdentifier() == "TEST2"

    }


}
