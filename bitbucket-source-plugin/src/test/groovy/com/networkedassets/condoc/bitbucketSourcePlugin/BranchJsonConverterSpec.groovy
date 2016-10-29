package com.networkedassets.condoc.bitbucketSourcePlugin

import com.networkedassets.condoc.bitbucketSourcePlugin.core.BranchJsonConverter
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import org.json.JSONObject
import spock.lang.Specification

class BranchJsonConverterSpec extends Specification {

    JSONObject object = new JSONObject("""
        {
            "size" : 16,
            "limit" : 25,
            "isLastPage" : true,
            "values" : [
                {
                    "id" : "refs/head/master",
                    "displayId" : "master",
                    "latestCommit" : "2412453252345325",
                    "latestChangeSet" : "36543653655365646464365",
                    "type" : "BRANCH",
                    "isDefault" : true
                },
                {
                    "id" : "refs/head/develop",
                    "displayId" : "develop",
                    "latestCommit" : "24242525425432524352345",
                    "latestChangeSet" : "29247592982759822745732",
                    "type" : "BRANCH",
                    "isDefault" : false
                }
            ],
            "start" : 0
        }
    """);

    def "should create list of two branch objects which displayId is master and develop"() {
        given:
        SourceNodeIdentifier parentSourceNodeIdentifier = new SourceNodeIdentifier(0, "proj", "repo")

        when:
        def branchJsonConverter = new BranchJsonConverter()
        branchJsonConverter.setParentNodeIdentifier(parentSourceNodeIdentifier)

        then:
        branchJsonConverter.convert(object).get(0).getSourceNodeIdentifier().lastPartOfUnitIdentifier == "master"
        branchJsonConverter.convert(object).get(1).getSourceNodeIdentifier().lastPartOfUnitIdentifier == "develop"
    }
}
