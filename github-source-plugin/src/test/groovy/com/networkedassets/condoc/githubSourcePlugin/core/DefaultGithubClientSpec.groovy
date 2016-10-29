package com.networkedassets.condoc.githubSourcePlugin

import com.networkedassets.condoc.githubSourcePlugin.core.*
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source
import com.networkedassets.condoc.transformer.manageSources.core.entity.SourceSettingField
import org.junit.experimental.categories.Category
import spock.lang.IgnoreIf
import spock.lang.Specification

@Category(IntegrationTest)
class DefaultGithubClientSpec extends Specification {


    def client = new DefaultGithubClient()
    def factory = new DefaultJsonConverterFactory()
    def parser = new DefaultGithubHeaderParser()
    def password = System.getProperty("pwd")
    def username = System.getProperty("usr")


    def setup() {
        client.jsonConverterFactory = factory
        client.githubHeaderParser = parser
    }

    @IgnoreIf({ System.getProperty("pwd")?.size() <= 0 || System.getProperty("usr")?.size() <= 0 })
    def "If uncorrect source data throw InvalidJsonException"() {

        given:
        def source = new Source()
        source.url = "https://github.com"
        source.settings = [new SourceSettingField().setName("username").setValue(username), new SourceSettingField()
                .setName("password").setValue("admin2")]

        when:


        client.fetchStructure(source)

        then:
        thrown InvalidJsonException

    }


}
