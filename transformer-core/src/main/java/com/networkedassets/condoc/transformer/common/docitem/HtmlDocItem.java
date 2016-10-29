package com.networkedassets.condoc.transformer.common.docitem;

import com.fasterxml.jackson.annotation.JsonView;
import com.networkedassets.condoc.transformer.common.Documentation;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class HtmlDocItem extends DocItem {
    @Column(columnDefinition = "TEXT")
    private String originalPath = "";

    @JsonView(Views.Content.class)
    @Column(columnDefinition = "TEXT")
    private String content = "";

    public String getOriginalPath() {
        return originalPath;
    }

    public HtmlDocItem setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
        return this;
    }

    public String getContent() {
        return content;
    }

    public HtmlDocItem setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public Documentation.DocumentationType getDocItemType() {
        return Documentation.DocumentationType.of(HtmlDocItem.class);
    }

    @Override
    public String getShortName() {
        return fullName;
    }

    public interface Views {
        interface Content {

        }
    }
}
