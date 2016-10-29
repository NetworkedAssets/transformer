package com.networkedassets.condoc.transformer.managePlugins.core.boundary;

public class PluginNotFoundException extends RuntimeException {
    public PluginNotFoundException() {
    }

    public PluginNotFoundException(String message) {
        super(message);
    }
}
