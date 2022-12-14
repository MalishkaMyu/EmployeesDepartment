package com.samsolutions.employeesdep.model.services;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    int EMPLOYEES_ON_PAGE = 5;

    List<EmployeeDTO> getEmployeesToDepartment(String departName);
    List<EmployeeDTO> getEmployeesToDepartment(String departName, int page);
    List<EmployeeDTO> getEmployeesToDepartment(String departName, int page, int pageSize);

    EmployeeDTO createEmployee(EmployeeDTO employeeToSaveDTO);

    void deleteEmployeeById(Long employeeId);
}
