package org.example.javafx_jpa_assurance.enumerations;

public enum AssuranceEnum {
    Habitat("Assurance  Habitation"),
    Auto("ASSURANCE AUTOMOBILE");

    public final String label;

    AssuranceEnum (String label) {
        this.label = label;
    }

}
