package com.networkedassets.condoc.transformer.retrieveDocumentation.core.boundary;

import com.networkedassets.condoc.transformer.retrieveDocumentation.core.DocItemSearchResult;

import java.util.Set;

public interface DocItemSearchService {
    Set<DocItemSearchResult> searchDocItemsInSourceUnit(int sourceUnitId, String phrase, int maxResults);
}
