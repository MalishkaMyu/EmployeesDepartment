package com.samsolutions.employeesdep.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Department implements Serializable {
    @Id
    @Column(name = "depart_id", unique = true, nullable = false)
    @SequenceGenerator(name = "pk_seq_depart", sequenceName = "seq_depart_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_seq_depart")
    private Long id;

    @Column(name = "depart_name", length = 50)
    @NonNull
    private String name;
}
