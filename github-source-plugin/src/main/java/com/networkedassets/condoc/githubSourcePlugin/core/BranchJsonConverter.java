package com.networkedassets.condoc.githubSourcePlugin.core;


import com.mashape.unirest.http.JsonNode;
import com.networkedassets.condoc.githubSourcePlugin.core.boundary.JsonConverter;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.SourceUnit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BranchJsonConverter implements JsonConverter<SourceUnit> {
    private SourceNodeIdentifier parentSourceNodeIdentifier;

    @Override
    public List<SourceUnit> convert(JsonNode body) {
        ArrayList<SourceUnit> branches = new ArrayList<>();

        if (!body.isArray()) {
            throw new InvalidJsonException(body.toString());
        }

        JSONArray array = body.getArray();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.optJSONObject(i);

            branches.add(new SourceUnit(new SourceNodeIdentifier(this.parentSourceNodeIdentifier
                    .getSourceIdentifier(), this.parentSourceNodeIdentifier.getPartOfUnitIdentifier(0), object
                    .getString("name"))));
        }

        return branches;
    }

    @Override
    public void setParentNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier) {
        this.parentSourceNodeIdentifier = sourceNodeIdentifier;
    }
}
