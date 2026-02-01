package org.example.javafx_jpa_assurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_assurance")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TypeAssurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "typeAssurance", cascade = CascadeType.ALL, orphanRemoval = false)
    @Exclude
    private List<Assurance> assurances = new ArrayList<>();
}
