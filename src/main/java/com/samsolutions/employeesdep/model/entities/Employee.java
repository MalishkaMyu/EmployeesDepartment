package com.samsolutions.employeesdep.model.entities;

import com.samsolutions.employeesdep.model.converters.GenderConverter;
import com.samsolutions.employeesdep.model.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee extends AbstractDatetimeEntity implements Serializable {
    @Id
    @Column(name = "emp_id", unique = true, nullable = false)
    @SequenceGenerator(name = "pk_seq_emp", sequenceName = "seq_emp_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_seq_emp")
    private Long id;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "surname", length = 30)
    private String surname;

    @Column(name = "sex")
    @Convert(converter = GenderConverter.class)
    private Gender sex;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "pass_no")
    private String passNumber;

    @Column(name = "pass_valid")
    private LocalDate passValid;

    @Column(name = "employment_date")
    private LocalDate employmentDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "depart_id")
    private Department department;

    @ManyToMany
    @JoinTable(name = "employees_roles",
            joinColumns = @JoinColumn(name = "emp_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> employeeRoles;

    public Employee(String name, String surname, Gender sex, LocalDate birthDate, LocalDate employmentDate) {
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.birthDate = birthDate;
        if (employmentDate != null)
            this.employmentDate = employmentDate;
        else
            this.employmentDate = LocalDate.now();
    }
}
