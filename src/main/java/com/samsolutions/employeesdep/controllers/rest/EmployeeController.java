package com.samsolutions.employeesdep.controllers.rest;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> read() {
        final List<EmployeeDTO> employees = employeeService.getAllEmployees();

        return employees != null && !employees.isEmpty()
                ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/page={page}")
    public ResponseEntity<List<EmployeeDTO>> read(@PathVariable(name = "page") int page) {
        final List<EmployeeDTO> employees = employeeService.getAllEmployees(page);

        return employees != null && !employees.isEmpty()
                ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/department={depart}")
    public ResponseEntity<List<EmployeeDTO>> read(@PathVariable(name = "depart") String depart) {
        final List<EmployeeDTO> employees = employeeService.getEmployeesToDepartment(depart);

        return employees != null && !employees.isEmpty()
                ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/department={depart}&page={page}")
    public ResponseEntity<List<EmployeeDTO>> read(
            @PathVariable(name = "depart") String depart, @PathVariable(name = "page") int page) {
        final List<EmployeeDTO> employees = employeeService.getEmployeesToDepartment(depart, page);

        return employees != null && !employees.isEmpty()
                ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeDTO> read(@PathVariable(name = "id") Long id) {
        final EmployeeDTO employee = employeeService.getEmployeeById(id);

        return employee != null
                ? new ResponseEntity<>(employee, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
