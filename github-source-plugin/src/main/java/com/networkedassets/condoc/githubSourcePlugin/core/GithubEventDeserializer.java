package com.networkedassets.condoc.githubSourcePlugin.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.networkedassets.condoc.githubSourcePlugin.core.entity.GithubEvent;

import java.io.IOException;

public class GithubEventDeserializer extends JsonDeserializer<GithubEvent> {
    private final String githubUrl = "https://github.com";

    @Override
    public GithubEvent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws
            IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String branchName;
        String repositoryName;
        String ownerName;
        try {
            branchName = node.get("ref").asText();
            repositoryName = node.get("repository").get("name").asText();
            ownerName = node.get("repository").get("owner").get("name").asText();
        } catch (Exception e) {
            throw new RuntimeException("One of the properties: ref, repository->name, repository->owner->name is " +
                    "missing in the input JSON");
        }
        return new GithubEvent(githubUrl, repositoryName, branchName, ownerName);
    }
}
