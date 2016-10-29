package com.networkedassets.condoc.bitbucketSourcePlugin.core;

import com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary.JsonConverter;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectJsonConverter implements JsonConverter<SourceStructureNode> {
    private SourceNodeIdentifier parentSourceNodeIdentifier;

    @Override
    public List<SourceStructureNode> convert(JSONObject jsonObject) {

        ArrayList<SourceStructureNode> projects = new ArrayList<>();
        JSONArray values = jsonObject.optJSONArray("values");
        if (Objects.isNull(values)) {
            throw new InvalidJsonException("Incorrect json:" + jsonObject.toString());
        }
        for (int i = 0; i < values.length(); i++) {
            JSONObject object = values.optJSONObject(i);
            projects.add(new SourceStructureNode(new SourceNodeIdentifier(this.parentSourceNodeIdentifier
                    .getSourceIdentifier(), object.getString("key"))));
        }


        return projects;
    }

    @Override
    public void setParentNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier) {
        this.parentSourceNodeIdentifier = sourceNodeIdentifier;

    }
}

