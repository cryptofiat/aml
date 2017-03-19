package com.cryptofiat.aml.sanctions;

import java.util.List;

public interface ISanctionsXmlParser {
    List<SanctionEntry> parseEntries(String filepath);
}
