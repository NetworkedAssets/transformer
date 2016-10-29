package com.networkedassets.condoc.transformer.manageSourceUnits.core.boundary;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.SourceUnit;

import java.util.List;
import java.util.Optional;

public interface SourceUnitManager {
    void persist(SourceUnit sourceUnit);

    SourceUnit save(SourceUnit sourceUnit);

    Optional<SourceUnit> get(Integer id);

    Optional<SourceUnit> get(SourceNodeIdentifier sourceNodeIdentifier);

    List<SourceUnit> getList();

    boolean isListened(SourceNodeIdentifier sourceNodeIdentifier);
}