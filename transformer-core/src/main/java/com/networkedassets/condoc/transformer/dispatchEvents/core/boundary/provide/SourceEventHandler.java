package com.networkedassets.condoc.transformer.dispatchEvents.core.boundary.provide;

import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourceEvent;

public interface SourceEventHandler {
    void dispatchEvent(SourceEvent sourceNodeIdentifier);
}
