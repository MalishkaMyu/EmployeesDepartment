package com.samsolutions.employeesdep.model.repository;

import com.samsolutions.employeesdep.model.entities.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByNameAndSurname(String name, String surname);

    @Query("select e from Employee e join e.department d where d.name = :department order by e.surname, e.name")
    List<Employee> findInDepartment(@Param("department") String department);

    @Query("select e from Employee e join e.department d where d.name = :department order by e.surname, e.name")
    List<Employee> findInDepartment(@Param("department") String department, Pageable paging);

    @Query("select count(r) from Employee e left join e.employeeRoles r where e.id = :emp_id")
    int countRolesOfEmployee(@Param("emp_id") Long id);

    boolean existsByNameAndSurname(String name, String surname);

    @Query("select e from Employee e join e.user u where u.login = :login")
    Optional<Employee> findByUserLogin(@Param("login") String login);
}
