package org.example.javafx_jpa_assurance.entity;

public class AssuranceHabitat extends  Assurance {

    private  String adresse;
    private double superficie;
    private boolean zoneRisque;

    public AssuranceHabitat(String nomClient, double montant, String adresse, double superficie, boolean zoneRisque) {
        super(nomClient, montant);
        this.adresse = adresse;
        this.superficie = superficie;
        this.zoneRisque = zoneRisque;
    }

    @Override
    public double calculerPrime() {
        return montant * (1 + superficie/1000) * (zoneRisque ? 1.3 : 1.0);
    }

    @Override
    public String getTypeName() {
        return "entity.Assurance Habitation";
    }

    @Override
    public String toString() {
        return "entity.AssuranceHabitat{" +
                "adresse='" + adresse + '\'' +
                ", superficie=" + superficie +
                ", zoneRisque=" + zoneRisque +
                ", numero='" + numero + '\'' +
                ", nomClient='" + nomClient + '\'' +
                ", montant=" + montant +
                '}';
    }
}