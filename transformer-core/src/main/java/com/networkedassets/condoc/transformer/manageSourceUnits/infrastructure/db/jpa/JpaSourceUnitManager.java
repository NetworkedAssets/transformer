package com.networkedassets.condoc.transformer.manageSourceUnits.infrastructure.db.jpa;

import com.networkedassets.condoc.transformer.common.PersistenceManager;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.SourceUnit;
import com.networkedassets.condoc.transformer.common.exceptions.SourceUnitNotFoundException;
import com.networkedassets.condoc.transformer.manageBundles.core.entity.Bundle;
import com.networkedassets.condoc.transformer.manageSourceUnits.core.boundary.SourceUnitManager;
import com.networkedassets.condoc.transformer.util.functional.Optionals;

import javax.ejb.Singleton;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
public class JpaSourceUnitManager extends PersistenceManager<SourceUnit> implements SourceUnitManager {


    @Override
    @Transactional
    public SourceUnit save(SourceUnit sourceUnit) {
        sourceUnit.getDocumentations().forEach(s -> s.setSourceUnit(sourceUnit));
        return merge(sourceUnit);
    }

    @Override
    public Optional<SourceUnit> get(Integer id) {
        return find(id);
    }

    @Override
    public Optional<SourceUnit> get(SourceNodeIdentifier sourceNodeIdentifier) {
        return Optionals.ofThrowingSpecificException(NoResultException.class, () ->
                em.createQuery("select su from SourceUnit su " +
                        "where su.sourceNodeIdentifier.sourceIdentifier = :sourceIdentifier " +
                        "and su.sourceNodeIdentifier.unitIdentifier = :unitIdentifier", SourceUnit.class)
                        .setParameter("sourceIdentifier", sourceNodeIdentifier.getSourceIdentifier())
                        .setParameter("unitIdentifier", sourceNodeIdentifier.getUnitIdentifier())
                        .getSingleResult());
    }

    @Override
    public List<SourceUnit> getList() {
        return em.createQuery("select su from SourceUnit su", SourceUnit.class).getResultList();
    }

    @Override
    public boolean isListened(SourceNodeIdentifier sourceNodeIdentifier) {

        return get(sourceNodeIdentifier).orElseThrow(SourceUnitNotFoundException::new)
                .getBundles()
                .stream()
                .filter(Bundle::isListened)
                .findAny().isPresent();
    }

    @Override
    protected Class<SourceUnit> getEntityClass() {
        return SourceUnit.class;
    }
}
