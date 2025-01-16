package com.rent_management_system.User;

import com.rent_management_system.Exception.NotFoundException;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to return fetch all users
     * @date 016-01-2025
     * @return List of users
     */
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(){
        List<UserDTO> userDTOList = userService.getUsers();
        log.info("In fetch users method:==========");
        return ResponseHandler.responseBuilder("User details", userDTOList, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to create a new user
     * @date 016-01-2025
     * @param user object
     * @return user object
     */
    @PostMapping("/create-user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        log.info("In create user method:=========");
        UserDTO userData = userService.createUser(user);
        log.info("New user created successfully:=========");
        return ResponseHandler.responseBuilder("user added successfully", userData, HttpStatus.CREATED);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to get user by id
     * @date 016-01-2025
     * @param: id
     * @return user object
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id){
        log.info("In get user by ID method:========");
        UserDTO userDTO = userService.getUserById(id);
        return ResponseHandler.responseBuilder("User details", userDTO, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to remove user by id
     * @date 016-01-2025
     * @param: id
     * @return ResponseEntity with null data
     */
    @DeleteMapping("/remove-user/{id}")
    public ResponseEntity<Object> removeUserById(@PathVariable Long id){
        log.info("In remove user by Id method:======");
        userService.removeUserById(id);
        return ResponseHandler.responseBuilder("User deleted successfully", null, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to update user by id
     * @date 016-01-2025
     * @param id, user object
     * @return updated user object
     */
    @PutMapping("/update-user/{id}")
    public ResponseEntity<Object> updateUserById(@PathVariable Long id, User user){
        log.info("In update user by Id method:======");
        UserDTO userDTO = userService.updateUserById(id, user);
        return ResponseHandler.responseBuilder("User updated successfully", userDTO, HttpStatus.OK);
    }

}