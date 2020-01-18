package com.infoshareacademy.dao;

import com.infoshareacademy.model.Address;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Long save(Address c) {
        entityManager.persist(c);
        return c.getId();
    }

    public Address update(Address c) {
        return entityManager.merge(c);
    }

    public void delete(Long id) {
        final Address c = entityManager.find(Address.class, id);
        if (c != null) {
            entityManager.remove(c);
        }
    }

    public Address findById(Long id) {
        return entityManager.find(Address.class, id);
    }

    public List<Address> findAll() {
        final Query query = entityManager.createQuery("SELECT c FROM Address c");

        return query.getResultList();
    }

}
