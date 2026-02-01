package org.example.javafx_jpa_assurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

import javax.validation.constraints.Max;

@Entity
@Table(name = "assurances")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Assurance {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name = "number")
    protected String numero;

    protected String nomClient;

    @Max(value = 100)
    protected double montant;


    private static  int compteur = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_assurance_id")
    @Exclude
    private TypeAssurance typeAssurance;

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

    private static String generateNumero(){
        return String.format("ASS%04d", compteur++);
    }

    public  double calculerCoutTotal(int nbAnnees){
        return nbAnnees * calculerPrime();
    }

    public  double calculerPrime(){
        return 0;
    }
    public  String getTypeName(){
        return "Assurance";
    }

    public static int getNombreContrats(){
        return compteur;
    }

}