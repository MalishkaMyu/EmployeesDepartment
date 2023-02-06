package com.samsolutions.employeesdep.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesdepGlobalException extends RuntimeException {
    private String message;
    private String code;
    private Class<?> objectClass;

    public EmployeesdepGlobalException(String message) {
        super(message);
    }
}
