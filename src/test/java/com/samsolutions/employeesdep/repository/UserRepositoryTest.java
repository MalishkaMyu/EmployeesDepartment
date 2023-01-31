package com.samsolutions.employeesdep.repository;

import com.samsolutions.employeesdep.config.MyPasswordEncoder;
import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTest {

    @Value("${spring.flyway.placeholders.admin_login}")
    private String adminLogin;

    @Value("${spring.flyway.placeholders.admin_email}")
    private String adminEmail;

    @Autowired
    private MyPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    private final List<Long> listIDs = new ArrayList<>();

    @BeforeEach
    public void setup() {
        User userToSave, savedUser;
        String password,passwordHash;

        // saving of user "mary"
        userToSave = new User("mary", "maliska_myu@mail.ru");
        password = "marypwd123";
        userToSave.setPasswordHash(encoder.encode(password));
        userRepository.save(userToSave);
        if (userToSave.getId() != null) {
            listIDs.add(userToSave.getId());
            if (userRepository.findById(userToSave.getId()).isPresent()) {
                savedUser = userRepository.findById(userToSave.getId()).get();
                assertEquals("mary", savedUser.getLogin());
                assertEquals("maliska_myu@mail.ru", savedUser.getEmail());
                assertTrue(encoder.matches(password, savedUser.getPasswordHash()));
            }
        }

        // saving of user "queen"
        userToSave = new User("queen", "queen@gmail.com");
        password = "queenpwd444";
        userToSave.setPasswordHash(encoder.encode(password));
        userRepository.save(userToSave);
        if (userToSave.getId() != null) {
            listIDs.add(userToSave.getId());
            if (userRepository.findById(userToSave.getId()).isPresent()) {
                savedUser = userRepository.findById(userToSave.getId()).get();
                assertEquals("queen", savedUser.getLogin());
                assertEquals("queen@gmail.com", savedUser.getEmail());
                assertTrue(encoder.matches(password, savedUser.getPasswordHash()));
             }
        }
    }

    @Test
    void testFindByLoginAdmin() {
        if (userRepository.existsByLogin(adminLogin)) {
            User admin = userRepository.findByLogin(adminLogin).orElse(null);
            if (admin != null) {
                assertEquals(adminLogin, admin.getLogin());
                assertEquals(adminEmail, admin.getEmail());
            }
        }
    }

    @Test
    void testExistsByEmail() {
        assertTrue(userRepository.existsByEmail(adminEmail));
        assertTrue(userRepository.existsByEmail("maliska_myu@mail.ru"));
        assertFalse(userRepository.existsByEmail("krosh@mail.ru"));
    }

    @Test
    void testExistsByLogin() {
        assertTrue(userRepository.existsByLogin(adminLogin));
        assertTrue(userRepository.existsByLogin("mary"));
        assertFalse(userRepository.existsByLogin("crocodile"));
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
        /*for (Long id : listIDs) {
            userRepository.deleteById(id);
        }*/
        userRepository.deleteByLoginNot(adminLogin);
        //Predicate predicate = User.login.Not(adminLogin);
        listIDs.clear();
        List<User> all = userRepository.findAll();
        all.forEach(System.out::println);
        assertEquals(1, all.size());
    }
}
