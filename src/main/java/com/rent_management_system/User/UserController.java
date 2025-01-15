package com.rent_management_system.User;

import com.rent_management_system.Response.ResponseHandler;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(){
        List<UserDTO> userDTOList = userService.getUsers();
        log.info("In fetch users method:==========");
        return ResponseHandler.responseBuilder("User details", userDTOList, HttpStatus.OK);
    }

    @PostMapping("/create-user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        log.info("In create user method:=========");
        UserDTO userData = userService.createUser(user);
        log.info("New user created successfully:=========");
        return ResponseHandler.responseBuilder("user added successfully", userData, HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id){
        log.info("In get user by ID method:========");
        UserDTO userDTO = userService.getUserById(id);
        return ResponseHandler.responseBuilder("User details", userDTO, HttpStatus.OK);
    }

    @DeleteMapping("/remove-user/{id}")
    public ResponseEntity<Object> removeUserById(@PathVariable Long id){
        log.info("In remove user by Id method:======");
        userService.removeUserById(id);
        return ResponseHandler.responseBuilder("User deleted successfully", null, HttpStatus.OK);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<Object> updateUserById(@PathVariable Long id, User user){
        log.info("In update user by Id method:======");
        UserDTO userDTO = userService.updateUserById(id, user);
        return ResponseHandler.responseBuilder("User updated successfully", userDTO, HttpStatus.OK);
    }

}