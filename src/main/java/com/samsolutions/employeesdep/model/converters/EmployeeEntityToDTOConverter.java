package com.samsolutions.employeesdep.model.converters;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.entities.Employee;
import com.samsolutions.employeesdep.model.entities.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class EmployeeEntityToDTOConverter implements Converter<Employee, EmployeeDTO> {
    @Override
    public EmployeeDTO convert(Employee source) {
        EmployeeDTO target = new EmployeeDTO();
        BeanUtils.copyProperties(source, target);
        target.setWorkExperience(ChronoUnit.YEARS.between(target.getEmploymentDate(), LocalDate.now()));
        target.setDepartment(new DepartmentEntityToDTOConverter().convert(source.getDepartment()));
        target.setUser(new UserEntityToDTOConverter().convert(source.getUser()));
        Set<RoleDTO> targetRoles = new HashSet<>();
        RoleEntityToDTOConverter roleEntityToDTOConverter = new RoleEntityToDTOConverter();
        for (Role role : source.getEmployeeRoles()) {
            targetRoles.add(roleEntityToDTOConverter.convert(role));
        }
        target.setEmployeeRoles(targetRoles);
        return target;
    }
}
