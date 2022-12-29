package com.samsolutions.employeesdep.model.converters;

import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

public class UserDTOToEntityConverter implements Converter<UserDTO, User> {
    @Override
    public User convert(UserDTO source) {
        User target = new User();
        BeanUtils.copyProperties(source,target);
        return target;
    }
}
