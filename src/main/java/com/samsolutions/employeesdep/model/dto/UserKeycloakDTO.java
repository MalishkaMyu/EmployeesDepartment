package com.samsolutions.employeesdep.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserKeycloakDTO {
    private String keycloakId;
    private String login;
    private String password;
    private String email;
    private String name;
    private String surname;

    private List<String> roles;

    public UserKeycloakDTO(String login, String password, String email, String name, String surname) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }
}