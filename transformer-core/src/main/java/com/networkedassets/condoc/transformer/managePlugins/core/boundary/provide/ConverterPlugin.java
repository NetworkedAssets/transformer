package com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide;

import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;

public interface ConverterPlugin extends Plugin {
    Documentation convert(RawData rawData);
}