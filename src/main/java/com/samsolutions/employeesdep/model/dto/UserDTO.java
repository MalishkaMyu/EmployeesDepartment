package com.samsolutions.employeesdep.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String login;
    private String password;
    @JsonIgnore
    private String passwordHash;
    private String email;

    public UserDTO(String login, String password, String email) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.passwordHash = "";
    }

    @JsonCreator
    public UserDTO(@JsonProperty("id") Long id, @JsonProperty("login") String login,
                   @JsonProperty("password") String password, @JsonProperty("email") String email) {
        this(login, password, email);
        this.id = id;
    }
}