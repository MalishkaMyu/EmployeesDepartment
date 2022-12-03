package com.samsolutions.employeesdep.model.converters;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.entities.Employee;
import com.samsolutions.employeesdep.model.entities.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Set;

public class convertEmployeeDTOToEntity implements Converter<EmployeeDTO, Employee> {
    @Override
    public Employee convert(EmployeeDTO source) {
        Employee target = new Employee();
        BeanUtils.copyProperties(source,target);
        target.setDepartment(new convertDepartmentDTOToEntity().convert(source.getDepartment()));
        Set<Role> targetRoles = new HashSet<>();
        for (RoleDTO roleDTO:source.getEmployeeRoles())  {
            targetRoles.add(new convertRoleDTOToEntity().convert(roleDTO));
        }
        target.setEmployeeRoles(targetRoles);
        return target;
    }
}
