package org.example.javafx_jpa_assurance.repository;

import org.example.javafx_jpa_assurance.entity.Assurance;

import java.util.List;

public interface IAssurance {


    public void insert(Assurance assurance);

    public Assurance findById(int id);

    public List<Assurance> findAll();

    public void update(Assurance assurance);

    public void delete(int id);

}