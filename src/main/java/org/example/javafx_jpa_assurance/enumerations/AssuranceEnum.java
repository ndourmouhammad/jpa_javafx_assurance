package org.example.javafx_jpa_assurance.enumerations;

public enum AssuranceEnum {
    HABITAT("Assurance  Habitation"),
    AUTO("ASSURANCE AUTOMOBILE");

    public final String label;

    AssuranceEnum (String label) {
        this.label = label;
    }

}
