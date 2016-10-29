package com.networkedassets.condoc.transformer.common
import spock.lang.Specification

class SourceNodeIdentifierSpec extends Specification {
    def "should create unit identifier properly basing on constructor arguments"() {

        when:
        def sui = new SourceNodeIdentifier(1, 'BBB', 'CCC', 'DDD', 'EEE')

        then:
        sui.sourceIdentifier == 1
        sui.unitIdentifier == 'BBB'+sui.JOIN_TOKEN+'CCC'+sui.JOIN_TOKEN+'DDD'+sui.JOIN_TOKEN+'EEE'
    }

    def "returns last part of unit's identifier"() {
        when:
        def sui = new SourceNodeIdentifier(1, 'BBB', 'CCC', 'DDD', 'EEE')

        then:
        sui.getLastPartOfUnitIdentifier() == "EEE"
    }
}
