package com.networkedassets.condoc.githubSourcePlugin.core;


import com.mashape.unirest.http.JsonNode;
import com.networkedassets.condoc.githubSourcePlugin.core.boundary.JsonConverter;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RepositoryJsonConverter implements JsonConverter<SourceStructureNode> {
    private SourceNodeIdentifier parentSourceNodeIdentifier;

    @Override
    public List<SourceStructureNode> convert(JsonNode body) {

        ArrayList<SourceStructureNode> repositories = new ArrayList<>();

        if (!body.isArray()) {
            throw new InvalidJsonException(body.toString());
        }
        JSONArray jsonArray = body.getArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.optJSONObject(i);
            repositories.add(new SourceStructureNode(new SourceNodeIdentifier(this.parentSourceNodeIdentifier
                    .getSourceIdentifier(), object.getString("full_name"))));
        }

        return repositories;
    }

    @Override
    public void setParentNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier) {
        this.parentSourceNodeIdentifier = sourceNodeIdentifier;
    }
}
