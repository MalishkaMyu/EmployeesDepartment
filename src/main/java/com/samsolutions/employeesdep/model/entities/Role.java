package com.samsolutions.employeesdep.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Role {
    @Id
    @Column(name = "role_id", unique = true, nullable = false)
    @SequenceGenerator(name = "pk_seq_role", sequenceName = "seq_role_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_seq_role")
    private Long id;

    @Column(name = "role", length = 30)
    @NonNull
    private String role;

    @Override
    public String toString() {
        return role;
    }
}
