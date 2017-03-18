package com.cryptofiat.aml.sanctions

import spock.lang.Specification

class UnSanctionsXmlParserTest extends Specification {
	UnSanctionsXmlParser sanctionsParser

	void setup() {
		sanctionsParser = new UnSanctionsXmlParser()
	}

	def "can parse entire EU sanctions list"() {
		given:
			def filepath = getFilePath('un-consolidated-sanctions.xml')
		when:
			sanctionsParser.parseDocument(filepath)
		then:
			sanctionsParser.entries.size() > 0
	}

	String getFilePath(String filename) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		return file.getAbsolutePath();
	}
}
