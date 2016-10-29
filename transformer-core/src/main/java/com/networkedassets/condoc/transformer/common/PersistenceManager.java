package com.networkedassets.condoc.transformer.common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

public abstract class PersistenceManager<T> {
    private Class<T> clazz;

    @PersistenceContext(unitName = "TransformerPostgresDS")
    protected EntityManager em;

    @Transactional
    public T merge(T entity) {
        return em.merge(entity);
    }

    @Transactional
    public void persist(T entity) {
        em.persist(entity);
    }

    public Optional<T> find(Object id) {
        return Optional.ofNullable(em.find(getEntityClass(), id));
    }

    @Transactional
    public void remove(T entity) {
        em.remove(entity);
    }

    abstract protected Class<T> getEntityClass();
}
