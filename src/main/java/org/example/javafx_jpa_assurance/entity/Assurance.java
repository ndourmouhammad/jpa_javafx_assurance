package org.example.javafx_jpa_assurance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "assurances")
public class Assurance {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name = "number")
    protected String numero;

    protected String nomClient;
    protected double montant;


    private static  int compteur = 0;

    public Assurance(String nomClient, double montant) {
        this.nomClient = nomClient;
        this.montant = montant;
        this.numero = generateNumero();
    }

    public Assurance(int id, String numero, String nomClient, double montant) {
        this.id = id;
        this.numero = numero;
        this.nomClient = nomClient;
        this.montant = montant;
    }

    public Assurance() {

    }

    private String generateNumero(){
        return String.format("ASS%04d", compteur++);
    }

    public  double calculerCoutTotal(int nbAnnees){
        return nbAnnees * calculerPrime();
    }

    public int getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public static int getCompteur() {
        return compteur;
    }

    public static void setCompteur(int compteur) {
        Assurance.compteur = compteur;
    }

    public  double calculerPrime(){
        return 0;
    }
    public  String getTypeAssurance(){
        return "Assurance";
    }

    public static int getNombreContrats(){
        return compteur;
    }



    @Override
    public String toString() {
        return "entity.Assurance{" +
                "numero='" + numero + '\'' +
                ", nomClient='" + nomClient + '\'' +
                ", montant=" + montant +
                '}';
    }
}