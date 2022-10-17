package com.samsolutions.employeesdep.model.entities;

import com.samsolutions.employeesdep.model.converters.GenderConverter;
import com.samsolutions.employeesdep.model.enums.Gender;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee {
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

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "depart_id")
    private Department department = new Department();

    @ManyToMany
    @JoinTable(name = "employees_roles",
            joinColumns = @JoinColumn(name = "emp_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> employeeRoles;

    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassNumber() {
        return passNumber;
    }

    public void setPassNumber(String passNumber) {
        this.passNumber = passNumber;
    }

    public LocalDate getPassValid() {
        return passValid;
    }

    public void setPassValid(LocalDate passValid) {
        this.passValid = passValid;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Role> getEmployeeRoles() {
        return employeeRoles;
    }

    public void setEmployeeRoles(Set<Role> employeeRoles) {
        this.employeeRoles = employeeRoles;
    }
}
