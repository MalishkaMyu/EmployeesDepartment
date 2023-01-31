package com.samsolutions.employeesdep.services;

import com.samsolutions.employeesdep.config.MyPasswordEncoder;
import com.samsolutions.employeesdep.exception.EntityDuplicateException;
import com.samsolutions.employeesdep.exception.EntityNotFoundException;
import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserServiceTest {

    @Value("${spring.flyway.placeholders.admin_login}")
    private String adminLogin;
    @Value("${spring.flyway.placeholders.admin_email}")
    private String adminEmail;

    @Autowired
    private UserService userService;

    @Autowired
    private MyPasswordEncoder encoder;

    private final List<Long> listIDs = new ArrayList<>();

    @BeforeEach
    public void setup() {
        UserDTO userToSave, savedUser;

        // saving the first User
        userToSave = new UserDTO("krosh", "kroshpwd", "krosh@mail.ru");
        savedUser = userService.createUser(userToSave);
        if (savedUser.getId() != null)
            listIDs.add(savedUser.getId());

        // saving the second user
        userToSave = new UserDTO("nyusha", "nyushapwd", "nyusha@mail.ru");
        savedUser = userService.createUser(userToSave);
        if (savedUser.getId() != null)
            listIDs.add(savedUser.getId());
    }

    @Test
    public void readUserById() {
        Long userId = listIDs.get(0);
        UserDTO readUser = userService.getUserById(userId);
        if (readUser != null) {
            assertEquals("krosh", readUser.getLogin());
            assertTrue(encoder.matches("kroshpwd", readUser.getPasswordHash()));
            assertEquals("krosh@mail.ru", readUser.getEmail());
        }
    }

    @Test
    public void readAdminByLogin() {
        UserDTO readUser = userService.getUserByLogin(adminLogin);
        if (readUser != null) {
            assertEquals(adminLogin, readUser.getLogin());
            assertEquals(adminEmail, readUser.getEmail());
        }
    }

    @Test
    public void readUserByLogin() {
        UserDTO readUser = userService.getUserByLogin("nyusha");
        if (readUser != null) {
            assertEquals("nyusha", readUser.getLogin());
            assertTrue(encoder.matches("nyushapwd", readUser.getPasswordHash()));
            assertEquals("nyusha@mail.ru", readUser.getEmail());
        }
    }

    @Test
    public void updateUser() {
        Long userId = listIDs.get(1);
        UserDTO userToSave = userService.getUserById(userId);
        if (userToSave != null) {
            assertEquals("nyusha", userToSave.getLogin());
            // updating fields
            userToSave.setPassword("newnyushapwd");
            userToSave.setEmail("newnyusha@mail.ru");
            // updating user and check saved values
            UserDTO savedUser = userService.updateUser(userToSave);
            assertEquals("nyusha", savedUser.getLogin());
            assertEquals("newnyushapwd", savedUser.getPassword());
            assertTrue(encoder.matches("newnyushapwd", savedUser.getPasswordHash()));
            assertEquals("newnyusha@mail.ru", savedUser.getEmail());

            // restore old values
            userToSave.setPassword("nyushapwd");
            userToSave.setEmail("nyusha@mail.ru");
            userService.updateUser(userToSave);
        }
    }

    @Test
    public void findAllUsersWithPages() {
        // first page
        List<UserDTO> users = userService.getAllUsers();
        assertEquals(2, users.size());
        // second page
        users = userService.getAllUsers(1);
        assertEquals(1, users.size());
    }

    @Test
    public void findUserByLoginNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                        userService.getUserByLogin("bibi"),
                "EntityNotFoundException was expected");
    }

    @Test
    public void findUserByIdNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                        userService.getUserById(1000L),
                "EntityNotFoundException was expected");
    }

    @Test
    public void createUserDuplicateByLoginException() {
        UserDTO userToSave, savedUser;

        // creating user with existing login
        userToSave = new UserDTO("krosh", "kroshanotherpwd", "krosh2@mail.ru");
        // saving of the user must cause exception
        assertThrows(EntityDuplicateException.class, () ->
                        userService.createUser(userToSave),
                "EntityDuplicateException was expected.");
    }

    @Test
    public void createUserDuplicateByEmailException() {
        UserDTO userToSave, savedUser;

        // creating user with existing email
        userToSave = new UserDTO("bibi", "bibipws123", "mary@sam-solutions.com");
        // saving of the user must cause exception
        assertThrows(EntityDuplicateException.class, () ->
                        userService.createUser(userToSave),
                "EntityDuplicateException was expected.");
    }

    @Test
    public void updateUserDuplicateByEmailException() {
        Long userId = listIDs.get(1);
        UserDTO userToSave = userService.getUserById(userId);
        if (userToSave != null) {
            assertEquals("nyusha", userToSave.getLogin());
            // set email existing by another user
            userToSave.setEmail("krosh@mail.ru");
            // saving of the user must cause exception
            assertThrows(EntityDuplicateException.class, () ->
                            userService.updateUser(userToSave),
                    "EntityDuplicateException was expected.");
        }
    }

    @AfterEach
    public void tearDown() {
        for (Long id : listIDs) {
            assertTrue(userService.deleteUserById(id));
        }
        listIDs.clear();
        assertEquals(1, userService.getAllUsers().size());
    }
}
