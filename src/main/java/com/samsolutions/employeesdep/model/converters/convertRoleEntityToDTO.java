package com.samsolutions.employeesdep.model.converters;

import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.entities.Role;
import org.springframework.core.convert.converter.Converter;

public class convertRoleEntityToDTO implements Converter<Role, RoleDTO> {
    @Override
    public RoleDTO convert(Role source) {
        RoleDTO target = new RoleDTO();
        target.setId(source.getId());
        target.setRole(source.getRole());
        return target;
    }
}
