package com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary;


public interface JsonConverterFactory {
    JsonConverter getConverterForUrl(String url);
}
