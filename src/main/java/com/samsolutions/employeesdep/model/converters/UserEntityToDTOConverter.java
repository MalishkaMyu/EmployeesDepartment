package com.samsolutions.employeesdep.model.converters;

import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

public class UserEntityToDTOConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User source) {
        UserDTO target = new UserDTO();
        BeanUtils.copyProperties(source,target);
        target.setPassword("");
        return target;
    }
}
