package com.networkedassets.condoc.transformer.managePermissions.core.entity;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;

import javax.validation.constraints.NotNull;

public class SourceStructureRootNode extends SourceStructureNode {

    public SourceStructureRootNode(@NotNull Integer sourceIdentifier) {
        super(new SourceNodeIdentifier(sourceIdentifier, "root"));
    }

    @Override
    public SourceStructureNode setParent(Node parent) {
        throw new UnsupportedOperationException("Root node could not have a parent");
    }

    @Override
    public boolean isRoot() {
        return true;
    }
}
