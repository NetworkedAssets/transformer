package com.networkedassets.condoc.githubSourcePlugin

import com.networkedassets.condoc.githubSourcePlugin.core.DefaultGithubHeaderParser
import spock.lang.Specification

class DefaultGithubHeaderParserSpec extends Specification {


    def parser = new DefaultGithubHeaderParser()

    def "Should return last page number for given link header"() {

        given:
        def linkHeader =
                '<https://api.github.com/user/9287/repos?page=3&per_page=100>; rel="next", ' +
                        '<https://api.github.com/user/9287/repos?page=1&per_page=100>; rel="prev"; pet="cat", ' +
                        '<https://api.github.com/user/9287/repos?page=5&per_page=100>; rel="last"'

        when:

        def link = parser.getLastPage(linkHeader)
        //check if cache is working
        def link1 = parser.getLastPage(linkHeader)

        then:

        link == 5
        link1 == 5

    }

}
