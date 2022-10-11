package com.samsolutions.employeesdep.model.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id", unique = true, nullable = false)
    @SequenceGenerator(name = "pk_seq_role", sequenceName = "seq_role_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_seq_role")
    private long id;

    @Column(name = "role", length = 30)
    private String role;

    public Role() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
