package org.example.javafx_jpa_assurance.repository.implement;

import jakarta.persistence.EntityManager;
import org.example.javafx_jpa_assurance.entity.Assurance;
import org.example.javafx_jpa_assurance.repository.ICrud;
import org.example.javafx_jpa_assurance.utils.JPAUtil;
import java.util.List;

public class AssuranceRepository implements ICrud<Assurance> {

    private EntityManager entityManager;

    public AssuranceRepository() {
        this.entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public List<Assurance> getAll() {
        // Read-only query does not require an explicit transaction
        return entityManager.createQuery("from Assurance", Assurance.class).getResultList();
    }

    @Override
    public void insert(Assurance assurance) {
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(assurance);
        this.entityManager.getTransaction().commit();
    }

    @Override
    public void update(Assurance assurance) {
    entityManager.getTransaction().begin();
    this.entityManager.merge(assurance);
        this.entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Assurance assurance) {
        this.entityManager.getTransaction().begin();
        this.entityManager.remove(assurance);
        this.entityManager.getTransaction().commit();
    }

    @Override
    public Assurance getById(int id) {
        return entityManager.find(Assurance.class, id);
    }


}