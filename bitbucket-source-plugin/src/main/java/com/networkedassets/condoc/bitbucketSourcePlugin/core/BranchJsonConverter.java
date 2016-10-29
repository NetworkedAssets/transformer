package com.networkedassets.condoc.bitbucketSourcePlugin.core;


import com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary.JsonConverter;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.SourceUnit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BranchJsonConverter implements JsonConverter<SourceUnit> {
    private SourceNodeIdentifier parentSourceNodeIdentifier;

    @Override
    public List<SourceUnit> convert(JSONObject jsonObject) {
        ArrayList<SourceUnit> branches = new ArrayList<>();

        JSONArray values = jsonObject.optJSONArray("values");
        if (Objects.isNull(values)) {
            throw new InvalidJsonException("Incorrect json:" + jsonObject.toString());
        }

        for (int i = 0; i < values.length(); i++) {
            JSONObject object = values.optJSONObject(i);

            branches.add(new SourceUnit(new SourceNodeIdentifier(this.parentSourceNodeIdentifier.getSourceIdentifier(),
                    this.parentSourceNodeIdentifier.getPartOfUnitIdentifier(0), this.parentSourceNodeIdentifier
                    .getPartOfUnitIdentifier(1), object.getString("displayId"))));
        }

        return branches;
    }

    @Override
    public void setParentNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier) {
        this.parentSourceNodeIdentifier = sourceNodeIdentifier;
    }
}
