package com.samsolutions.employeesdep.model.services.impl;

import com.samsolutions.employeesdep.config.MyPasswordEncoder;
import com.samsolutions.employeesdep.exception.EntityDuplicateException;
import com.samsolutions.employeesdep.exception.EntityNotFoundException;
import com.samsolutions.employeesdep.model.converters.UserDTOToEntityConverter;
import com.samsolutions.employeesdep.model.converters.UserEntityToDTOConverter;
import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.repository.UserRepository;
import com.samsolutions.employeesdep.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${employees.per.page}")
    private int usersPerPage;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyPasswordEncoder encoder;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userToSaveDTO) {
        User userToSave = new UserDTOToEntityConverter().convert(userToSaveDTO);
        if (userToSave == null)
            return null;

        if (userToSave.getId() != null && userRepository.existsById(userToSave.getId())) {
            // user with ID is already exists
            throw new EntityDuplicateException(
                    "The User with ID " + userToSave.getId() + " already exists and can not be created.",
                    User.class);
        }
        if (userRepository.existsByLogin(userToSave.getLogin())) {
            // login is already exists
            throw new EntityDuplicateException(
                    "The login " + userToSave.getLogin() + " is already registered. Please choose another login.",
                    User.class);
        }
        if (userRepository.existsByEmail(userToSave.getEmail())) {
            // email is already exists
            throw new EntityDuplicateException(
                    "The email " + userToSave.getEmail() + " is already registered. Please choose another email.",
                    User.class);
        }
        if (!userToSaveDTO.getPassword().isBlank())
            userToSave.setPasswordHash(encoder.encode(userToSaveDTO.getPassword()));
        userRepository.save(userToSave);
        UserDTO savedUserDTO = new UserEntityToDTOConverter().convert(userToSave);
        if (savedUserDTO != null)
            savedUserDTO.setPassword(userToSaveDTO.getPassword());
        return savedUserDTO;
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userToSaveDTO) {
        User userToSave = new UserDTOToEntityConverter().convert(userToSaveDTO);
        if (userToSave == null)
            return null;

        // check where the user exists
        if (userToSaveDTO.getId() == null || !userRepository.existsById(userToSave.getId())) {
            // user is not still exists
            throw new EntityNotFoundException("There is no user with ID " + userToSave.getId() + ".",
                    User.class);
        }
        // reading current user information from database
        User existingUser = userRepository.getReferenceById(userToSave.getId());
        // check whether new login already has the another user
        if (userRepository.existsByLogin(userToSave.getLogin()) &&
                !userToSave.getLogin().equals(existingUser.getLogin())) {
            // user with the new login is already exists
            throw new EntityDuplicateException(
                    "The login " + userToSave.getLogin() + " is already registered for another user. " +
                            "Please choose another login.", User.class);
        }
        // check whether new email already has the another user
        if (userRepository.existsByEmail(userToSave.getEmail()) &&
                !userToSave.getEmail().equals(existingUser.getEmail())) {
            // user with the new email is already exists
            throw new EntityDuplicateException(
                    "The email " + userToSave.getEmail() + " is already registered for another user. " +
                            "Please choose another email.", User.class);
        }

        // encoding password
        userToSave.setPasswordHash(encoder.encode(userToSaveDTO.getPassword()));
        // saving user
        userRepository.save(userToSave);
        UserDTO savedUserDTO = new UserEntityToDTOConverter().convert(userToSave);
        if (savedUserDTO != null) {
            savedUserDTO.setPassword(userToSaveDTO.getPassword());
        }
        return savedUserDTO;
    }

    @Override
    public UserDTO getUserByLogin(String userLogin) {
        if (userRepository.findByLogin(userLogin).isPresent()) {
            User readUser = userRepository.findByLogin(userLogin).orElseThrow(EntityNotFoundException::new);
            return new UserEntityToDTOConverter().convert(readUser);
        } else
            throw new EntityNotFoundException("There is no User with login " + userLogin,
                    User.class);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            User readUser = userRepository.findById(userId).get();
            return new UserEntityToDTOConverter().convert(readUser);
        } else {
            throw new EntityNotFoundException("There is no User with ID " + userId,
                    User.class);
        }
    }

    @Override
    @Transactional
    public boolean deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return !userRepository.existsById(userId);
        } else {
            throw new EntityNotFoundException("There is no User with ID " + userId + " to delete.",
                    User.class);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        Pageable paging = PageRequest.of(0, usersPerPage);
        UserEntityToDTOConverter userEntityToDTOConverter = new UserEntityToDTOConverter();
        return userRepository.findAll(paging).stream() //stream from list
                .map(userEntityToDTOConverter::convert) //convert every element
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllUsers(int page) {
        Pageable paging = PageRequest.of(page, usersPerPage);
        UserEntityToDTOConverter userEntityToDTOConverter = new UserEntityToDTOConverter();
        return userRepository.findAll(paging).stream() //stream from list
                .map(userEntityToDTOConverter::convert) //convert every element
                .collect(Collectors.toList());
    }
}
