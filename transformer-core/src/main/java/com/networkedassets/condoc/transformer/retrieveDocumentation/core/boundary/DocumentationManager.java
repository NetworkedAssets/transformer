package com.networkedassets.condoc.transformer.retrieveDocumentation.core.boundary;

import com.networkedassets.condoc.transformer.common.DocItemTreeNode;
import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.common.docitem.DocItem;

import java.util.List;
import java.util.Optional;

public interface DocumentationManager {
    <T extends DocItem> Optional<Documentation<T>> get(Integer sourceUnitId, String type);

    <T extends DocItem> Optional<Documentation<T>> get(Integer sourceUnitId, Class<T> clazz);

    List<DocItemTreeNode> getOmniDocumentationAsTopPackageTrees(Integer sourceUnitId);

    Optional<DocItem> getDocItemByName(Integer sourceUnitId, String type, String name);

    <T extends DocItem> Optional<T> getDocItemByName(Integer sourceUnitId, Class<T> type, String name);

    void persist(Documentation documentation);

    Documentation merge(Documentation set);

    DocItemTreeNode getOmniDocumentationAsSingleRootTree(Integer sourceUnitId);
}
