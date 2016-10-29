package com.networkedassets.condoc.bitbucketSourcePlugin

import com.networkedassets.condoc.bitbucketSourcePlugin.core.BranchJsonConverter
import com.networkedassets.condoc.bitbucketSourcePlugin.core.DefaultJsonConverterFactory
import com.networkedassets.condoc.bitbucketSourcePlugin.core.ProjectJsonConverter
import com.networkedassets.condoc.bitbucketSourcePlugin.core.RepositoryJsonConverter
import spock.lang.Shared
import spock.lang.Specification

class DefaultJsonConverterFactorySpec extends Specification {

    @Shared def factory = new DefaultJsonConverterFactory()
    @Shared def projectConverter = new ProjectJsonConverter().getClass()
    @Shared def repositoryConverter = new RepositoryJsonConverter().getClass()
    @Shared def branchConverter = new BranchJsonConverter().getClass()

    def "should create correct converter for given url"() {

        when:

        def converter = factory.getConverterForUrl(request)

        then:
        converter.class==expectedConverter

        where:
        request                                                         | expectedConverter
        "http://localhost/bitbucket/projects"                           | projectConverter
        "http://localhost/bitbucket/projects/TEST1/repos"               | repositoryConverter
        "http://localhost/bitbucket/projects/TEST/repos/REPO1/branches" | branchConverter
    }


}



