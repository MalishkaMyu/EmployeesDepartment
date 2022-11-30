package com.samsolutions.employeesdep.model.dao;

import com.samsolutions.employeesdep.model.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
public class JpaRoleDao implements Dao<Role>{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Role> find(Long id) {
        return Optional.ofNullable(entityManager.find(Role.class, id));
    }

    @Override
    public List<Role> findAll() {
        Query query = entityManager.createQuery("SELECT r FROM Role r", Role.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Long save(Role role) {
        Role savedRole = entityManager.merge(role);
        return savedRole.getId();
    }

    @Override
    @Transactional
    public void update(Role role) {
        entityManager.merge(role);
    }

    @Override
    @Transactional
    public void delete(Role role) {
        entityManager.remove(role);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (find( id ).isPresent()) {
            Role role = find(id).get();
            entityManager.remove(role);
        }
    }
}
