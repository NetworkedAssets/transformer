package com.networkedassets.condoc.githubSourcePlugin.core.boundary;


public interface JsonConverterFactory {
    JsonConverter getConverterForUrl(String url);
}
