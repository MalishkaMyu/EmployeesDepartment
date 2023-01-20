package com.samsolutions.employeesdep.model.services;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getEmployeesToDepartment(String departName);

    List<EmployeeDTO> getEmployeesToDepartment(String departName, int page);

    EmployeeDTO getEmployeeById(Long employeeId);

    List<EmployeeDTO> getAllEmployees();

    List<EmployeeDTO> getAllEmployees(int page);

    EmployeeDTO createEmployee(EmployeeDTO employeeToSaveDTO);

    EmployeeDTO updateEmployee(EmployeeDTO employeeToSaveDTO);

    boolean deleteEmployeeById(Long employeeId);
}
