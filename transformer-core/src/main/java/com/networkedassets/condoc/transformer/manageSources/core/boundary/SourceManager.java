package com.networkedassets.condoc.transformer.manageSources.core.boundary;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;

import java.util.List;
import java.util.Optional;

public interface SourceManager {
    void verifyAndPersist(Source source);

    Source verifyAndSave(Integer id, Source source);

    void remove(Source source);

    void removeByIdentifier(Integer sourceIdentifier);

    void removeBySourceNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier);

    Optional<Source> getByIdentifier(Integer sourceIdentifier);

    Source getByIdentifierOrThrow(Integer sourceIdentifier);

    Source getWithSettingsByIdentifierOrThrow(Integer sourceIdentifier);

    Optional<Source> getBySourceNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier);

    SourceStructureRootNode getStructureFromSource(Source source);

    List<Source> getAllSources();

    Optional<Source> getWithSettingsByIdentifier(Integer sourceIdentifier);

    Optional<Source> getByUrl(String sourceUrl);
}