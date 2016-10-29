package com.networkedassets.condoc.transformer.manageSourceUnits

import com.networkedassets.condoc.transformer.common.PersistenceTestingAbility
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier
import com.networkedassets.condoc.transformer.common.SourceUnit
import com.networkedassets.condoc.transformer.manageSourceUnits.core.boundary.SourceUnitManager
import com.networkedassets.condoc.transformer.manageSourceUnits.infrastructure.db.jpa.JpaSourceUnitManager
import spock.lang.Specification

class SourceUnitManagerSpec extends Specification implements PersistenceTestingAbility {
    static SourceUnitManager sourceUnitManager

    def setupSpec() {
        sourceUnitManager = new JpaSourceUnitManager()
        sourceUnitManager.em = entityManager
    }

    def "persists SourceUnit and returns it by id and identifier"() {
        given:
        def identifier1 = new SourceNodeIdentifier(1, "BBB", "CCC")
        SourceUnit sourceUnit1 = new SourceUnit(identifier1)

        when:
        sourceUnitManager.persist(sourceUnit1)
        flushAndClearCache()
        def receivedSourceUnitByIdentifier = sourceUnitManager.get(identifier1)
        def receivedSourceUnit = sourceUnitManager.get(sourceUnit1.getId())

        then:
        receivedSourceUnitByIdentifier.isPresent()
        receivedSourceUnit.isPresent()
        receivedSourceUnit == receivedSourceUnitByIdentifier

        receivedSourceUnit.get().sourceNodeIdentifier == identifier1

    }

    def "returns all persisted SourceUnits"() {
        given:
        def sourceUnit1 = new SourceUnit(new SourceNodeIdentifier(1, "BBB", "CCC"))
        def sourceUnit2 = new SourceUnit(new SourceNodeIdentifier(1, "BBB", "DDD"))
        def sourceUnit3 = new SourceUnit(new SourceNodeIdentifier(1, "BBB", "EEE"))

        when:
        sourceUnitManager.persist(sourceUnit1)
        sourceUnitManager.persist(sourceUnit2)
        sourceUnitManager.persist(sourceUnit3)
        flushAndClearCache()
        List<SourceUnit> list = sourceUnitManager.getList()

        then:
        list.size() == 3
        list.contains(sourceUnit1)
        list.contains(sourceUnit2)
        list.contains(sourceUnit3)

    }

}
