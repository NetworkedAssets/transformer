package com.networkedassets.condoc.transformer.managePlugins

import com.networkedassets.condoc.transformer.managePlugins.core.DefaultPluginManager
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.ConverterPlugin
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.PluginManager
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source
import spock.lang.Specification

class PluginManagerSpec extends Specification {
    PluginManager pluginManager = new DefaultPluginManager()
    SourcePlugin sourcePlugin1 = Stub(SourcePlugin.class)
    SourcePlugin sourcePlugin2 = Stub(SourcePlugin.class)
    SourcePlugin sourcePlugin3 = Stub(SourcePlugin.class)


    ConverterPlugin converterPlugin1 = Stub(ConverterPlugin.class)
    ConverterPlugin converterPlugin2 = Stub(ConverterPlugin.class)
    ConverterPlugin converterPlugin2a = Stub(ConverterPlugin.class)

    def setup() {
        sourcePlugin1.getIdentifier() >> "AAA"
        sourcePlugin2.getIdentifier() >> "BBB"
        sourcePlugin3.getIdentifier() >> "CCC"
        converterPlugin1.getIdentifier() >> "DDD"
        converterPlugin2.getIdentifier() >> "SAME"
        converterPlugin2a.getIdentifier() >> "SAME"
    }

    def "registers plugins and return them by type"() {
        when:
        pluginManager.register(sourcePlugin1)
        pluginManager.register(sourcePlugin2)
        pluginManager.register(sourcePlugin3)
        pluginManager.register(converterPlugin1)
        pluginManager.register(converterPlugin2)

        then:
        def expectedConverters = [converterPlugin1, converterPlugin2]
        pluginManager.getConverterPlugins().size() == expectedConverters.size()
        pluginManager.getConverterPlugins().containsAll(expectedConverters)

        def expectedSourcePlugins = [sourcePlugin1, sourcePlugin2, sourcePlugin3]
        pluginManager.getSourcePlugins().size() == expectedSourcePlugins.size()
        pluginManager.getSourcePlugins().containsAll(expectedSourcePlugins)

    }

    def "doesn't register plugins with same identifiers"() {
        when:
        pluginManager.register(converterPlugin2)
        pluginManager.register(converterPlugin2a)
        then:
        pluginManager.getConverterPlugins().size()==1
        pluginManager.getConverterPlugins().getAt(0).getIdentifier() == converterPlugin2.getIdentifier();

    }

    def "returns proper source plugin for given PluginIdentifier"() {
        given:
        Source source1 = new Source();
        source1.setPluginIdentifier("AAA");

        when:
        pluginManager.register(sourcePlugin1)
        then:
        def optional = pluginManager.getSourcePluginForGivenSource(source1);
        optional.isPresent()
        optional.get().getIdentifier().equals(sourcePlugin1.getIdentifier())
    }

}
