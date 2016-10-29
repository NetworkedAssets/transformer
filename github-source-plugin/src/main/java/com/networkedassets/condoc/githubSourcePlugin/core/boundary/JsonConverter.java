package com.networkedassets.condoc.githubSourcePlugin.core.boundary;

import com.mashape.unirest.http.JsonNode;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;

import java.util.List;

public interface JsonConverter<T> {

    List<T> convert(JsonNode body);

    void setParentNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier);
}
