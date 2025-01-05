package com.rent_management_system.Exception;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(String message) {
        super(message);
    }

    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
