package com.networkedassets.condoc.bitbucketSourcePlugin

import com.networkedassets.condoc.bitbucketSourcePlugin.core.DefaultBitbucketClient
import com.networkedassets.condoc.bitbucketSourcePlugin.core.DefaultJsonConverterFactory
import com.networkedassets.condoc.bitbucketSourcePlugin.core.InvalidJsonException
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source
import com.networkedassets.condoc.transformer.manageSources.core.entity.SourceSettingField
import org.junit.experimental.categories.Category
import spock.lang.IgnoreIf
import spock.lang.Specification

@Category(IntegrationTest)
class DefaultBitbucketClientSpec extends Specification {

    def factory = new DefaultJsonConverterFactory()
    def client = new DefaultBitbucketClient()
    def password = System.getProperty("pwd")
    def username = System.getProperty("usr")
    def url = System.getProperty("bitbucketUrl")


    def setup() {
        client.jsonConverterFactory = factory

    }


    @IgnoreIf({
        System.getProperty("pwd")?.size() <= 0 ||
                System.getProperty("usr")?.size() <= 0 ||
                System.getProperty("bitbucketUrl")?.size() <= 0
    })
    def "Should return VerificationStatus.OK If source has given correct login data and exists"() {

        given:
        def source = new Source().setSettings([new SourceSettingField().setName("username").setValue(username), new
                SourceSettingField().setName("password").setValue(password)])
                .setUrl(url)


        when:

        SourcePlugin.VerificationStatus status = client.isVerified(source)

        then:

        status == SourcePlugin.VerificationStatus.OK

    }

    @IgnoreIf({
        System.getProperty("bitbucketUrl")?.size() <= 0
    })
    def "Should return VerificationStatus.WRONG_CREDENTIALS If source has given uncorrect login data and exists"() {

        given:
        def source = new Source().setSettings([new SourceSettingField().setName("username").setValue("admnin2"), new
                SourceSettingField().setName("password").setValue("admin2")])
                .setUrl(url)


        when:

        SourcePlugin.VerificationStatus status = client.isVerified(source)

        then:

        status == SourcePlugin.VerificationStatus.WRONG_CREDENTIALS

    }

    def "Should return VerificationStatus.SOURCE_NOT_FOUND If source does not exist"() {

        given:
        def source = new Source().setSettings([new SourceSettingField().setName("username").setValue("admnin2"), new
                SourceSettingField().setName("password").setValue("admin2")])
                .setUrl("http://atlasdemo.networkedassets.net/bitbucket2")


        when:

        SourcePlugin.VerificationStatus status = client.isVerified(source)

        then:

        status == SourcePlugin.VerificationStatus.SOURCE_NOT_FOUND

    }

    @IgnoreIf({
        System.getProperty("pwd")?.size() <= 0 ||
                System.getProperty("usr")?.size() <= 0 ||
                System.getProperty("bitbucketUrl")?.size() <= 0
    })
    def "Should create correct SourceStructureRootNode"() {

        given:
        def source = new Source().setSettings([new SourceSettingField().setName("username").setValue(username), new
                SourceSettingField().setName("password").setValue(password)])
                .setUrl(url).setId(0)


        when:

        SourceStructureRootNode sourceStructureRootNode = client.fetchStructure(source)

        then:

        sourceStructureRootNode.getChildren().size() > 0
        sourceStructureRootNode.getChildren().get(0).getChildren().size() > 0
        sourceStructureRootNode.getChildren().get(0).getChildren().get(0).getChildren().size() > 0


    }


    @IgnoreIf({ System.getProperty("bitbucketUrl")?.size() <= 0 })
    def "If uncorrect source data throw InvalidJsonException"() {

        given:
        def source = new Source().setSettings([new SourceSettingField().setName("username").setValue("admnin2"), new
                SourceSettingField().setName("password").setValue("admin2")])
                .setUrl(url)

        when:


        client.fetchStructure(source)

        then:
        thrown InvalidJsonException

    }
}
