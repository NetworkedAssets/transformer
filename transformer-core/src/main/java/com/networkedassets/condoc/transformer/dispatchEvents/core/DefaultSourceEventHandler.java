package com.networkedassets.condoc.transformer.dispatchEvents.core;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.exceptions.SourceNotFoundException;
import com.networkedassets.condoc.transformer.dispatchEvents.core.boundary.provide.SourceEventHandler;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourceEvent;
import com.networkedassets.condoc.transformer.manageSourceUnits.core.boundary.SourceUnitManager;
import com.networkedassets.condoc.transformer.manageSources.core.boundary.SourceManager;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;
import com.networkedassets.condoc.transformer.produceDocumentation.core.boundary.DocumentationProducer;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton(name = "EventDispatcher")
public class DefaultSourceEventHandler implements SourceEventHandler {
    @Inject
    private Logger logger;
    @Inject
    private SourceManager sourceManager;
    @Inject
    private SourceUnitManager sourceUnitManager;
    @Inject
    private DocumentationProducer documentationProducer;


    @Override
    public void dispatchEvent(@Observes SourceEvent event) {
        logger.log(Level.INFO, "SourceEvent obtained: " + event.toString());
        SourceNodeIdentifier sourceNodeIdentifier = getSourceIdentifierForEvent(event);
        if (sourceUnitManager.isListened(sourceNodeIdentifier)) {
            documentationProducer.produceDocumentationForSourceUnit(sourceNodeIdentifier);
        }
    }

    private SourceNodeIdentifier getSourceIdentifierForEvent(SourceEvent ev) {
        Source source = sourceManager.getByUrl(ev.getSourceUrl()).orElseThrow(SourceNotFoundException::new);
        return new SourceNodeIdentifier(source.getId(), ev.getUnitIdentifier());
    }
}