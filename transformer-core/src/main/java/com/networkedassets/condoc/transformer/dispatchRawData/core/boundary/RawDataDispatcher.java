package com.networkedassets.condoc.transformer.dispatchRawData.core.boundary;


import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;

public interface RawDataDispatcher {
    void dispatch(SourceNodeIdentifier sourceNodeIdentifier, RawData rawData);

}
