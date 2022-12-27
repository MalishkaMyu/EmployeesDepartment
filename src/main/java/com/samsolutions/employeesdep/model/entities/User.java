package com.samsolutions.employeesdep.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractDateTimeEntity implements Serializable {
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    @SequenceGenerator(name = "pk_seq_user", sequenceName = "seq_user_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_seq_user")
    private Long id;

    @Column(name = "login", length = 20)
    @NonNull
    private String login;

    @Column(name = "password", length = 100)
    @NonNull
    private String passwordHash;

    @Column(name = "email", length = 70)
    @NonNull
    private String email;

    public User(String login, String email) {
        this.login = login;
        this.email = email;
        this.passwordHash = "";
    }

}
