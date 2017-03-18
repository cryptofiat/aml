package com.cryptofiat.aml.sanctions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EuSanctionEntry {
    Long entityId;
    EuSanctionType type;
    Set<String> fullNames = new HashSet<String>();
    Set<LocalDate> datesOfBirth = new HashSet<LocalDate>();
    Set<Integer> yearsOfBirth = new HashSet<Integer>();
}
