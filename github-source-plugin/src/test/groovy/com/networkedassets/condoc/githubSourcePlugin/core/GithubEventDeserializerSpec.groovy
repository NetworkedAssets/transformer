package com.networkedassets.condoc.githubSourcePlugin.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.networkedassets.condoc.githubSourcePlugin.core.entity.GithubEvent
import spock.lang.Specification

class GithubEventDeserializerSpec extends Specification {
    def "should build GithubEvent from githubs JSON properly"() {
        final String JSON = """{
                                    "ref": "refs/heads/changes",
                                    "repository": {
                                        "name": "public-repo",
                                        "owner": {
                                            "name": "NetworkedAssets"
                                        }
                                    }
                                }"""

        when:
        ObjectMapper mapper = new ObjectMapper()
        SimpleModule module = new SimpleModule()
        module.addDeserializer(GithubEvent.class, new GithubEventDeserializer())
        mapper.registerModule(module)

        GithubEvent githubEvent = mapper.readValue(JSON, GithubEvent.class)

        then:
        githubEvent.isValid()
    }
}