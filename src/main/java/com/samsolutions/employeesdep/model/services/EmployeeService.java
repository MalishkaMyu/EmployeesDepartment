package com.samsolutions.employeesdep.model.services;

import com.samsolutions.employeesdep.model.daos.JpaRoleDao;
import com.samsolutions.employeesdep.model.entities.Department;
import com.samsolutions.employeesdep.model.entities.Employee;
import com.samsolutions.employeesdep.model.entities.Role;
import com.samsolutions.employeesdep.model.repository.DepartmentRepository;
import com.samsolutions.employeesdep.model.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository empRepository;

    @Autowired
    private DepartmentRepository departRepository;

    @Autowired
    private JpaRoleDao roleRepository;

    public List<Employee> getEmployeesToDepartment(String departName) {
        if (departName.equals(""))
            return empRepository.findAll();
        else
            return empRepository.findInDepartment(departName);
    }

    public Employee saveEmployee(Employee employeeToSave) {
        Department departToSave = employeeToSave.getDepartment();
        departRepository.save(departToSave);
        for (Role role:employeeToSave.getEmployeeRoles()) {
            roleRepository.save(role);
        }
        return empRepository.save(employeeToSave);
    }
}
