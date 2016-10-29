package com.networkedassets.condoc.bitbucketSourcePlugin.core;

import com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary.JsonConverter;
import com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary.JsonConverterFactory;

public class DefaultJsonConverterFactory implements JsonConverterFactory {

    @Override
    public JsonConverter getConverterForUrl(final String url) {

        String part[] = url.split("/");

        switch (part[part.length - 1]) {
            case "projects":
                return new ProjectJsonConverter();

            case "repos":
                return new RepositoryJsonConverter();

            case "branches":
                return new BranchJsonConverter();

            default:
                return new ProjectJsonConverter();
        }

    }
}
