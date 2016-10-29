package com.networkedassets.condoc.transformer.managePermissions.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.SourceUnit;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Node of plugin structure tree representing a single unit of organisation higher than SourceUnit, like repository, project etc.
 */
public class SourceStructureNode implements Node {

    private SourceNodeIdentifier sourceNodeIdentifier;

    private Node parent;

    private List<Node> children = new ArrayList<>();

    public SourceStructureNode(@NotNull SourceNodeIdentifier sourceNodeIdentifier) {
        this.sourceNodeIdentifier = sourceNodeIdentifier;
    }

    @Override
    public Optional<Node> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    public SourceStructureNode setParent(Node parent) {
        throwIfSameNode(parent);
        throwIfNotInTheSameSource(parent);
        this.parent = parent;
        if (!parent.getChildren().contains(this)) {
            parent.addChildren(this);
        }
        return this;
    }

    public SourceStructureNode setChildren(List<Node> children) {
        this.children = children;
        return this;
    }

    @JsonIgnore
    public List<SourceUnit> getBelongingSourceUnits() {
        List<SourceUnit> units = new ArrayList<>();
        return getBelongingSourceUnits(units);
    }

    private List<SourceUnit> getBelongingSourceUnits(List<SourceUnit> units) {
        for (Node node : getChildren()) {
            if (node instanceof SourceUnit) {
                SourceUnit unit = (SourceUnit) node;
                units.add(unit);
            } else {
                SourceStructureNode structureNode = (SourceStructureNode) node;
                units.addAll(structureNode.getBelongingSourceUnits());
            }
        }
        return units;
    }

    @Override
    public boolean isLeaf() {
        return children.size() == 0;
    }

    @Override
    public boolean isRoot() {
        return !getParent().isPresent();
    }

    @Override
    public Node addChildren(Node child) {
        throwIfSameNode(child);
        throwIfNotInTheSameSource(child);
        if (!children.contains(child)) {
            children.add(child);
        }
        child.setParent(this);
        return this;
    }

    @Override
    public SourceNodeIdentifier getSourceNodeIdentifier() {
        return sourceNodeIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourceStructureNode that = (SourceStructureNode) o;
        return Objects.equals(sourceNodeIdentifier, that.sourceNodeIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceNodeIdentifier);
    }

    private void throwIfNotInTheSameSource(Node node) {
        if (!node.getSourceNodeIdentifier().getSourceIdentifier().equals(this.getSourceNodeIdentifier().getSourceIdentifier())) {
            throw new IllegalArgumentException("Nodes from different sources can't be related");
        }
    }

    private void throwIfSameNode(Node node) {
        if (this.equals(node)) {
            throw new IllegalArgumentException("Node can't be itself's parent or child");
        }
    }
}
