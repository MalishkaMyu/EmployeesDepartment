package com.samsolutions.employeesdep.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DepartmentDTO {
    private Long id;
    private String name;

    public DepartmentDTO(String name) {
        this.name = name;
    }
}
