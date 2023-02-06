package com.samsolutions.employeesdep.exception;

public class EntityNotFoundException extends EmployeesdepGlobalException{
    public EntityNotFoundException() {
        super("Requested entity not present in the DB.",
                GlobalErrorCode.ERROR_ENTITY_NOT_FOUND,
                null);
    }

    public EntityNotFoundException(Class<?> obj) {
        super("Requested entity not present in the DB.",
                GlobalErrorCode.ERROR_ENTITY_NOT_FOUND,
                obj);
    }

    public EntityNotFoundException (String message) {
        super(message,
                GlobalErrorCode.ERROR_ENTITY_NOT_FOUND,
                null);
    }

    public EntityNotFoundException (String message, Class<?> obj) {
        super(message,
                GlobalErrorCode.ERROR_ENTITY_NOT_FOUND,
                obj);
    }
}
