package com.rent_management_system.Controllers;

import com.rent_management_system.DTO.UserDTO;
import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.Models.User;
import com.rent_management_system.Response.ResponseHandler;
import com.rent_management_system.ServiceImp.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<Object> createUser(@RequestBody @Valid User user, BindingResult result){
        if (result.hasErrors()){
            throw  new InvalidDataException("Invalid data");
        }
        if (user == null){
            throw new NotFoundException("No data provided");
        }
        log.info("In create user method:=========");
        UserDTO userData = userService.createUser(user);
        return ResponseHandler.responseBuilder("user added successfully", userData, HttpStatus.CREATED);
    }
}