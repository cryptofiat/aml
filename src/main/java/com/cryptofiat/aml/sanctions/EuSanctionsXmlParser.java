package com.cryptofiat.aml.sanctions;

import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class EuSanctionsXmlParser extends DefaultHandler {

    @Getter
    private List<EuSanctionEntry> entries;

    private EuSanctionEntry entry;
    private String tmpValue;
    private DateTimeFormatter dateOfBirthFormatter;

    public EuSanctionsXmlParser() {
        clearEntries();
        dateOfBirthFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    private void clearEntries() {
        this.entries = new ArrayList<EuSanctionEntry>();
    }

    public void parseDocument(String fileUri) {
        clearEntries();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(fileUri, this);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
        } catch (IOException e) {
            System.out.println("IO error");
        }
    }

    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
        if (elementName.equalsIgnoreCase("ENTITY")) {
            entry = new EuSanctionEntry();
            entry.setEntityId(Long.parseLong(attributes.getValue("Id")));
            entry.setType(EuSanctionType.parseFromXml(attributes.getValue("Type")));
        }
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equals("ENTITY")) {
            entries.add(entry);
        }

        if (element.equalsIgnoreCase("WHOLENAME")) {
            entry.fullNames.add(tmpValue);
        }

        if (element.equals("DATE")) {
            if (tmpValue.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
                entry.datesOfBirth.add((LocalDate.parse(tmpValue, dateOfBirthFormatter)));
            } else if (tmpValue.matches("([0-9]{4})")) {
                entry.yearsOfBirth.add(Integer.valueOf(tmpValue));
            } else if (tmpValue.matches("([0-9]{4}) - ([0-9]{4}) \\(approximative\\)")
                    || tmpValue.matches("([0-9]{4}) - ([0-9]{4})")) {
                Integer start = Integer.valueOf(tmpValue.substring(0, 4));
                Integer end = Integer.valueOf(tmpValue.substring(7, 11));
                IntStream.rangeClosed(start, end).forEach(year -> entry.yearsOfBirth.add(year));
            } else if (Pattern.compile("^([0-9]{4}).*").matcher(tmpValue).find()) {
                entry.yearsOfBirth.add(Integer.valueOf(tmpValue.substring(0,4)));
            } else if (Pattern.compile("^([0-9]{1,2})-([0-9]{4})").matcher(tmpValue).find()) {
                entry.yearsOfBirth.add(Integer.valueOf(tmpValue.split("-")[1]));
            } else if (tmpValue.replaceAll("\\s+","").equals("")) {
                //Do Nothing
            } else {
                System.out.println("empty: " + tmpValue.isEmpty());
                System.out.println("null: " + (tmpValue == null));
                System.out.println("empty string: " + (tmpValue.equals("")));
                throw new RuntimeException("Unknown date format: " + tmpValue);
            }
        }
    }

    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}
