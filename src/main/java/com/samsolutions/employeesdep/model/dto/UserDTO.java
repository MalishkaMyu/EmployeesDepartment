package com.samsolutions.employeesdep.model.dto;

import com.samsolutions.employeesdep.config.MyPasswordEncoder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @Autowired
    private MyPasswordEncoder encoder;

    private Long id;
    private String login;
    private String password;
    private String passwordHash;
    private String email;

    public UserDTO(String login, String password, String email) {
        this.login = login;
        this.email = email;
        this.password = password;
        if (!password.isBlank()) {
            this.passwordHash = encoder.encode(password);
        }
    }

    public void setPassword(String password) {
        this.password = password;
        if (!password.isBlank()) {
            this.passwordHash = encoder.encode(password);
        }
    }
}