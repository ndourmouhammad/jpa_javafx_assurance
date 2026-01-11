package org.example.javafx_jpa_assurance.entity;

public  class AssuranceAuto extends Assurance {

    private String immat;
    private int puissance;
    private int bonusMalus;

    public AssuranceAuto(String nomClient, double montant, String immat, int puissance, int bonusMalus) {
        super(nomClient, montant);
        this.immat = immat;
        this.puissance = puissance;
        this.bonusMalus = bonusMalus;
    }

    @Override
    public double calculerPrime() {
        return montant * (1 + puissance *0.05) * (bonusMalus/100);
    }

    @Override
    public String getTypeAssurance() {
        return "entity.Assurance auto";
    }

    @Override
    public String toString() {
        return "entity.AssuranceAuto{" +
                "immat='" + immat + '\'' +
                ", puissance=" + puissance +
                ", bonusMalus=" + bonusMalus +
                ", numero='" + numero + '\'' +
                ", nomClient='" + nomClient + '\'' +
                ", montant=" + montant +
                '}';
    }
}