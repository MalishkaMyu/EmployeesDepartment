package com.samsolutions.employeesdep.model.repository;

import com.samsolutions.employeesdep.model.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select d from Department d where d.name=:name")
    Department findByName(@Param("name") String name);
}
