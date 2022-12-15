package com.samsolutions.employeesdep.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import com.samsolutions.employeesdep.model.entities.Role;

@Repository
@Slf4j
public class JpaRoleDao implements Dao<Role> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Role> find(Long id) {
        return Optional.ofNullable(entityManager.find(Role.class, id));
    }

    @Override
    public List<Role> findAll() {
        Query query = entityManager.createQuery("SELECT r FROM Role r", Role.class);
        return castRolesList(Role.class, query.getResultList());
    }

    @Override
    @Transactional
    public Long save(Role role) {
        entityManager.persist(role);
        Role savedRole = entityManager.find(Role.class, role.getId());
        if (savedRole == null)
            return null;
        else
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
        if (find(id).isPresent()) {
            Role role = find(id).get();
            entityManager.remove(role);
        }
    }

    @Override
    @Transactional
    public int deleteAll() {
        Query query = entityManager.createQuery("DELETE from Role r");
        return query.executeUpdate();
    }

    public static <Role> List<Role> castRolesList(Class<? extends Role> clazz, Collection<?> roles) {
        List<Role> result = new ArrayList<>(roles.size());
        for (Object role : roles) {
            try {
                result.add(clazz.cast(role));
            } catch (ClassCastException e) {
                log.error("ClassCastException: " + e.getMessage());
            }
        }
        return result;
    }
}
