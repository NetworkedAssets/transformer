package com.networkedassets.condoc.transformer.retrieveDocumentation.core;

import com.networkedassets.condoc.transformer.common.Documentation;

public class DocItemSearchResult {

    private String fullName = "";
    private String shortName = "";
    private Integer id=0;
    private Documentation.DocumentationType documentationType;

    public DocItemSearchResult(String fullName, String shortName, Integer id, Documentation.DocumentationType documentationType) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.documentationType = documentationType;
        this.id=id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public Integer getId() {
        return id;
    }

    public Documentation.DocumentationType getDocumentationType() {
        return documentationType;
    }
}
