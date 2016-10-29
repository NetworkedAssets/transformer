package com.networkedassets.condoc.transformer.manageBundles

import com.networkedassets.condoc.transformer.common.PersistenceTestingAbility
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import com.networkedassets.condoc.transformer.common.SourceUnit
import com.networkedassets.condoc.transformer.manageBundles.core.boundary.BundleManager
import com.networkedassets.condoc.transformer.manageBundles.core.entity.Bundle
import com.networkedassets.condoc.transformer.manageBundles.infrastructure.db.jpa.JpaBundleManager
import com.networkedassets.condoc.transformer.manageSourceUnits.core.boundary.SourceUnitManager
import com.networkedassets.condoc.transformer.manageSourceUnits.infrastructure.db.jpa.JpaSourceUnitManager
import spock.lang.Specification

//@Ignore
class BundleManagerSpec extends Specification implements PersistenceTestingAbility {
    static BundleManager bundleManager = new JpaBundleManager()
    static SourceUnitManager sourceUnitManager = new JpaSourceUnitManager()


    def setupSpec() {
        sourceUnitManager.em = entityManager
        bundleManager.em = entityManager
        bundleManager.sourceUnitManager = sourceUnitManager
    }

    def "persists bundles and returns them by ID"() {
        given:
        Bundle bundle1 = new Bundle().setName("AAA")
        Bundle bundle2 = new Bundle().setName("BBB")

        when:
        bundleManager.persist(bundle1)
        bundleManager.persist(bundle2)
        flushAndClearCache()

        then:
        bundleManager.getAllBundles().size() == 2
        bundleManager.getById(bundle1.getId()).isPresent()
        bundleManager.getById(bundle1.getId()).get().name == bundle1.name
        bundleManager.getById(bundle2.getId()).isPresent()
        bundleManager.getById(bundle2.getId()).get().name == bundle2.name
    }

    def "persists bundle with attached source units"() {
        given:
        Bundle bundle = new Bundle().setName("AAA")
        SourceUnit sourceUnit1 = new SourceUnit(new SourceNodeIdentifier(1, "BBBB"))
        SourceUnit sourceUnit2 = new SourceUnit(new SourceNodeIdentifier(2, "CCCC"))
        bundle.addSourceUnit(sourceUnit1).addSourceUnit(sourceUnit2)

        when:
        bundleManager.persist(bundle)
        flushAndClearCache()

        then:
        Optional<Bundle> retrievedBundle = bundleManager.getById(bundle.getId())
        retrievedBundle.isPresent()
        Collection<SourceUnit> retrievedUnits = retrievedBundle.get().getSourceUnits()
        retrievedUnits.size() == 2
        retrievedUnits.containsAll([sourceUnit1, sourceUnit2])
    }

    def "adds source units to existing bundle"() {
        given:
        Bundle bundle = new Bundle().setName("AAA")
        SourceUnit sourceUnit1 = new SourceUnit(new SourceNodeIdentifier(1, "BBBB"))
        SourceUnit sourceUnit2 = new SourceUnit(new SourceNodeIdentifier(2, "CCCC"))
        bundle.addSourceUnit(sourceUnit1)
        bundleManager.persist(bundle)
        flushAndClearCache()

        when:
        Bundle retrievedBundle = bundleManager.getById(bundle.getId()).get()
        retrievedBundle.addSourceUnit(sourceUnit2)
        bundleManager.save(bundle.getId(), retrievedBundle)
        flushAndClearCache()

        then:
        Collection<SourceUnit> retrievedSourceUnits = bundleManager.getById(bundle.getId()).get().sourceUnits
        retrievedSourceUnits.size() == 2
        retrievedSourceUnits.containsAll(sourceUnit1, sourceUnit2)
    }

    def "removes source units from an existing bundle"() {
        given:
        Bundle bundle = new Bundle().setName("AAA")
        SourceUnit sourceUnit1 = new SourceUnit(new SourceNodeIdentifier(1, "BBBB"))
        SourceUnit sourceUnit2 = new SourceUnit(new SourceNodeIdentifier(2, "CCCC"))
        bundle.addSourceUnit(sourceUnit1).addSourceUnit(sourceUnit2)
        bundleManager.persist(bundle)
        flushAndClearCache()

        when:
        Bundle retrievedBundle = bundleManager.getById(bundle.getId()).get()
        retrievedBundle.removeSourceUnit(sourceUnit2)
        bundleManager.save(bundle.getId(), retrievedBundle)
        flushAndClearCache()

        then:
        Collection<SourceUnit> retrievedSourceUnits = bundleManager.getById(bundle.getId()).get().sourceUnits
        retrievedSourceUnits.size() == 1
        retrievedSourceUnits.contains(sourceUnit1)
    }
}
