package com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide;

public class SourceEvent {
    private String sourceUrl;
    private String unitIdentifier;

    public SourceEvent(String sourceUrl, String unitIdentifier) {
        this.sourceUrl = sourceUrl;
        this.unitIdentifier = unitIdentifier;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getUnitIdentifier() {
        return unitIdentifier;
    }
}
