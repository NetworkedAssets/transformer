package com.networkedassets.condoc.transformer.retrieveDocumentation.infrastructure.db.jpa;

import com.networkedassets.condoc.transformer.common.docitem.DocItem;
import com.networkedassets.condoc.transformer.retrieveDocumentation.core.DocItemSearchResult;
import com.networkedassets.condoc.transformer.retrieveDocumentation.core.boundary.DocItemSearchService;
import com.networkedassets.condoc.transformer.util.functional.Optionals;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaDocItemSearchService implements DocItemSearchService {
    @PersistenceContext(unitName = "TransformerPostgresDS")
    protected EntityManager em;

    // TODO: Refactor this monstrosity
    /* TODO: Multiple selects are bad for performance, but can't do one with many joins because hibernate basically
             can't perform left joins, doing inner insteads - results are wrong. Maybe there is a way to fix it.
    */
    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Set<DocItemSearchResult> searchDocItemsInSourceUnit(int sourceUnitId, String phrase, int maxResults) {
        String normalizedPhrase = StringUtils.stripAccents(phrase).toLowerCase();
        return new LinkedHashSet<>(
                Optionals.ofThrowingSpecificException(EntityNotFoundException.class, () -> {
                    return em.createQuery(
                            "select di from SourceUnit su join su.documentations d join d.docItems di " +
                                    "where (su.id = :sourceUnitId) and " +
                                    "" +
                                    "(di.id in " +
                                    "(select odi.id from OmniDocItem odi where odi.inlined = false and " +
                                    "(lower(concat(" +
                                    "odi.fullName, odi.docText, odi.declarationInCode, odi.displayName," +
                                    "odi.itemType, odi.afterDisplayName, odi.afterDocText," +
                                    "odi.afterItemType, odi.afterMemberCategories, odi.afterMetadata," +
                                    "odi.afterModifiers, odi.afterRelations, odi.afterDocText, odi.beforeDisplayName," +
                                    "odi.beforeDocText, odi.beforeItemType, odi.beforeMemberCategories, odi.beforeMetadata," +
                                    "odi.beforeModifiers, odi.beforeRelations)) like :phrase)  ) " +
                                    "" +
                                    "or di.id in (select odi.id from OmniDocItem odi join odi.metadata m where odi.inlined = false and  lower(m) like :phrase) " +
                                    "or di.id in (select odi.id from OmniDocItem odi join odi.modifiers m where odi.inlined = false and lower(m) like :phrase)" +
                                    "or di.id in (select odi.id from OmniDocItem odi join odi.functionParameters f where odi.inlined = false and lower(f) like :phrase)" +
                                    "or di.id in (select odi.id from OmniDocItem odi join odi.relations r join r.items i where odi.inlined = false and lower(i) like :phrase)" +
                                    "or di.id in (select odi.id from OmniDocItem odi join odi.memberCategories c join c.items i where odi.inlined = false and lower(i.fullName) like :phrase)" +
                                    "" +
                                    "or di.id in (select hdi.id from HtmlDocItem hdi where " +
                                    "lower(concat(hdi.fullName, hdi.content, hdi.originalPath)) like :phrase ))"

                            , DocItem.class)
                            .setParameter("sourceUnitId", sourceUnitId)
                            .setParameter("phrase", "%" + normalizedPhrase + "%")
                            .setMaxResults(maxResults)
                            .getResultList()
                            .stream().map(di -> new DocItemSearchResult(di.getFullName(), di.getShortName(), di.getId(), di.getDocItemType())).collect(Collectors.toSet());
                })
                        .orElseGet(LinkedHashSet::new));
    }
}
