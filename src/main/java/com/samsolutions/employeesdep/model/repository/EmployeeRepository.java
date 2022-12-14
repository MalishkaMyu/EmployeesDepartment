package com.samsolutions.employeesdep.model.repository;

import com.samsolutions.employeesdep.model.entities.Department;
import com.samsolutions.employeesdep.model.entities.Employee;
import com.samsolutions.employeesdep.model.entities.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select e from Employee e where e.name = :name and e.surname = :surname")
    Employee findByName(@Param("name") String name, @Param("surname") String surname);

    @Query("select e from Employee e join e.department d where d.name = :department order by e.surname, e.name")
    List<Employee> findInDepartment(@Param("department") String department);

    @Query("select e from Employee e join e.department d where d.name = :department order by e.surname, e.name")
    List<Employee> findInDepartment(@Param("department") String department, Pageable paging);

    @Query("select count(r) from Employee e left join e.employeeRoles r where e.id = :emp_id")
    //@Query("select SIZE(e.employeeRoles) from Employee e where e.id = :emp_id")
    int countRolesOfEmployee(@Param("emp_id") Long id);
}
