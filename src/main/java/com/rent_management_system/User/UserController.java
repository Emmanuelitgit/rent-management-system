package com.rent_management_system.User;

import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.OTP.OTP;
import com.rent_management_system.OTP.OTPRepository;
import com.rent_management_system.Response.ResponseHandler;
import com.rent_management_system.OTP.OTPService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final OTPRepository otpRepository;
    private final OTPService otpVerificationService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, OTPRepository otpRepository, OTPService otpVerificationService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.otpVerificationService = otpVerificationService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        log.info("In create user method:=========");
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()){
            throw new InvalidDataException("Üser Already exist");
        }
        UserDTO userData = userService.createUser(user);
        log.info("New user created successfully:=========");
        return ResponseHandler.responseBuilder("user added successfully", userData, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(){
        List<UserDTO> userDTOList = userService.getUsers();
        log.info("In fetch users method:==========");
        return ResponseHandler.responseBuilder("User details", userDTOList, HttpStatus.OK);
    }

//    @DeleteMapping("/remove-otp/{id}")
//    public ResponseEntity<Object> removeOTP(@PathVariable Long id){
//        otpVerificationService.removeOTPByUserId(id);
//        return ResponseHandler.responseBuilder("deleted", null, HttpStatus.OK);
//    }

    @GetMapping("/otp")
    public ResponseEntity<Object> getOTPs(){
        List<OTP> otp = otpRepository.findAll();
        return ResponseHandler.responseBuilder("otps", otp, HttpStatus.OK);
    }
}