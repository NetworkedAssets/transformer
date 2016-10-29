package com.networkedassets.condoc.transformer.produceDocumentation.core.boundary;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;

public interface DocumentationProducer {
    void produceDocumentationForSourceUnit(SourceNodeIdentifier sourceNodeIdentifier);
    void produceDocumentationForBundle(int bundleId);
}
