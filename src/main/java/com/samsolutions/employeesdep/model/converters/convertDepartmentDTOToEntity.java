package com.samsolutions.employeesdep.model.converters;

import com.samsolutions.employeesdep.model.dto.DepartmentDTO;
import com.samsolutions.employeesdep.model.entities.Department;
import org.springframework.core.convert.converter.Converter;

public class convertDepartmentDTOToEntity implements Converter<DepartmentDTO,Department> {

    @Override
    public Department convert(DepartmentDTO source) {
        Department target = new Department();
        target.setId(source.getId());
        target.setName(source.getName());
        return target;
    }
}
