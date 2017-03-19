package com.cryptofiat.aml.sanctions

import spock.lang.Specification

import java.time.LocalDate

class EuSanctionsXmlParserTest extends Specification {

	EuSanctionsXmlParser sanctionsParser

	void setup() {
		sanctionsParser = new EuSanctionsXmlParser()
	}

	def "parser throws exception if file not found"() {
		given:
			def filename = 'hello.xml'
		when:
			sanctionsParser.parseEntries(filename)
		then:
			thrown(RuntimeException)
	}

	def "parse sanction entries and types"() {
		given:
			def filepath = getFilePath('eu-sanctions-test.xml')
		when:
			def entries = sanctionsParser.parseEntries(filepath)
		then:
			entries.size() == 2
			entries[0].id == 1l
			entries[0].entityType == SanctionEntityType.PERSON
			entries[0].listSource == SanctionListSource.EU

			entries[1].id == 13l
			entries[1].entityType == SanctionEntityType.PERSON
			entries[1].listSource == SanctionListSource.EU
	}

	def "parse sanction names"() {
		given:
			def filepath = getFilePath('eu-sanctions-test.xml')
		when:
			def entries = sanctionsParser.parseEntries(filepath)
		then:
			entries.size() == 2
			entries[0].fullNames.size() == 1
			entries[0].fullNames.first() == "Robert Gabriel Mugabe"

			entries[1].fullNames.size() == 3
			entries[1].fullNames.contains("Saddam Hussein Al-Tikriti")
			entries[1].fullNames.contains("Abu Ali")
			entries[1].fullNames.contains("Abou Ali")
	}

	def "parse sanction dates of birth"() {
		given:
			def filepath = getFilePath('eu-sanctions-test.xml')
		when:
			def entries = sanctionsParser.parseEntries(filepath)
		then:
			entries.size() == 2
			entries[0].datesOfBirth.size() == 1
			entries[0].datesOfBirth.first() == new LocalDate(1924,02,21)

			entries[1].datesOfBirth.size() == 1
			entries[1].datesOfBirth.first() == new LocalDate(1937,04,28)
	}

	def "parse sanction years of birth"() {
		given:
			def filepath = getFilePath('eu-sanctions-yob-test.xml')
		when:
			def entries = sanctionsParser.parseEntries(filepath)
		then:
			entries.size() == 2
			entries[0].yearsOfBirth.size() == 5
			entries[0].yearsOfBirth.contains(1924)
			entries[0].yearsOfBirth.contains(1961)
			entries[0].yearsOfBirth.contains(1965)
			entries[0].yearsOfBirth.contains(1966)
			entries[0].yearsOfBirth.contains(1967)

			entries[1].yearsOfBirth.size() == 6
			entries[1].yearsOfBirth.contains(1958)
			entries[1].yearsOfBirth.contains(1959)
			entries[1].yearsOfBirth.contains(1960)
			entries[1].yearsOfBirth.contains(1961)
			entries[1].yearsOfBirth.contains(1962)
			entries[1].yearsOfBirth.contains(1963)
	}

	def "can parse entire EU sanctions list"() {
		given:
			def filepath = getFilePath('eu-global-sanctions.xml')
		when:
			def entries = sanctionsParser.parseEntries(filepath)
		then:
			entries.size() > 0
	}

	String getFilePath(String filename) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		return file.getAbsolutePath();
	}
}
