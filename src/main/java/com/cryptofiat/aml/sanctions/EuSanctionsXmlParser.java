package com.cryptofiat.aml.sanctions;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class EuSanctionsXmlParser extends DefaultHandler implements ISanctionsXmlParser {

    private List<SanctionEntry> entries;

    private SanctionEntry entry;
    private String tmpValue;
    private DateTimeFormatter dateOfBirthFormatter;

    public EuSanctionsXmlParser() {
        clearEntries();
        dateOfBirthFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    private void clearEntries() {
        this.entries = new ArrayList<>();
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

    @Override
    public void startElement(String s, String s1, String element, Attributes attributes) throws SAXException {
        if (element.equalsIgnoreCase("ENTITY")) {
            entry = (new SanctionEntry())
                    .setSource(SanctionSource.EU)
                    .setSourceId(Long.parseLong(attributes.getValue("Id")))
                    .setType(parseSanctionEntityType(attributes.getValue("Type")));
        }
    }

    private static SanctionEntryType parseSanctionEntityType(String type) {
        if (type.equals("P")) {
            return SanctionEntryType.PERSON;
        } else if (type.equals("E")) {
            return SanctionEntryType.ORGANIZATION;
        }
        throw new RuntimeException("Unknown EU Sanction Type: " + type);
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equals("ENTITY")) {
            entries.add(entry);
        }

        if (element.equalsIgnoreCase("WHOLENAME")) {
            entry.getFullNames().add(tmpValue);
        }

        if (element.equals("DATE")) {
            parseDate();
        }
    }

    private void parseDate() {
        if (tmpValue.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
            entry.getDatesOfBirth().add((LocalDate.parse(tmpValue, dateOfBirthFormatter)));
        } else if (tmpValue.matches("([0-9]{4})")) {
            entry.getYearsOfBirth().add(Integer.valueOf(tmpValue));
        } else if (tmpValue.matches("([0-9]{4}) - ([0-9]{4}) \\(approximative\\)")
                || tmpValue.matches("([0-9]{4}) - ([0-9]{4})")) {
            Integer start = Integer.valueOf(tmpValue.substring(0, 4));
            Integer end = Integer.valueOf(tmpValue.substring(7, 11));
            IntStream.rangeClosed(start, end).forEach(year -> entry.getYearsOfBirth().add(year));
        } else if (Pattern.compile("^([0-9]{4}).*").matcher(tmpValue).find()) {
            entry.getYearsOfBirth().add(Integer.valueOf(tmpValue.substring(0,4)));
        } else if (Pattern.compile("^([0-9]{1,2})-([0-9]{4})").matcher(tmpValue).find()) {
            entry.getYearsOfBirth().add(Integer.valueOf(tmpValue.split("-")[1]));
        } else if (tmpValue.replaceAll("\\s+","").equals("")) {
            //Do Nothing
        } else {
            System.out.println("empty: " + tmpValue.isEmpty());
            System.out.println("null: " + (tmpValue == null));
            System.out.println("empty string: " + (tmpValue.equals("")));
            throw new RuntimeException("Unknown date format: " + tmpValue);
        }
    }

    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}
