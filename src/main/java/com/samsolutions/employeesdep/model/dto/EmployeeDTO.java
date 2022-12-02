package com.samsolutions.employeesdep.model.dto;

import com.samsolutions.employeesdep.model.entities.Department;
import com.samsolutions.employeesdep.model.entities.Role;
import com.samsolutions.employeesdep.model.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class EmployeeDTO {
    private Long id;
    private String name;
    private String surname;
    private Gender sex;
    private LocalDate birthDate;
    private String passNumber;
    private LocalDate passValid;
    private LocalDate employmentDate;
    @Positive
    private Long workExperience;

    private DepartmentDTO department;
    private Set<RoleDTO> employeeRoles;

    public EmployeeDTO(String name, String surname, Gender sex, LocalDate birthDate, LocalDate employmentDate) {
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.birthDate = birthDate;
        this.employmentDate = Objects.requireNonNullElse(employmentDate, LocalDate.now());
        this.workExperience = ChronoUnit.YEARS.between(employmentDate, LocalDate.now());
    }
}
