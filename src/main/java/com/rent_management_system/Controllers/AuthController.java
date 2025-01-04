package com.rent_management_system.Controllers;

import com.rent_management_system.Components.JWTAccess;
import com.rent_management_system.DTO.UserDTO;
import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Models.User;
import com.rent_management_system.Response.ResponseHandler;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTAccess jwtAccess;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JWTAccess jwtAccess) {
        this.authenticationManager = authenticationManager;
        this.jwtAccess = jwtAccess;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticateUser(@RequestBody User user, HttpSession session){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (authentication.isAuthenticated()){
            String token = jwtAccess.generateToken(user.getEmail());
            return ResponseHandler.responseBuilder("authenticated", token, HttpStatus.OK);
        }else{
            throw  new InvalidDataException("Invalid credentials");
        }
    }
}