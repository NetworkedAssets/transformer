package com.networkedassets.condoc.transformer.managePermissions.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;

import java.util.List;
import java.util.Optional;

public interface Node {
    @JsonView(Views.Tree.class)
    Node setParent(Node parent);

    @JsonIgnore
    Optional<Node> getParent();


    @JsonView(Views.Tree.class)
    List<Node> getChildren();

    @JsonView(Views.Tree.class)
    boolean isLeaf();

    @JsonView(Views.Tree.class)
    boolean isRoot();

    @JsonView(Views.Tree.class)
    Node addChildren(Node children);

    SourceNodeIdentifier getSourceNodeIdentifier();

    @JsonView(Views.Tree.class)
    default String getNodeName() {
        return getSourceNodeIdentifier().getLastPartOfUnitIdentifier();
    }

    interface Views {
        interface Tree {

        }

        interface Full extends Tree {

        }
    }
}
