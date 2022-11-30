package com.samsolutions.employeesdep.model.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.samsolutions.employeesdep.model.entities.Role;

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
        entityManager.persist(role);
        Role savedRole = entityManager.find(Role.class, role.getId());
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
