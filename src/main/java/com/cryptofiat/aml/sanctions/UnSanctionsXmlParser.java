package com.cryptofiat.aml.sanctions;

import lombok.Getter;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class UnSanctionsXmlParser extends DefaultHandler {

    @Getter
    private List<UnSanctionEntry> entries;

    public void parseDocument(String filepath) {

    }
}
