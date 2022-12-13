package com.samsolutions.employeesdep.model.entities;

import org.hibernate.tuple.GenerationTiming;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "departments")
public class Department implements Serializable {
    @Id
    @Column(name = "depart_id", unique = true, nullable = false)
    @SequenceGenerator(name = "pk_seq_depart", sequenceName = "seq_depart_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_seq_depart")
    private Long id;

    @Column(name = "depart_name", length = 50)
    private String name;

    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
