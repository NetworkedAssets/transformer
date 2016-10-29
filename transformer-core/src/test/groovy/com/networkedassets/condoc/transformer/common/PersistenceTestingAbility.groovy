package com.networkedassets.condoc.transformer.common

import org.junit.After
import org.junit.Before

import javax.persistence.EntityManager
import javax.persistence.Persistence

trait PersistenceTestingAbility {
    static EntityManager entityManager = Persistence.createEntityManagerFactory("h2").createEntityManager()

    def flushAndClearCache() {
        entityManager.flush();
        entityManager.clear();
    }

    @Before
    def beginTransaction() {
        entityManager.getTransaction().begin()
    }

    @After
    def rollbackTransaction() {
        entityManager.getTransaction().rollback()
    }

}
