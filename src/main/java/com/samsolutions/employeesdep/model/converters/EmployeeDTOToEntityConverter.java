package com.samsolutions.employeesdep.model.converters;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.entities.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

public class EmployeeDTOToEntityConverter implements Converter<EmployeeDTO, Employee> {
    @Override
    public Employee convert(EmployeeDTO source) {
        Employee target = new Employee();
        BeanUtils.copyProperties(source,target);
        return target;
    }
}
