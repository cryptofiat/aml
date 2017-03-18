package com.cryptofiat.aml.sanctions

import spock.lang.Specification

import java.time.LocalDate

class UnSanctionsXmlParserTest extends Specification {
	UnSanctionsXmlParser sanctionsParser

	void setup() {
		sanctionsParser = new UnSanctionsXmlParser()
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

	def "can parse sanction entries and types"() {
		given:
			def filepath = getFilePath('un-sanctions-test.xml')
		when:
			sanctionsParser.parseDocument(filepath)
		then:
			sanctionsParser.entries.size() == 6

			sanctionsParser.entries[0].id == 6908434l
			sanctionsParser.entries[0].entityType == SanctionEntityType.PERSON
			sanctionsParser.entries[0].listSource == SanctionListSource.UN

			sanctionsParser.entries[1].id == 6908576l
			sanctionsParser.entries[1].entityType == SanctionEntityType.PERSON
			sanctionsParser.entries[1].listSource == SanctionListSource.UN

			sanctionsParser.entries[2].id == 6908443l
			sanctionsParser.entries[2].entityType == SanctionEntityType.PERSON
			sanctionsParser.entries[2].listSource == SanctionListSource.UN

			sanctionsParser.entries[3].id == 110403l
			sanctionsParser.entries[3].entityType == SanctionEntityType.ORGANIZATION
			sanctionsParser.entries[3].listSource == SanctionListSource.UN

			sanctionsParser.entries[4].id == 6908409l
			sanctionsParser.entries[4].entityType == SanctionEntityType.ORGANIZATION
			sanctionsParser.entries[4].listSource == SanctionListSource.UN

			sanctionsParser.entries[5].id == 113445l
			sanctionsParser.entries[5].entityType == SanctionEntityType.ORGANIZATION
			sanctionsParser.entries[5].listSource == SanctionListSource.UN
	}

	def "can parse sanction names"() {
		given:
			def filepath = getFilePath('un-sanctions-test.xml')
		when:
			sanctionsParser.parseDocument(filepath)
		then:
			sanctionsParser.entries[0].fullNames.size() == 6
			[
					"ABD AL-KHALIQ AL-HOUTHI",
					"Abd-al-Khaliq al-Huthi",
					"Abd-al-Khaliq Badr-al-Din al Huthi",
					"‘Abd al-Khaliq Badr al-Din al-Huthi",
					"Abd al-Khaliq al-Huthi",
					"Abu-Yunus"
			].every { name -> sanctionsParser.entries[0].fullNames.contains(name) }

			sanctionsParser.entries[1].fullNames.size() == 8
			[
					"IYAD NAZMI SALIH KHALIL ALI",
					"Ayyad Nazmi Salih Khalil",
					"Eyad Nazmi Saleh Khalil",
					"Iyad al-Toubasi",
					"Iyad al-Tubasi",
					"Abu al-Darda'",
					"Abu-Julaybib al-Urduni",
					"Abu-Julaybib"
			].every { name -> sanctionsParser.entries[1].fullNames.contains(name) }

			sanctionsParser.entries[3].fullNames.size() == 1
			sanctionsParser.entries[3].fullNames.first() == "7TH OF TIR"

			sanctionsParser.entries[4].fullNames.size() == 4
			[
					"ABDALLAH AZZAM BRIGADES (AAB)",
					"Abdullah Azzam Brigades",
					"Ziyad al-Jarrah Battalions of the Abdallah Azzam Brigades",
					"Yusuf al-'Uyayri Battalions of the Abdallah Azzam Brigades",
			].every { name -> sanctionsParser.entries[4].fullNames.contains(name) }
	}

	def "can parse dates and years of birth"() {
		given:
			def filepath = getFilePath('un-sanctions-test.xml')
		when:
			sanctionsParser.parseDocument(filepath)
		then:
			sanctionsParser.entries[0].datesOfBirth.isEmpty()
			sanctionsParser.entries[0].yearsOfBirth.size() == 1
			sanctionsParser.entries[0].yearsOfBirth.first() == 1984

			sanctionsParser.entries[1].datesOfBirth.isEmpty()
			sanctionsParser.entries[1].yearsOfBirth.size() == 1
			sanctionsParser.entries[1].yearsOfBirth.first() == 1974

			sanctionsParser.entries[2].yearsOfBirth.isEmpty()
			sanctionsParser.entries[2].datesOfBirth.size() == 1
			sanctionsParser.entries[2].datesOfBirth.first() == new LocalDate(1989,07,13)

			sanctionsParser.entries[3].datesOfBirth.isEmpty()
			sanctionsParser.entries[3].yearsOfBirth.isEmpty()

			sanctionsParser.entries[4].datesOfBirth.isEmpty()
			sanctionsParser.entries[4].yearsOfBirth.isEmpty()

			sanctionsParser.entries[5].datesOfBirth.isEmpty()
			sanctionsParser.entries[5].yearsOfBirth.isEmpty()
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
