package com.rent_management_system.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }
}