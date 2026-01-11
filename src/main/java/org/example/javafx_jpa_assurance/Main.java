package org.example.javafx_jpa_assurance;

import org.example.javafx_jpa_assurance.entity.Assurance;
import org.example.javafx_jpa_assurance.repository.implement.AssuranceRepository;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        AssuranceRepository assuranceRepository = new AssuranceRepository();
//        for (int i = 0; i < 10; i++) {
//            assuranceRepository.insert(new Assurance("AMADOU DIOP", 1000000));
//        }

        // Suppression par entité (le repo attend une entité, pas un id)
        Assurance toDelete = assuranceRepository.getById(2);
        if (toDelete != null) {
            assuranceRepository.delete(toDelete);
        }

        // Avoid NullPointerException by not assuming a specific id exists
//        Assurance anyAssurance = assuranceRepository.getAll().stream().findFirst().orElse(null);
//        if (anyAssurance != null) {
//            System.out.println(anyAssurance);
//        } else {
//            System.out.println("No assurance records found.");
//        }
//
//        assuranceRepository.getAll().forEach(System.out::println);

        // Safely update an existing record if available
        Assurance assurance =  assuranceRepository.getAll().stream().findFirst().orElse(null);
        if (assurance != null) {
            assurance.setMontant(500000);
            assurance.setNomClient("AISSA POUYE");
            assuranceRepository.update(assurance);
        }
    }
}