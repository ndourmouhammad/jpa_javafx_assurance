package org.example.javafx_jpa_assurance.repository.implement;

import org.example.javafx_jpa_assurance.entity.TypeAssurance;
import org.example.javafx_jpa_assurance.repository.ICrud;

import java.util.List;

public class TypeAssuranceRepository implements ICrud<TypeAssurance> {
    @Override
    public List<TypeAssurance> getAll() {
        return List.of();
    }

    @Override
    public void insert(TypeAssurance typeAssurance) {

    }

    @Override
    public void update(TypeAssurance typeAssurance) {

    }

    @Override
    public void delete(TypeAssurance typeAssurance) {

    }

    @Override
    public TypeAssurance getById(int id) {
        return null;
    }
}
