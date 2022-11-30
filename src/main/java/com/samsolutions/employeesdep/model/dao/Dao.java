package com.samsolutions.employeesdep.model.dao;

import java.util.Optional;
import java.util.List;

public interface Dao<T> {

    Optional<T> find(Long id);

    List<T> findAll();

    Long save(T t);

    void update(T t);

    void delete(T t);

    void deleteById(Long id);
}