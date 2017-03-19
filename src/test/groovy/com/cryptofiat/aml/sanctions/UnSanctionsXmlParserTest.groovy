package com.cryptofiat.aml.sanctions

import spock.lang.Specification

import java.time.LocalDate

class UnSanctionsXmlParserTest extends Specification {
	UnSanctionsXmlParser sanctionsParser

	void setup() {
		sanctionsParser = new UnSanctionsXmlParser()
	}

	def "parser throws exception if file doesn't exist"() {
		given:
			def filename = 'hello.xml'
		when:
			sanctionsParser.parseEntries(filename)
		then:
			thrown(RuntimeException)
	}

	def "can parse sanction entries and types"() {
		given:
			def filepath = getFilePath('un-sanctions-test.xml')
		when:
			def entries = sanctionsParser.parseEntries(filepath)
		then:
			entries.size() == 6

			entries[0].sourceId == 6908434l
			entries[0].type == SanctionEntryType.PERSON
			entries[0].source == SanctionSource.UN

			entries[1].sourceId == 6908576l
			entries[1].type == SanctionEntryType.PERSON
			entries[1].source == SanctionSource.UN

			entries[2].sourceId == 6908443l
			entries[2].type == SanctionEntryType.PERSON
			entries[2].source == SanctionSource.UN

			entries[3].sourceId == 110403l
			entries[3].type == SanctionEntryType.ORGANIZATION
			entries[3].source == SanctionSource.UN

			entries[4].sourceId == 6908409l
			entries[4].type == SanctionEntryType.ORGANIZATION
			entries[4].source == SanctionSource.UN

			entries[5].sourceId == 113445l
			entries[5].type == SanctionEntryType.ORGANIZATION
			entries[5].source == SanctionSource.UN
	}

	def "can parse sanction names"() {
		given:
			def filepath = getFilePath('un-sanctions-test.xml')
		when:
			def entries = sanctionsParser.parseEntries(filepath)
		then:
			entries[0].fullNames.size() == 6
			[
					"ABD AL-KHALIQ AL-HOUTHI",
					"Abd-al-Khaliq al-Huthi",
					"Abd-al-Khaliq Badr-al-Din al Huthi",
					"‘Abd al-Khaliq Badr al-Din al-Huthi",
					"Abd al-Khaliq al-Huthi",
					"Abu-Yunus"
			].every { name -> entries[0].fullNames.contains(name) }

			entries[1].fullNames.size() == 8
			[
					"IYAD NAZMI SALIH KHALIL ALI",
					"Ayyad Nazmi Salih Khalil",
					"Eyad Nazmi Saleh Khalil",
					"Iyad al-Toubasi",
					"Iyad al-Tubasi",
					"Abu al-Darda'",
					"Abu-Julaybib al-Urduni",
					"Abu-Julaybib"
			].every { name -> entries[1].fullNames.contains(name) }

			entries[3].fullNames.size() == 1
			entries[3].fullNames.first() == "7TH OF TIR"

			entries[4].fullNames.size() == 4
			[
					"ABDALLAH AZZAM BRIGADES (AAB)",
					"Abdullah Azzam Brigades",
					"Ziyad al-Jarrah Battalions of the Abdallah Azzam Brigades",
					"Yusuf al-'Uyayri Battalions of the Abdallah Azzam Brigades",
			].every { name -> entries[4].fullNames.contains(name) }
	}

	def "can parse dates and years of birth"() {
		given:
			def filepath = getFilePath('un-sanctions-test.xml')
		when:
			def entries = sanctionsParser.parseEntries(filepath)
		then:
			entries[0].datesOfBirth.isEmpty()
			entries[0].yearsOfBirth.size() == 1
			entries[0].yearsOfBirth.first() == 1984

			entries[1].datesOfBirth.isEmpty()
			entries[1].yearsOfBirth.size() == 1
			entries[1].yearsOfBirth.first() == 1974

			entries[2].yearsOfBirth.isEmpty()
			entries[2].datesOfBirth.size() == 1
			entries[2].datesOfBirth.first() == new LocalDate(1989,07,13)

			entries[3].datesOfBirth.isEmpty()
			entries[3].yearsOfBirth.isEmpty()

			entries[4].datesOfBirth.isEmpty()
			entries[4].yearsOfBirth.isEmpty()

			entries[5].datesOfBirth.isEmpty()
			entries[5].yearsOfBirth.isEmpty()
	}

	def "can parse entire EU sanctions list"() {
		given:
			def filepath = getFilePath('un-consolidated-sanctions.xml')
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
