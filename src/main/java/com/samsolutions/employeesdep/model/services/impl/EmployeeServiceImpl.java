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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Value("${employees.per.page}")
    private int employeesPerPage;

    @Autowired
    private EmployeeRepository empRepository;

    @Autowired
    private DepartmentRepository departRepository;

    @Autowired
    private JpaRoleDao roleRepository;

    public List<EmployeeDTO> getEmployeesToDepartmentCommon(String departName, int page, int pageSize) {
        Pageable paging = PageRequest.of(page, pageSize);
        EmployeeEntityToDTOConverter employeeEntityToDTOConverter = new EmployeeEntityToDTOConverter();
        if (StringUtils.isBlank(departName)) {
            return null;
        } else {
            return empRepository.findInDepartment(departName, paging).stream() //stream from list
                    .map(employeeEntityToDTOConverter::convert) //convert every element
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getEmployeesToDepartment(String departName) {
        return getEmployeesToDepartmentCommon(departName, 0, employeesPerPage);
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getEmployeesToDepartment(String departName, int page) {
        return getEmployeesToDepartmentCommon(departName, page, employeesPerPage);
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAllEmployees() {
        Pageable paging = PageRequest.of(0, employeesPerPage);
        EmployeeEntityToDTOConverter employeeEntityToDTOConverter = new EmployeeEntityToDTOConverter();
        return empRepository.findAll(paging).stream() //stream from list
                .map(employeeEntityToDTOConverter::convert) //convert every element
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAllEmployees(int page) {
        Pageable paging = PageRequest.of(page, employeesPerPage);
        EmployeeEntityToDTOConverter employeeEntityToDTOConverter = new EmployeeEntityToDTOConverter();
        return empRepository.findAll(paging).stream() //stream from list
                .map(employeeEntityToDTOConverter::convert) //convert every element
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeToSaveDTO) {
        Employee employeeToSave = new EmployeeDTOToEntityConverter().convert(employeeToSaveDTO);
        if (employeeToSave == null)
            return null;

        // saving department if new
        Department departToSave;
        if (employeeToSaveDTO.getDepartment().getId() == null) {
            departToSave = new Department(employeeToSaveDTO.getDepartment().getName());
            departRepository.save(departToSave);
        } else {
            departToSave = departRepository.getReferenceById(employeeToSaveDTO.getDepartment().getId());
        }
        employeeToSave.setDepartment(departToSave);

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
