package com.samsolutions.employeesdep.model.services.impl;

import com.samsolutions.employeesdep.model.converters.EmployeeDTOToEntityConverter;
import com.samsolutions.employeesdep.model.converters.EmployeeEntityToDTOConverter;
import com.samsolutions.employeesdep.model.converters.RoleDTOToEntityConverter;
import com.samsolutions.employeesdep.model.dao.JpaRoleDao;
import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.entities.Department;
import com.samsolutions.employeesdep.model.entities.Employee;
import com.samsolutions.employeesdep.model.entities.Role;
import com.samsolutions.employeesdep.model.repository.DepartmentRepository;
import com.samsolutions.employeesdep.model.repository.EmployeeRepository;
import com.samsolutions.employeesdep.model.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository empRepository;

    @Autowired
    private DepartmentRepository departRepository;

    @Autowired
    private JpaRoleDao roleRepository;

    @Override
    @Transactional
    public List<EmployeeDTO> getEmployeesToDepartment(String departName) {
        EmployeeEntityToDTOConverter employeeEntityToDTOConverter = new EmployeeEntityToDTOConverter();
        if (departName.equals(""))
            return empRepository.findAll().stream() //stream from list
                    .map(employeeEntityToDTOConverter::convert) //convert every element
                    .collect(Collectors.toList());
        else
            return empRepository.findInDepartment(departName).stream() //stream from list
                    .map(employeeEntityToDTOConverter::convert) //convert every element
                    .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeToSaveDTO) {
        Employee employeeToSave = new EmployeeDTOToEntityConverter().convert(employeeToSaveDTO);

        Department departToSave;
        Long departID = employeeToSaveDTO.getDepartment().getId();
        // saving department if new
        if (departID == null) {
            //departToSave = new DepartmentDTOToEntityConverter().convert(employeeToSaveDTO.getDepartment());
            departToSave = new Department(employeeToSaveDTO.getDepartment().getName());
            departRepository.save(departToSave);
        } else {
            departToSave = departRepository.getReferenceById(departID);
        }
        if (employeeToSave != null)
            employeeToSave.setDepartment(departToSave);
        else
            return null;

        // saving roles if new
        RoleDTOToEntityConverter roleDTOToEntityConverter = new RoleDTOToEntityConverter();
        Set<Role> rolesToSave = new HashSet<>();
        for (RoleDTO roleDTO : employeeToSaveDTO.getEmployeeRoles()) {
            Role roleToSave = roleDTOToEntityConverter.convert(roleDTO);
            if (roleToSave != null) {
                if (roleToSave.getId() == null || roleToSave.getId() == 0) {
                    roleRepository.save(roleToSave);
                }
                rolesToSave.add(roleToSave);
            }
        }
        employeeToSave.setEmployeeRoles(rolesToSave);
        // saving employee
        empRepository.save(employeeToSave);
        // convert employee entity back to DTO to have all IDs
        return new EmployeeEntityToDTOConverter().convert(employeeToSave);
    }

    @Override
    @Transactional
    public void deleteEmployeeById(Long employeeId) {
        if (empRepository.existsById(employeeId))
            empRepository.deleteById(employeeId); // how to know whether delete was successful???
    }
}
