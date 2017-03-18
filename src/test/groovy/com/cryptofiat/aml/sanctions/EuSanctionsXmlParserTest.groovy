package com.cryptofiat.aml.sanctions

import spock.lang.Specification

import java.time.LocalDate

class EuSanctionsXmlParserTest extends Specification {
	EuSanctionsXmlParser sanctionsParser

	void setup() {
		sanctionsParser = new EuSanctionsXmlParser()
	}

	def "new parser has empty sanctions list"() {
		when:
			def entries = sanctionsParser.getEntries()
		then:
			entries.size() == 0
	}

	def "parser does nothing if file doesn't exist"() {
		given:
			def filename = 'hello.xml'
		when:
			sanctionsParser.parseDocument(filename)
		then:
			sanctionsParser.entries.size() == 0
	}

	def "parse sanction entries and types"() {
		given:
			def filepath = getFilePath('eu-sanctions-test.xml')
		when:
			sanctionsParser.parseDocument(filepath)
		then:
			sanctionsParser.entries.size() == 2
			sanctionsParser.entries[0].entityId == 1l
			sanctionsParser.entries[0].type == EuSanctionType.PERSON
			sanctionsParser.entries[1].entityId == 13l
			sanctionsParser.entries[1].type == EuSanctionType.PERSON
	}

	def "parse sanction names"() {
		given:
			def filepath = getFilePath('eu-sanctions-test.xml')
		when:
			sanctionsParser.parseDocument(filepath)
		then:
			sanctionsParser.entries.size() == 2
			sanctionsParser.entries[0].fullNames.size() == 1
			sanctionsParser.entries[0].fullNames.first() == "Robert Gabriel Mugabe"
			sanctionsParser.entries[1].fullNames.size() == 3
			sanctionsParser.entries[1].fullNames.contains("Saddam Hussein Al-Tikriti")
			sanctionsParser.entries[1].fullNames.contains("Abu Ali")
			sanctionsParser.entries[1].fullNames.contains("Abou Ali")
	}

	def "parse sanction dates of birth"() {
		given:
			def filepath = getFilePath('eu-sanctions-test.xml')
		when:
			sanctionsParser.parseDocument(filepath)
		then:
			sanctionsParser.entries.size() == 2
			sanctionsParser.entries[0].datesOfBirth.size() == 1
			sanctionsParser.entries[0].datesOfBirth.first() == new LocalDate(1924,02,21)
			sanctionsParser.entries[1].datesOfBirth.size() == 1
			sanctionsParser.entries[1].datesOfBirth.first() == new LocalDate(1937,04,28)
	}

	def "parse sanction years of birth"() {
		given:
			def filepath = getFilePath('eu-sanctions-yob-test.xml')
		when:
			sanctionsParser.parseDocument(filepath)
		then:
			sanctionsParser.entries.size() == 2
			sanctionsParser.entries[0].yearsOfBirth.size() == 5
			sanctionsParser.entries[0].yearsOfBirth.contains(1924)
			sanctionsParser.entries[0].yearsOfBirth.contains(1961)
			sanctionsParser.entries[0].yearsOfBirth.contains(1965)
			sanctionsParser.entries[0].yearsOfBirth.contains(1966)
			sanctionsParser.entries[0].yearsOfBirth.contains(1967)

			sanctionsParser.entries[1].yearsOfBirth.size() == 6
			sanctionsParser.entries[1].yearsOfBirth.contains(1958)
			sanctionsParser.entries[1].yearsOfBirth.contains(1959)
			sanctionsParser.entries[1].yearsOfBirth.contains(1960)
			sanctionsParser.entries[1].yearsOfBirth.contains(1961)
			sanctionsParser.entries[1].yearsOfBirth.contains(1962)
			sanctionsParser.entries[1].yearsOfBirth.contains(1963)
	}

	def "can parse entire EU sanctions list"() {
		given:
			def filepath = getFilePath('eu-global-sanctions.xml')
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
