package com.cryptofiat.aml.sanctions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
@Getter
@Setter
public class SanctionEntry {
    private Long sourceId;
    private SanctionSource source;
    private SanctionEntryType type;
    private Set<String> fullNames = new HashSet<>();
    private Set<Integer> yearsOfBirth = new HashSet<>();
    private Set<LocalDate> datesOfBirth = new HashSet<>();
}
