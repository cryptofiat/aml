package com.cryptofiat.aml.sanctions;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnSanctionsXmlParser extends DefaultHandler implements ISanctionsXmlParser {

    private List<SanctionEntry> entries;

    private String tmpValue;
    private SanctionEntry entry;
    private String compoundName;
    private boolean isDateOfBirthTag = false;
    private DateTimeFormatter dateOfBirthFormatter;

    private List<String> nameComponentTags;

    public UnSanctionsXmlParser() {
        clearEntries();
        dateOfBirthFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        nameComponentTags = Arrays.asList("FIRST_NAME", "SECOND_NAME", "THIRD_NAME", "FOURTH_NAME", "FIFTH_NAME");
    }

    @Override
    public List<SanctionEntry> parseEntries(String filepath) {
        clearEntries();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(filepath, this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<SanctionEntry> results = entries;
        clearEntries();
        return results;
    }

    private void clearEntries() {
        this.entries = new ArrayList<>();
    }

    @Override
    public void startElement(String s, String s1, String element, Attributes attributes) throws SAXException {
        if (element.equals("INDIVIDUAL")) {
            entry = (new SanctionEntry())
                    .setListSource(SanctionListSource.UN)
                    .setEntityType(SanctionEntityType.PERSON);
            compoundName = "";
        }

        if (element.equals("ENTITY")) {
            entry = (new SanctionEntry())
                    .setListSource(SanctionListSource.UN)
                    .setEntityType(SanctionEntityType.ORGANIZATION);
            compoundName = "";
        }

        if (element.equals("INDIVIDUAL_DATE_OF_BIRTH")) {
            isDateOfBirthTag = true;
        }
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equals("INDIVIDUAL") || element.equals("ENTITY")) {
            entry.getFullNames().add(compoundName.trim());
            entries.add(entry);
        }

        if (element.equals("DATAID")) {
            entry.setId(Long.parseLong(tmpValue));
        }

        if (nameComponentTags.contains(element)) {
            compoundName += tmpValue.trim() + " ";
        }

        if (element.equals("ALIAS_NAME")) {
            entry.getFullNames().add(tmpValue.trim());
        }

        if (element.equals("INDIVIDUAL_DATE_OF_BIRTH")) {
            isDateOfBirthTag = false;
        }

        if (isDateOfBirthTag && element.equals("YEAR")) {
            entry.getYearsOfBirth().add(Integer.parseInt(tmpValue));
        }

        if (isDateOfBirthTag && element.equals("DATE")) {
            if (tmpValue.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})-([0-9]{2}):([0-9]{2})")) {
                entry.getYearsOfBirth().add(Integer.valueOf(tmpValue.substring(0,4)));
            } else {
                entry.getDatesOfBirth().add((LocalDate.parse(tmpValue, dateOfBirthFormatter)));
            }
        }
    }

    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}
