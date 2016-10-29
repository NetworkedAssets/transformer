package com.networkedassets.condoc.bitbucketSourcePlugin

import com.networkedassets.condoc.bitbucketSourcePlugin.core.BitbucketSourcePlugin
import com.networkedassets.condoc.bitbucketSourcePlugin.core.DefaultBitbucketClient
import com.networkedassets.condoc.bitbucketSourcePlugin.core.DefaultJsonConverterFactory
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source
import com.networkedassets.condoc.transformer.manageSources.core.entity.SourceSettingField
import org.junit.experimental.categories.Category
import spock.lang.IgnoreIf
import spock.lang.Specification

@Category(IntegrationTest)
class BitbucketSourcePluginSpec extends Specification {


    def factory = new DefaultJsonConverterFactory()
    def client = new DefaultBitbucketClient()
    def plugin = new BitbucketSourcePlugin()
    def password = System.getProperty("pwd")
    def username = System.getProperty("usr")
    def url = System.getProperty("bitbucketUrl")
    def sourceNodeId = System.getProperty("sourceNodeId")


    def setup() {

        client.jsonConverterFactory = factory
        plugin.bitbucketClient = client

    }

    @IgnoreIf({
        System.getProperty("pwd")?.size() <= 0 ||
                System.getProperty("usr")?.size() <= 0 ||
                System.getProperty("bitbucketUrl")?.size() <= 0
    })
    def "Should return VerificationStatus.OK for source"() {

        given:

        def source = new Source()
        source.url = url
        source.settings = [new SourceSettingField().setName("username").setValue(username), new SourceSettingField()
                .setName("password").setValue(password)]


        when:

        SourcePlugin.VerificationStatus status = plugin.verify(source)
        then:

        status == SourcePlugin.VerificationStatus.OK

    }


    @IgnoreIf({
        System.getProperty("pwd")?.size() <= 0 ||
                System.getProperty("usr")?.size() <= 0 ||
                System.getProperty("bitbucketUrl")?.size() <= 0 ||
                System.getProperty("sourceNodeId")?.size() <= 0
    })
    def "Should return correct rawData"() {

        given:

        def source = new Source().setPluginIdentifier(plugin.getIdentifier()).setId(0)
        def sourceUnidIdentifier = new SourceNodeIdentifier(0, sourceNodeId)
        source.url = url
        source.settings = [new SourceSettingField().setName("username").setValue(username), new SourceSettingField()
                .setName("password").setValue(password)]


        when:

        def rawData = plugin.fetchRawData(sourceUnidIdentifier,source)
        then:

        rawData.isPresent()

    }







    @IgnoreIf({
        System.getProperty("pwd")?.size() <= 0 ||
                System.getProperty("usr")?.size() <= 0 ||
                System.getProperty("bitbucketUrl")?.size() <= 0
    })
    def "Should create correct SourceStructureRootNode"() {

        given:

        def source = new Source()
        source.url = url
        source.id = 0
        source.settings = [new SourceSettingField().setName("username").setValue(username), new SourceSettingField()
                .setName("password").setValue(password)]


        when:

        SourceStructureRootNode sourceStructureRootNode = plugin.fetchStructureForSource(source)

        then:

        sourceStructureRootNode.getChildren().size() > 0
        sourceStructureRootNode.getChildren().get(0).getChildren().size() > 0
        sourceStructureRootNode.getChildren().get(0).getChildren().get(0).getChildren().size() > 0

    }


    def "Should return VerificationStatus.SOURCE_NOT_FOUND If source does not exist"() {

        given:

        def source = new Source()
        source.url = "http://atlasdemo.networkedassets.net/bitbucket2"
        source.settings = [new SourceSettingField().setName("username").setValue("admin"), new SourceSettingField()
                .setName("password").setValue("admin")]


        when:

        SourcePlugin.VerificationStatus status = plugin.verify(source)
        then:

        status == SourcePlugin.VerificationStatus.SOURCE_NOT_FOUND

    }

}
