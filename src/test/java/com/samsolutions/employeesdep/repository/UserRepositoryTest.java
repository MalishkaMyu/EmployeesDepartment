package com.samsolutions.employeesdep.repository;

import com.samsolutions.employeesdep.model.entities.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private PasswordEncoder encoder;

    @Test
    void testEncodedSize() {
        String pwd="01234567890123456789";
        String passwordHash = encoder.encode(pwd);
        System.out.println("Pwd: " + pwd + " Encoded password: " + passwordHash +
                " length=" + passwordHash.length());
        pwd = "0123456789";
        passwordHash = encoder.encode(pwd);
        System.out.println("Pwd: " + pwd + " Encoded password: " + passwordHash +
                " length=" + passwordHash.length());
    }
}
