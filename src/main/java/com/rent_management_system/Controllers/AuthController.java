package com.rent_management_system.Controllers;

import com.rent_management_system.Components.JWTAccess;
import com.rent_management_system.Components.VerifyOTP;
import com.rent_management_system.DTO.UserDTO;
import com.rent_management_system.DTOMappers.UserDTOMapper;
import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.UnAuthorizedException;
import com.rent_management_system.Models.User;
import com.rent_management_system.Repositories.OTPVerificationRepository;
import com.rent_management_system.Repositories.UserRepository;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTAccess jwtAccess;
    private final UserRepository userRepository;
    private final OTPVerificationRepository otpVerificationRepository;
    private final VerifyOTP verifyOTP;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JWTAccess jwtAccess, UserRepository userRepository, UserRepository userRepository1, OTPVerificationRepository otpVerificationRepository, VerifyOTP verifyOTP) {
        this.authenticationManager = authenticationManager;
        this.jwtAccess = jwtAccess;
        this.userRepository = userRepository1;
        this.otpVerificationRepository = otpVerificationRepository;
        this.verifyOTP = verifyOTP;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticateUser(@RequestBody User user, HttpSession session){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean status = verifyOTP.verifyUserOTPStatus(user.getEmail());
        log.info("OPT STATUS:====={}", status);
        if (authentication.isAuthenticated()){
            String token = jwtAccess.generateToken(user.getEmail());
            Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
            UserDTO userData  = UserDTOMapper.toDTO(userOptional.get());
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("data", userData);
            return ResponseHandler.responseBuilder("authenticated", data, HttpStatus.OK);
        }else{
            throw  new InvalidDataException("Invalid credentials");
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Object> verifyEmail(){
        String email = "eyidana001@gmail.com";
        int otp = 1234;
        boolean verified = verifyOTP.verifyOtp(email, otp);
        log.info("Verified in auth controller:{}", verified);
        if (!verified) throw new UnAuthorizedException("not verified");
        return ResponseHandler.responseBuilder("verified successfully", true, HttpStatus.OK);
    }

}