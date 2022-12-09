package com.samsolutions.employeesdep.model.services;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getEmployeesToDepartment(String departName);

    EmployeeDTO createEmployee(EmployeeDTO employeeToSaveDTO);

    void deleteEmployeeById(Long employeeId);
}
