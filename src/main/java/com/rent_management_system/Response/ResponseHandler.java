package com.rent_management_system.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseHandler {

    public static ResponseEntity<Object> responseBuilder(String message, Object data, HttpStatus status){
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("data", data);
        response.put("status", status);

        return new ResponseEntity<>(response, status);
    }
}
