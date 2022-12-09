package com.samsolutions.employeesdep.model.converters;

import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.entities.Role;
import org.springframework.core.convert.converter.Converter;

public class RoleDTOToEntityConverter implements Converter<RoleDTO, Role>  {
    @Override
    public Role convert(RoleDTO source) {
        Role target = new Role();
        target.setId(source.getId());
        target.setRole(source.getRole());
        return target;
    }
}
