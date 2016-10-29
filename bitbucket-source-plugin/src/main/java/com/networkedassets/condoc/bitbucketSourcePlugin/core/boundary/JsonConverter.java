package com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.SourceUnit;
import org.json.JSONObject;

import java.util.List;

public interface JsonConverter<T> {

    List<T> convert(JSONObject jsonObject);
    void setParentNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier);
}
