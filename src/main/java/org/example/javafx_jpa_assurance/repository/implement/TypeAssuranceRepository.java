package org.example.javafx_jpa_assurance.repository.implement;

import jakarta.persistence.EntityManager;
import org.example.javafx_jpa_assurance.entity.TypeAssurance;
import org.example.javafx_jpa_assurance.repository.ICrud;
import org.example.javafx_jpa_assurance.utils.JPAUtil;

import java.util.List;

public class TypeAssuranceRepository implements ICrud<TypeAssurance> {

    private final EntityManager entityManager;

    public TypeAssuranceRepository() {
        this.entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public List<TypeAssurance> getAll() {
        return entityManager.createQuery("from TypeAssurance", TypeAssurance.class).getResultList();
    }

    @Override
    public void insert(TypeAssurance typeAssurance) {
        entityManager.getTransaction().begin();
        entityManager.persist(typeAssurance);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(TypeAssurance typeAssurance) {
        entityManager.getTransaction().begin();
        entityManager.merge(typeAssurance);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(TypeAssurance typeAssurance) {
        entityManager.getTransaction().begin();
        entityManager.remove(typeAssurance);
        entityManager.getTransaction().commit();
    }

    @Override
    public TypeAssurance getById(int id) {
        return entityManager.find(TypeAssurance.class, id);
    }
}
