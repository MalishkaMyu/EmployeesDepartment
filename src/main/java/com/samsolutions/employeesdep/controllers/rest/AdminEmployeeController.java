package com.samsolutions.employeesdep.controllers.rest;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/employees")
public class AdminEmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeDTO employee) {
                                              //RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        /*String JwtToken = StringUtils.substring(token, "Bearer".length());
        DecodedJWT jwt = JWT.decode(JwtToken);
        String keycloakID = jwt.getSubject();*/

        final EmployeeDTO createdEmp = employeeService.createEmployee(employee);
        return createdEmp != null
                ? new ResponseEntity<>(createdEmp, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable(name = "id") Long id, @RequestBody EmployeeDTO employee) {
        employee.setId(id);
        final EmployeeDTO updatedEmp = employeeService.updateEmployee(employee);
        return updatedEmp != null
                ? new ResponseEntity<>(updatedEmp, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        final boolean deleted = employeeService.deleteEmployeeById(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
