package com.samsolutions.employeesdep.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleDTO {

    private Long id;
    private String role;

    public RoleDTO(String role) {
        this.role = role;
    }
}
