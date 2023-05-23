package com.samsolutions.employeesdep.exception;

public class StatusWrongException extends EmployeesdepGlobalException{
    public StatusWrongException() {
        super("Status is wrong.",
                GlobalErrorCode.ERROR_STATUS_WRONG,
                null);
    }

    public StatusWrongException(Class<?> obj) {
        super("Status is wrong.",
                GlobalErrorCode.ERROR_STATUS_WRONG,
                obj);
    }

    public StatusWrongException(String message) {
        super(message,
                GlobalErrorCode.ERROR_STATUS_WRONG,
                null);
    }

    public StatusWrongException(String message, Class<?> obj) {
        super(message,
                GlobalErrorCode.ERROR_STATUS_WRONG,
                obj);
    }
}
