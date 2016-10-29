package com.networkedassets.condoc.transformer.managePlugins.core;

import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.*;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;

import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Singleton(name = "PluginManager")
public class DefaultPluginManager implements PluginManager {
    private final int concurrencyTimeout = 30;

    @Inject
    private SourceNodeIdentifierPublisher publisher;

    private Map<String, Plugin> registeredPlugins = new HashMap<>();

    @AccessTimeout(value = concurrencyTimeout, unit = TimeUnit.SECONDS)
    @Override
    public void register(Plugin plugin) {
        registeredPlugins.put(plugin.getIdentifier(), plugin);
    }

    @AccessTimeout(value = concurrencyTimeout, unit = TimeUnit.SECONDS)
    @Override
    public void unregister(Plugin plugin) {
        registeredPlugins.remove(plugin.getIdentifier());
    }

    @Override
    public List<ConverterPlugin> getConverterPlugins() {
        return registeredPlugins.values().stream().filter(p -> p instanceof ConverterPlugin)
                .map(c -> (ConverterPlugin) c).collect(Collectors.toList());
    }

    @Override
    public List<SourcePlugin> getSourcePlugins() {
        return registeredPlugins.values().stream().filter(p -> p instanceof SourcePlugin)
                .map(c -> (SourcePlugin) c).collect(Collectors.toList());
    }

    @Override
    public Optional<Plugin> getPluginBy(String pluginIdentifier) {
        return registeredPlugins.values().stream().filter(p -> p.getIdentifier().equals(pluginIdentifier))
                .findAny();
    }

    @Override
    public Optional<SourcePlugin> getSourcePluginForGivenSource(Source source) {
        return registeredPlugins.values().stream()
                .filter(p -> p.getIdentifier().equals(source.getPluginIdentifier()))
                .map(p -> (SourcePlugin) p)
                .findAny();
    }

    @Override
    public SourceNodeIdentifierPublisher getPublisher() {
        return publisher;
    }
}