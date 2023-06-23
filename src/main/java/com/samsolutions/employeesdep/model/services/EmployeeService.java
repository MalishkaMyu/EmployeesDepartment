package com.samsolutions.employeesdep.model.services;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getEmployeesToDepartment(String departName);

    List<EmployeeDTO> getEmployeesToDepartment(String departName, int page);

    EmployeeDTO getEmployeeById(Long employeeId);

    List<EmployeeDTO> getAllEmployees();

    List<EmployeeDTO> getAllEmployees(int page);

    EmployeeDTO createEmployee(EmployeeDTO employeeToSaveDTO, Boolean modifyKeycloakUser);

    EmployeeDTO updateEmployee(EmployeeDTO employeeToSaveDTO, Boolean modifyKeycloakUser);

    boolean deleteEmployeeById(Long employeeId, Boolean modifyKeycloakUser);
}
