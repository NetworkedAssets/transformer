package com.networkedassets.condoc.transformer.manageSources

import com.google.common.collect.ImmutableList
import com.networkedassets.condoc.transformer.common.PersistenceTestingAbility
import com.networkedassets.condoc.transformer.common.exceptions.WrongSourceSettingsException
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.PluginManager
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin
import com.networkedassets.condoc.transformer.manageSources.core.boundary.SourceManager
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source
import com.networkedassets.condoc.transformer.manageSources.core.entity.SourceSettingField
import com.networkedassets.condoc.transformer.manageSources.infrastructure.repository.db.jpa.JpaSourceManager
import spock.lang.Specification

class SourceManagerSpec extends Specification implements PersistenceTestingAbility {
    static SourceManager sourceManager

    def setupSpec() {
        sourceManager = new JpaSourceManager()
        sourceManager.em = entityManager
    }

    def "persists Sources and returns them by ID"() {
        given:
        SourcePlugin sourcePlugin = Stub(SourcePlugin.class)
        sourcePlugin.verify(_) >> SourcePlugin.VerificationStatus.OK
        PluginManager pluginManager = Stub(PluginManager.class)
        pluginManager.getSourcePluginForGivenSource(_) >> Optional.of(sourcePlugin)
        sourceManager.pluginManager = pluginManager


        def settings1 = new Source()
        settings1
                .setPluginIdentifier("bitbucket-source")
                .setUrl("aaa.example.com")
        def settings2 = new Source()
        settings2
                .setPluginIdentifier("github-source")
                .setUrl("bbb.example.net")

        when:
        sourceManager.verifyAndPersist(settings1)
        sourceManager.verifyAndPersist(settings2)
        flushAndClearCache()

        then:
        sourceManager.allSources.size() == 2
        sourceManager.getByIdentifier(settings1.id).get().pluginIdentifier == "bitbucket-source"
        sourceManager.getByIdentifier(settings2.id).get().pluginIdentifier == "github-source"
    }

    def "persists Source's settings"() {
        given:
        SourcePlugin sourcePlugin = Stub(SourcePlugin.class)
        sourcePlugin.verify(_) >> SourcePlugin.VerificationStatus.OK
        PluginManager pluginManager = Stub(PluginManager.class)
        pluginManager.getSourcePluginForGivenSource(_) >> Optional.of(sourcePlugin)
        sourceManager.pluginManager = pluginManager

        def source = new Source()
                .setPluginIdentifier("bitbucket-source")
                .setUrl("aaa.example.com")
        def settings1 = ImmutableList.of(
                new SourceSettingField().setName("username").setType(SourceSettingField.Type.TEXT).setValue("admin"),
                new SourceSettingField().setName("password").setType(SourceSettingField.Type.PASSWORD).setValue("admin"),
        )
        def settings2 = ImmutableList.of(
                new SourceSettingField().setName("username").setType(SourceSettingField.Type.TEXT).setValue("admin2"),
                new SourceSettingField().setName("password").setType(SourceSettingField.Type.TEXT).setValue("admin"),
        )
        source.settings = settings1
        sourceManager.verifyAndPersist(source)
        flushAndClearCache()

        when:
        source.settings = settings2
        sourceManager.verifyAndSave(source.id, source)
        flushAndClearCache()

        then:
        Source retrievedSource = sourceManager.getByIdentifier(source.id).get()
        retrievedSource.settings.size() == settings2.size()
        settingsFieldsEqualInBusinessTerms(retrievedSource.getSettingByKey("username").get(), settings2[0])
        settingsFieldsEqualInBusinessTerms(retrievedSource.getSettingByKey("password").get(), settings2[1])

    }

    void settingsFieldsEqualInBusinessTerms(SourceSettingField settings1, SourceSettingField settings2) {
        assert settings1.name == settings2.name
        assert settings1.type == settings2.type
        assert settings1.value == settings2.value
    }

    def "throws WrongSourceSettingsException when wrong settings provided"() {
        given:
        SourcePlugin sourcePlugin = Stub(SourcePlugin.class)
        sourcePlugin.verify(_) >> SourcePlugin.VerificationStatus.WRONG_CREDENTIALS
        PluginManager pluginManager = Stub(PluginManager.class)
        pluginManager.getSourcePluginForGivenSource(_) >> Optional.of(sourcePlugin)
        sourceManager.pluginManager = pluginManager

        def wrongSettings = new Source()
        wrongSettings.setPluginIdentifier("AAA").setUrl("BBB")

        when:
        sourceManager.verifyAndPersist(wrongSettings)

        then:
        WrongSourceSettingsException ex = thrown()
        ex.getMessage() == SourcePlugin.VerificationStatus.WRONG_CREDENTIALS.toString()

    }
}
