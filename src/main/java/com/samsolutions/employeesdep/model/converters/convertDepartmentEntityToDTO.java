package com.samsolutions.employeesdep.model.converters;

import com.samsolutions.employeesdep.model.dto.DepartmentDTO;
import com.samsolutions.employeesdep.model.entities.Department;

import org.springframework.core.convert.converter.Converter;

public class convertDepartmentEntityToDTO implements Converter<Department, DepartmentDTO> {

    @Override
    public DepartmentDTO convert(Department source) {
        DepartmentDTO target = new DepartmentDTO();
        target.setId(source.getId());
        target.setName(source.getName());
        return target;
    }
}
