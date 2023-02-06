package com.samsolutions.employeesdep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmployeesdepGlobalException.class)
    protected ResponseEntity<Object> handleGlobalException(EmployeesdepGlobalException employeesdepGlobalException, Locale locale) {
        HttpStatus status = switch (employeesdepGlobalException.getCode()) {
            case GlobalErrorCode.ERROR_ENTITY_NOT_FOUND:
                yield HttpStatus.NOT_FOUND;
            case GlobalErrorCode.ERROR_ENTITY_DUPLICATE:
                yield HttpStatus.CONFLICT;
            default:
                yield HttpStatus.BAD_REQUEST;
        };

        if ((employeesdepGlobalException.getObjectClass() != null)) {
            return new ResponseEntity<>(
                    ErrorResponse.builder()
                            .code(employeesdepGlobalException.getCode())
                            .objectName(employeesdepGlobalException.getObjectClass().getSimpleName())
                            .message(employeesdepGlobalException.getMessage())
                            .build(), status);
        } else {
            return new ResponseEntity<>(
                    ErrorResponse.builder()
                            .code(employeesdepGlobalException.getCode())
                            .message(employeesdepGlobalException.getMessage())
                            .build(), status);
        }

    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleException(Exception e, Locale locale) {
        return ResponseEntity
                .badRequest()
                .body("Exception occur inside API " + e);
    }
}
