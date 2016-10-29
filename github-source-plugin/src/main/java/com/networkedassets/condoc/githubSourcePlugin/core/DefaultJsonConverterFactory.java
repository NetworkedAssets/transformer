package com.networkedassets.condoc.githubSourcePlugin.core;


import com.networkedassets.condoc.githubSourcePlugin.core.boundary.JsonConverter;
import com.networkedassets.condoc.githubSourcePlugin.core.boundary.JsonConverterFactory;

public class DefaultJsonConverterFactory implements JsonConverterFactory {

    @Override
    public JsonConverter getConverterForUrl(String url) {

        String part[] = url.split("/");

        switch (part[part.length - 1]) {

            case "repos":
                return new RepositoryJsonConverter();

            case "branches":
                return new BranchJsonConverter();

            default:
                return new RepositoryJsonConverter();
        }

    }
}
