package com.samsolutions.employeesdep.exception;

public class EntityNotFoundException extends EmployeesdepGlobalException{
    public EntityNotFoundException() {
        super("Requested entity not present in the DB.",
                GlobalErrorCode.ERROR_ENTITY_NOT_FOUND,
                "unknown Object");
    }

    public EntityNotFoundException (String message) {
        super(message,
                GlobalErrorCode.ERROR_ENTITY_NOT_FOUND,
                "unknown Object");
    }

    public EntityNotFoundException (String message, String objectName) {
        super(message,
                GlobalErrorCode.ERROR_ENTITY_NOT_FOUND,
                objectName);
    }
}
