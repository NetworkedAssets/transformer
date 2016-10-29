package com.networkedassets.condoc.bitbucketSourcePlugin

import com.networkedassets.condoc.bitbucketSourcePlugin.core.DefaultBitbucketClient
import spock.lang.Specification

class DefaultBitbucketClientRepositoryPathSpec extends Specification {


    def client = new DefaultBitbucketClient()


    def "should create correct repositoryPath basing on git instance url without port and source id"() {

        when:

        def repositoryPath = client.getRepositoryPath("http://bitbucket.networkedassets.net", "usr1", "naatlas", "condoc")


        then:
        repositoryPath == "http://usr1@bitbucket.networkedassets.net/scm/naatlas/condoc.git"
    }

    def "should create correct repositoryPath basing on git instance url with port and source id "() {

        when:

        def repositoryPath = client.getRepositoryPath("http://bitbucket.networkedassets.net:5520", "usr1", "naatlas",
                "condoc")


        then:
        repositoryPath == "http://usr1@bitbucket.networkedassets.net:5520/scm/naatlas/condoc.git"
    }


}
