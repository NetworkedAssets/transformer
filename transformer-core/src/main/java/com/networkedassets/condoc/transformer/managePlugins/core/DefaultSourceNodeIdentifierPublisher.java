package com.networkedassets.condoc.transformer.managePlugins.core;

import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourceEvent;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourceNodeIdentifierPublisher;
import com.networkedassets.condoc.transformer.manageSources.core.boundary.SourceManager;

import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Singleton(name = "DefaultSourceNodeIdentifierPublisher")
public class DefaultSourceNodeIdentifierPublisher implements SourceNodeIdentifierPublisher {
    @Inject
    private Event<SourceEvent> events;
    @Inject
    private SourceManager sourceManager;

    @Override
    public void publish(SourceEvent sourceEvent) {
        events.fire(sourceEvent);
    }
}
