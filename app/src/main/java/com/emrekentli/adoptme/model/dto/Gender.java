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

    public static Gender getFromValue(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Geçersiz cinsiyet değeri: " + value);
    }
}
