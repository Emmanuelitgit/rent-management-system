package com.rent_management_system.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Exception> handleNotFoundException(NotFoundException notFoundException){
        Exception exception = new Exception(
                notFoundException.getMessage(),
                notFoundException.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<InvalidDataException> handleInvalidDataException(InvalidDataException invalidDataException){
        Exception exception = new Exception(
                invalidDataException.getMessage(),
                invalidDataException.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<InvalidDataException> handleUnAuthorizedException(UnAuthorizedException unAuthorizedException){
        Exception exception = new Exception(
                unAuthorizedException.getMessage(),
                unAuthorizedException.getCause(),
                HttpStatus.UNAUTHORIZED
        );

        return new ResponseEntity(exception, HttpStatus.UNAUTHORIZED);
    }
}
