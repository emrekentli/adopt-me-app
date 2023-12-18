package com.emrekentli.adoptme.model.dto;

public enum Gender {
    MALE("Erkek"),
    FEMALE("Dişi");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
