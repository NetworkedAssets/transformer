package com.networkedassets.condoc.bitbucketSourcePlugin

import com.networkedassets.condoc.bitbucketSourcePlugin.core.ProjectJsonConverter
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import org.json.JSONObject
import spock.lang.Specification

class ProjectJsonConverterSpec extends Specification {

    JSONObject object = new JSONObject("""
        { 
            "size" : 16,
            "limit" : 25,
            "isLastPage" : true,
            "values" : [
                {
                    "key" : "TEST1",
                    "name" : "TEST_1",
                    "id" : 241,
                    "description" : "NA Internal project",
                    "type" : "NORMAL",
                    "public" : false
                },
                {
                    "key" : "TEST2",
                    "name" : "TEST_2",
                    "id" : 242,
                    "description" : "NA Internal project 2",
                    "type" : "8NORMAL",
                    "public" : true
                }
            ],
            "start" : 0
        }
    """);


    def "should create list of two project which name is TEST_1 and TEST_2"() {
        given:

        SourceNodeIdentifier parentSourceNodeIdentifier = new SourceNodeIdentifier(1)


        when:

        def projectJsonConverter = new ProjectJsonConverter()
        projectJsonConverter.setParentNodeIdentifier(parentSourceNodeIdentifier)

        then:
        projectJsonConverter.convert(object).get(0).getSourceNodeIdentifier().getLastPartOfUnitIdentifier() == "TEST1"
        projectJsonConverter.convert(object).get(1).getSourceNodeIdentifier().getLastPartOfUnitIdentifier() == "TEST2"

    }


}
