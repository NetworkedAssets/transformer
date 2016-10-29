package com.networkedassets.condoc.bitbucketSourcePlugin.core;


import com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary.JsonConverter;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RepositoryJsonConverter implements JsonConverter<SourceStructureNode> {
    private SourceNodeIdentifier parentSourceNodeIdentifier;

    @Override
    public List<SourceStructureNode> convert(JSONObject jsonObject) {

        ArrayList<SourceStructureNode> repositories = new ArrayList<>();
        JSONArray values = jsonObject.optJSONArray("values");
        if (Objects.isNull(values)) {
            throw new InvalidJsonException("Incorrect json:" + jsonObject.toString());
        }
        for (int i = 0; i < values.length(); i++) {
            JSONObject object = values.optJSONObject(i);
            repositories.add(new SourceStructureNode(new SourceNodeIdentifier(
                    this.parentSourceNodeIdentifier.getSourceIdentifier(),
                    this.parentSourceNodeIdentifier.getPartOfUnitIdentifier(0),
                    object.getString("slug"))));
        }

        return repositories;
    }

    @Override
    public void setParentNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier) {
        this.parentSourceNodeIdentifier = sourceNodeIdentifier;

    }
}
