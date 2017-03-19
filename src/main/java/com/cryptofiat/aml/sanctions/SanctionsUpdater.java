package com.cryptofiat.aml.sanctions;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.List;

public class SanctionsUpdater {

    private static final String EU_SANCTIONS_URL = "http://ec.europa.eu/external_relations/cfsp/sanctions/list/version4/global/global.xml";
    private static final String UN_SANCTIONS_URL = "https://scsanctions.un.org/resources/xml/en/consolidated.xml";

    private final String tmpDir;
    private ISanctionsXmlParser euSanctionsParser;
    private ISanctionsXmlParser unSanctionsParser;

    public SanctionsUpdater() {
        tmpDir = System.getProperty("java.io.tmpdir");
        euSanctionsParser = new EuSanctionsXmlParser();
        unSanctionsParser = new UnSanctionsXmlParser();
    }

    public void updateAllLists() {
        updateEuList();
        updateUnList();
    }

    private void updateEuList() {
        List<SanctionEntry> entries = downloadEuSanctionsEntries();
        System.out.println("Parsed " + entries.size() + " EU sanction entries");
    }

    private void updateUnList() {
        List<SanctionEntry> entries = downloadUnSanctionsEntries();
        System.out.println("Parsed " + entries.size() + " UN sanction entries");
    }

    private List<SanctionEntry> downloadEuSanctionsEntries() {
        return downloadEntriesFromXmlFile(euSanctionsParser, EU_SANCTIONS_URL, "eu-sanctions");
    }

    private List<SanctionEntry> downloadUnSanctionsEntries() {
        return downloadEntriesFromXmlFile(unSanctionsParser, UN_SANCTIONS_URL, "un-sanctions");
    }

    private List<SanctionEntry> downloadEntriesFromXmlFile(ISanctionsXmlParser sanctionsParser, String sanctionsFileUrl, String temporaryFilename) {
        try {
            URL url = new URL(sanctionsFileUrl);
            File tempFile = File.createTempFile(temporaryFilename, "xml", new File(tmpDir));
            FileUtils.copyURLToFile(url, tempFile);
            return sanctionsParser.parseEntries(tempFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
