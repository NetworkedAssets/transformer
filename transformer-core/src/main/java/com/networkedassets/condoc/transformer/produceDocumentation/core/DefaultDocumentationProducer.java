package com.networkedassets.condoc.transformer.produceDocumentation.core;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.exceptions.BundleNotFoundException;
import com.networkedassets.condoc.transformer.common.exceptions.SourceNotFoundException;
import com.networkedassets.condoc.transformer.common.exceptions.SourceUnavailableException;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.RawDataDispatcher;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.manageBundles.core.boundary.BundleManager;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.PluginNotFoundException;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.PluginManager;
import com.networkedassets.condoc.transformer.manageSources.core.boundary.SourceManager;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;
import com.networkedassets.condoc.transformer.produceDocumentation.core.boundary.DocumentationProducer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultDocumentationProducer implements DocumentationProducer {

    @Inject
    private SourceManager sourceManager;

    @Inject
    private BundleManager bundleManager;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private RawDataDispatcher rawDataDispatcher;

    @Override
    public void produceDocumentationForSourceUnit(SourceNodeIdentifier sourceNodeIdentifier) {
        Source source = sourceManager.getBySourceNodeIdentifier(sourceNodeIdentifier)
                .orElseThrow(SourceNotFoundException::new);
        RawData rawData = pluginManager.getSourcePluginForGivenSource(source)
                .orElseThrow(PluginNotFoundException::new)
                .fetchRawData(sourceNodeIdentifier, source)
                .orElseThrow(SourceUnavailableException::new);
        rawDataDispatcher.dispatch(sourceNodeIdentifier, rawData);
    }

    @Override
    public void produceDocumentationForBundle(int bundleId) {
        bundleManager.getById(bundleId)
                .orElseThrow(BundleNotFoundException::new)
                .getSourceUnits()
                .forEach(sourceUnit -> produceDocumentationForSourceUnit(sourceUnit.getSourceNodeIdentifier()));
    }
}
