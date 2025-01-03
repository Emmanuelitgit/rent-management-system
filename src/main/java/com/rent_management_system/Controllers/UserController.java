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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        log.info("In create user method:=========");
        if (result.hasErrors()){
            throw  new InvalidDataException("Invalid data");
        }
        if (user == null){
            throw new NotFoundException("No data provided");
        }
        UserDTO userData = userService.createUser(user);
        return ResponseHandler.responseBuilder("user added successfully", userData, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(){
        List<UserDTO> userDTOList = userService.getUsers();
        log.info("In fetch users method:==========");
        if (userDTOList.isEmpty()){
            throw new NotFoundException("No data found");
        }
        return ResponseHandler.responseBuilder("User details", userDTOList, HttpStatus.OK);
    }
}