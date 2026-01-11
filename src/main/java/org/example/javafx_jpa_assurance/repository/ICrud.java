package org.example.javafx_jpa_assurance.repository;

import java.util.List;

public interface ICrud<T> {

    public List<T> getAll();
    public void insert(T t);
    public void update(T t);
    public void delete(T t);
    public T getById(int id);
}
