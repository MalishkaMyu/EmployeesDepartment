package com.samsolutions.employeesdep.model.services;

import com.samsolutions.employeesdep.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userToSaveDTO);

    UserDTO updateUser(UserDTO userToSaveDTO);

    UserDTO getUserByLogin(String userLogin);

    UserDTO getUserById(Long userId);

    boolean deleteUserById(Long userId);

    List<UserDTO> getAllUsers();

    List<UserDTO> getAllUsers(int page);
}
