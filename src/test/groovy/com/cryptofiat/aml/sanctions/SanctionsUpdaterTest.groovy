package com.cryptofiat.aml.sanctions

import spock.lang.Ignore
import spock.lang.Specification

class SanctionsUpdaterTest extends Specification {
	SanctionsUpdater sanctionsUpdater

	void setup() {
		sanctionsUpdater = new SanctionsUpdater()
	}

	@Ignore("basically an integration test")
	def "can update sanctions"() {
		when:
			sanctionsUpdater.updateAllLists()
		then:
			true
	}
}
