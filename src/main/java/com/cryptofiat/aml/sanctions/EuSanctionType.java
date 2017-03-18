package com.cryptofiat.aml.sanctions;

public enum EuSanctionType {
    PERSON,
    ORGANIZATION;

    public static EuSanctionType parseFromXml(String value) {
        if (value.equals("P")) {
            return PERSON;
        } else if (value.equals("E")) {
            return ORGANIZATION;
        }
        throw new RuntimeException("Unknown EU Sanction Type: " + value);
    }
}
