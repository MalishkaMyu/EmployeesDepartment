package com.samsolutions.employeesdep.exception;

public class EntityDuplicateException extends EmployeesdepGlobalException{
    public EntityDuplicateException() {
        super("Entity exists already in the DB.",
                GlobalErrorCode.ERROR_ENTITY_DUPLICATE,
                null);
    }

    public EntityDuplicateException(Class<?> obj) {
        super("Entity exists already in the DB.",
                GlobalErrorCode.ERROR_ENTITY_DUPLICATE,
                obj);
    }

    public EntityDuplicateException (String message) {
        super(message,
                GlobalErrorCode.ERROR_ENTITY_DUPLICATE,
                null);
    }

    public EntityDuplicateException (String message, Class<?> obj) {
        super(message,
                GlobalErrorCode.ERROR_ENTITY_DUPLICATE,
                obj);
    }
}
