package com.networkedassets.condoc.transformer.dispatchRawData.core;

import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.SourceUnit;
import com.networkedassets.condoc.transformer.common.exceptions.SourceUnitNotFoundException;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.RawDataDispatcher;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.ConverterPlugin;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.PluginManager;
import com.networkedassets.condoc.transformer.manageSourceUnits.core.boundary.SourceUnitManager;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
public class DefaultRawDataDispatcher implements RawDataDispatcher {

    @Inject
    private SourceUnitManager sourceUnitManager;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private Logger logger;

    @Override
    public void dispatch(SourceNodeIdentifier sourceNodeIdentifier, RawData rawData) {
        SourceUnit sourceUnit = sourceUnitManager.get(sourceNodeIdentifier).orElseThrow(SourceUnitNotFoundException::new);

        Set<Documentation> documentations = new HashSet<>();

        pluginManager.getConverterPlugins()
                .parallelStream()
                .map(plugin -> {
                    try {
                        logger.info(plugin.getIdentifier() + " started!");
                        return plugin.convert(rawData);
                    } catch (Exception e) {
                        logger.throwing(ConverterPlugin.class.getName(), "convert", e);
                        return null;
                    } finally {
                        logger.info(plugin.getIdentifier() + " ended!");
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Documentation::getType))
                .forEach((type, documentationList) -> {
                    if (documentationList.isEmpty()) return;
                    Documentation documentation = new Documentation();
                    documentation.setType(type);
                    documentationList.forEach(doc -> documentation.addDocItems(doc.getDocItems()));
                    documentations.add(documentation);
                });

        sourceUnit.setDocumentations(documentations);
        logger.info("Finished producing documentation");

        sourceUnitManager.save(sourceUnit);
    }
}
