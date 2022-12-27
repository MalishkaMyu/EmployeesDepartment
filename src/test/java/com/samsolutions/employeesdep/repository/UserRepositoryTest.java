package com.samsolutions.employeesdep.repository;

import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    private final List<Long> listIDs = new ArrayList<>();

    @BeforeEach
    public void setup() {
        User userToSave, savedUser;
        String passwordHash;
        userToSave = new User("mary", "maliska_myu@mail.ru");
        passwordHash = encoder.encode("marypwd123");
        userToSave.setPasswordHash(passwordHash);
        userRepository.save(userToSave);
        if (userToSave.getId() != null) {
            listIDs.add(userToSave.getId());
            if (userRepository.findById(userToSave.getId()).isPresent()) {
                savedUser = userRepository.findById(userToSave.getId()).get();
                assertEquals("mary", savedUser.getLogin());
                assertEquals("maliska_myu@mail.ru", savedUser.getEmail());
                assertEquals(passwordHash, savedUser.getPasswordHash());
            }
        }
        userToSave = new User("queen", "queen@gmail.com");
        passwordHash = encoder.encode("queenpwd444");
        userToSave.setPasswordHash(passwordHash);
        userRepository.save(userToSave);
        if (userToSave.getId() != null) {
            listIDs.add(userToSave.getId());
            if (userRepository.findById(userToSave.getId()).isPresent()) {
                savedUser = userRepository.findById(userToSave.getId()).get();
                assertEquals("queen", savedUser.getLogin());
                assertEquals("queen@gmail.com", savedUser.getEmail());
                assertEquals(passwordHash, savedUser.getPasswordHash());
             }
        }
    }

    @Test
    void testFindByLoginAdmin() {
        User admin = userRepository.findByLogin("admin");
        if (admin.getId() != null) {
            assertEquals("admin", admin.getLogin());
            assertEquals("admin@gmail.com", admin.getEmail());
        }
    }

    @Test
    void testFindAllWithSortByLogin() {
        Sort sort = Sort.by("login").ascending();
        List<User> allUsers = userRepository.findAll(sort);
        assertEquals(3, allUsers.size());
        assertEquals("admin",allUsers.get(0).getLogin());
        assertEquals("mary",allUsers.get(1).getLogin());
        assertEquals("queen",allUsers.get(2).getLogin());
    }

    @Test
    void testReadByIdUpdateUserPasswordEmail() {
        if (userRepository.findById(listIDs.get(1)).isPresent()) {
            User userToUpdate = userRepository.findById(listIDs.get(1)).get();
            String newPasswordHash = encoder.encode("newPassword2023%%");
            userToUpdate.setEmail("new_email@gmail.com");
            userToUpdate.setPasswordHash(newPasswordHash);
            userRepository.save(userToUpdate);
            // reread user
            userToUpdate = userRepository.findById(listIDs.get(1)).get();
            assertEquals("new_email@gmail.com", userToUpdate.getEmail());
            assertEquals(newPasswordHash, userToUpdate.getPasswordHash());
        }
    }

    @AfterEach
    public void tearDown() {
        for (Long id : listIDs) {
            userRepository.deleteById(id);
        }
        listIDs.clear();
        assertEquals(1, userRepository.findAll().size());
    }
}
