package com.rent_management_system.user;

import com.rent_management_system.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * @auther Emmanuel Yidana
     * @description: A method to return fetch all users
     * @date 016-01-2025
     * @return List of users
     */
    @Operation(summary = "fetch all users", description = "This api fetch all users from the database. no parameter is required")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class))}, description = "users fetched successfully"),
            @ApiResponse(responseCode = "404", content = { @Content}, description = "no data found")
    })
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
    @Operation(summary = "create user", description = "an api to add/create new user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class))},
                    description = "users fetched successfully"),
            @ApiResponse(responseCode = "400", content = { @Content}, description = "user already exist")
    })
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
    @Operation(summary = "get user by id", description = "retrieve user data by the user Id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class))},
                    description = "User details"),
            @ApiResponse(responseCode = "404", content = { @Content}, description = "user not found")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserById(@Parameter(
            description = "ID of the user to be retrieved",
            required = true) @PathVariable Long id){
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
    @Operation(summary = "remove user by id", description = "remove user by the user Id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", content = { @Content}, description = "user not found")
    })
    @DeleteMapping("/remove-user/{id}")
    public ResponseEntity<Object> removeUserById(@Parameter(
            description = "Id of the user to be deleted",
            required = true
    ) @PathVariable Long id){
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
    @Operation(summary = "update user by id", description = "update user data by the user Id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class))},
                    description = "User updated successfully"),
            @ApiResponse(responseCode = "404", content = { @Content}, description = "user not found")
    })
    @PutMapping("/update-user/{id}")
    public ResponseEntity<Object> updateUserById(@Parameter(
            description = "Id of the user to be updated",
            required = true
    ) @PathVariable Long id, @RequestBody User user){
        log.info("In update user by Id method:======");
        UserDTO userDTO = userService.updateUserById(id, user);
        return ResponseHandler.responseBuilder("User updated successfully", userDTO, HttpStatus.OK);
    }
}