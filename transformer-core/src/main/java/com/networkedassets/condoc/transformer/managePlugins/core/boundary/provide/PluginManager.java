package com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide;

import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;

import javax.ejb.Local;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Optional;

@Local
public interface PluginManager {
    String JNDI_ADDRESS = "java:global/transformer-core/PluginManager!" +
            "com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.PluginManager";
    static PluginManager get() throws NamingException {
        return (PluginManager) new InitialContext().lookup(JNDI_ADDRESS);
    }

    void register(Plugin pluginClass);

    void unregister(Plugin pluginClass);

    List<ConverterPlugin> getConverterPlugins();
    List<SourcePlugin> getSourcePlugins();

    Optional<Plugin> getPluginBy(String pluginIdentifier);

    Optional<SourcePlugin> getSourcePluginForGivenSource(Source source);

    SourceNodeIdentifierPublisher getPublisher();

}
