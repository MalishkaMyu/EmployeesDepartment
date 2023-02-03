package com.samsolutions.employeesdep.exception;

public class EntityDuplicateException extends EmployeesdepGlobalException{
    public EntityDuplicateException() {
        super("Entity exists already in the DB.",
                GlobalErrorCode.ERROR_ENTITY_DUPLICATE,
                "unknown Object");
    }

    public EntityDuplicateException (String message) {
        super(message,
                GlobalErrorCode.ERROR_ENTITY_DUPLICATE,
                "unknown Object");
    }

    public EntityDuplicateException (String message, String objectName) {
        super(message,
                GlobalErrorCode.ERROR_ENTITY_DUPLICATE,
                objectName);
    }
}
