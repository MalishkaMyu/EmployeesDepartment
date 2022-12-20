package com.samsolutions.employeesdep.model.repository;

import com.samsolutions.employeesdep.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
