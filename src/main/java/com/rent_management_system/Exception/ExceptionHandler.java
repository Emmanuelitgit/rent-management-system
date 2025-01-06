package com.rent_management_system.Exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler{

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> objectBody = new HashMap<>();
        objectBody.put("Current Timestamp", new Date());
        objectBody.put("Status", status.value());

        // Get all errors
        List<String> exceptionalErrors
                = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        objectBody.put("Errors", exceptionalErrors);

        return new ResponseEntity<>(objectBody, status);
    }


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
