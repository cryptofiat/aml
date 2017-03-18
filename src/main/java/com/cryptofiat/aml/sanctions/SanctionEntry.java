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
    Long id;
    SanctionListSource listSource;
    SanctionEntityType entityType;
    Set<String> fullNames = new HashSet<>();
    Set<LocalDate> datesOfBirth = new HashSet<>();
    Set<Integer> yearsOfBirth = new HashSet<>();
}
