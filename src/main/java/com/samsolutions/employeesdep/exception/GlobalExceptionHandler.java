package com.samsolutions.employeesdep.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EmployeesdepGlobalException.class)
    @SuppressWarnings("rawtypes")
    protected ResponseEntity handleGlobalException(EmployeesdepGlobalException employeesdepGlobalException, Locale locale) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .code(employeesdepGlobalException.getCode())
                        .message(employeesdepGlobalException.getMessage())
                        .build());
    }

    @ExceptionHandler({Exception.class})
    @SuppressWarnings("rawtypes")
    protected ResponseEntity handleException(Exception e, Locale locale) {
        return ResponseEntity
                .badRequest()
                .body("Exception occur inside API " + e);
    }
}
