package com.networkedassets.condoc.transformer.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.networkedassets.condoc.transformer.common.docitem.OmniDocItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocItemTreeNode {
    private String name;
    private String fullName;
    private String type;
    private List<DocItemTreeNode> children = new ArrayList<>();
    @JsonIgnore
    private OmniDocItem originalDocItem;

    public DocItemTreeNode(String fullName, String name, String type) {
        this.fullName = fullName;
        this.name = name;
        this.type = type;
    }

    public DocItemTreeNode(OmniDocItem item) {
        this.setFullName(item.getFullName())
                .setName(item.getShortName())
                .setType(item.getItemType())
                .setOriginalDocItem(item);
    }

    public String getName() {
        return name;
    }

    public DocItemTreeNode setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public DocItemTreeNode setType(String type) {
        this.type = type;
        return this;
    }

    public List<DocItemTreeNode> getChildren() {
        return children;
    }

    public DocItemTreeNode setChildren(List<DocItemTreeNode> children) {
        this.children = children;
        return this;
    }

    public void addChild(DocItemTreeNode child) {
        this.children.add(child);
    }

    public String getFullName() {
        return fullName;
    }

    public DocItemTreeNode setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public OmniDocItem getOriginalDocItem() {
        return originalDocItem;
    }

    public DocItemTreeNode setOriginalDocItem(OmniDocItem originalDocItem) {
        this.originalDocItem = originalDocItem;
        return this;
    }

    public Integer getOriginalId() {
        if (originalDocItem == null) return null;
        return originalDocItem.getId();
    }


    public Optional<DocItemTreeNode> getChildByName(String name) {
        return children.stream().filter(c -> c.getName().equals(name)).findAny();
    }

    public DocItemTreeNode addChild(String fullName, String name, String type) {
        DocItemTreeNode child = new DocItemTreeNode(fullName, name, type);
        children.add(child);
        return child;
    }
}
