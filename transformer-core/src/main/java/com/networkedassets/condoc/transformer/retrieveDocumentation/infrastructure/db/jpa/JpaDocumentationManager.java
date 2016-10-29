package com.networkedassets.condoc.transformer.retrieveDocumentation.infrastructure.db.jpa;

import com.networkedassets.condoc.transformer.common.DocItemTreeNode;
import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.common.PersistenceManager;
import com.networkedassets.condoc.transformer.common.docitem.DocItem;
import com.networkedassets.condoc.transformer.common.docitem.OmniDocItem;
import com.networkedassets.condoc.transformer.common.exceptions.DocumentationNotFoundException;
import com.networkedassets.condoc.transformer.retrieveDocumentation.core.OmniDocTreeifier;
import com.networkedassets.condoc.transformer.retrieveDocumentation.core.boundary.DocumentationManager;
import com.networkedassets.condoc.transformer.util.functional.Optionals;

import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JpaDocumentationManager extends PersistenceManager<Documentation> implements DocumentationManager {

    @Override
    protected Class<Documentation> getEntityClass() {
        return Documentation.class;
    }

    @Override
    public <T extends DocItem> Optional<Documentation<T>> get(Integer sourceUnitId, String type) {
        //noinspection unchecked
        return Optionals.ofThrowingSpecificException(NoResultException.class, () ->
                em.createQuery("select d from Documentation d " +
                        "where d.sourceUnit.id = :sourceUnitId " +
                        "and d.type = :type", Documentation.class)
                        .setParameter("sourceUnitId", sourceUnitId)
                        .setParameter("type", Documentation.DocumentationType.valueOf(type.toUpperCase()))
                        .getSingleResult());
    }

    @Override
    public <T extends DocItem> Optional<Documentation<T>> get(Integer sourceUnitId, Class<T> clazz) {
        //noinspection unchecked
        return Optionals.ofThrowingSpecificException(NoResultException.class, () ->
                em.createQuery("select d from Documentation d " +
                        "where d.sourceUnit.id = :sourceUnitId " +
                        "and d.type = :type", Documentation.class)
                        .setParameter("sourceUnitId", sourceUnitId)
                        .setParameter("type", Documentation.DocumentationType.of(clazz))
                        .getSingleResult());
    }

    @Override
    public Optional<DocItem> getDocItemByName(Integer sourceUnitId, String type, String name) {
        return Optionals.ofThrowingSpecificException(NoResultException.class, () ->
                em.createQuery("select di from Documentation d join d.docItems di " +
                        "where d.sourceUnit.id = :sourceUnitId and d.type =:type and di.fullName =:name", DocItem.class)
                        .setParameter("sourceUnitId", sourceUnitId)
                        .setParameter("type", Documentation.DocumentationType.valueOf(type.toUpperCase()))
                        .setParameter("name", name)
                        .getSingleResult());
    }

    @Override
    public <T extends DocItem> Optional<T> getDocItemByName(Integer sourceUnitId, Class<T> clazz, String name) {
        return Optionals.ofThrowingSpecificException(NoResultException.class, () ->
                clazz.cast(em.createQuery("select di from Documentation d join d.docItems di " +
                        "where d.sourceUnit.id = :sourceUnitId and d.type = :type and di.fullName = :name", DocItem.class)
                        .setParameter("sourceUnitId", sourceUnitId)
                        .setParameter("type", Documentation.DocumentationType.of(clazz))
                        .setParameter("name", name)
                        .getSingleResult()));
    }

    @Override
    public List<DocItemTreeNode> getOmniDocumentationAsTopPackageTrees(Integer sourceUnitId) {
        OmniDocTreeifier omniDocTreeifier =
                new OmniDocTreeifier(getDocItemsForGivenDocumentation(sourceUnitId, OmniDocItem.class));
        return omniDocTreeifier.treeify();
    }

    @Override
    public DocItemTreeNode getOmniDocumentationAsSingleRootTree(Integer sourceUnitId) {
        OmniDocTreeifier omniDocTreeifier =
                new OmniDocTreeifier(getDocItemsForGivenDocumentation(sourceUnitId, OmniDocItem.class));
        return omniDocTreeifier.treeifyWithSingleRoot();
    }

    private <T extends DocItem> Collection<T> getDocItemsForGivenDocumentation(Integer sourceUnitId, Class<T> clazz) {
        if (clazz.equals(OmniDocItem.class)) {
            //noinspection unchecked
            return (Collection<T>) getNotInlinedOmniDocItemsForGivenDocumentation(sourceUnitId);
        } else {
            return get(sourceUnitId, clazz)
                    .orElseThrow(DocumentationNotFoundException::new)
                    .getDocItems();
        }
    }

    private Collection<OmniDocItem> getNotInlinedOmniDocItemsForGivenDocumentation(Integer sourceUnitId) {
        return Optionals.ofThrowingSpecificException(
                NoResultException.class,
                () -> em.createQuery(
                        "select odi from Documentation d join d.docItems di, OmniDocItem odi " +
                                "where di = odi and " +
                                "d.sourceUnit.id = :sourceUnitId and " +
                                "odi.inlined = false",
                        OmniDocItem.class
                )
                        .setParameter("sourceUnitId", sourceUnitId)
                        .getResultList()
        ).orElseThrow(DocumentationNotFoundException::new);
    }
}
