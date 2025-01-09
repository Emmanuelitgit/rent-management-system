package com.rent_management_system.Authentiication;

import com.rent_management_system.Components.JWTAccess;
import com.rent_management_system.Components.OTPComponent;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.User.UserDTO;
import com.rent_management_system.User.UserDTOMapper;
import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.User.User;
import com.rent_management_system.User.UserRepository;
import com.rent_management_system.Response.ResponseHandler;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTAccess jwtAccess;
    private final UserRepository userRepository;
    private final OTPRepository otpRepository;
    private final OTPComponent OTPComponent;
    private final OTPService otpService;
    private final OTPComponent otpComponent;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JWTAccess jwtAccess, UserRepository userRepository, UserRepository userRepository1, OTPRepository otpRepository, OTPComponent OTPComponent, OTPService otpService, com.rent_management_system.Components.OTPComponent otpComponent) {
        this.authenticationManager = authenticationManager;
        this.jwtAccess = jwtAccess;
        this.userRepository = userRepository1;
        this.otpRepository = otpRepository;
        this.OTPComponent = OTPComponent;
        this.otpService = otpService;
        this.otpComponent = otpComponent;
    }

    private Object getUserDetails(User user){
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        String token = jwtAccess.generateToken(user.getEmail());
        if (userOptional.isEmpty()){
            throw new NotFoundException("User not found");
        }
        UserDTO userData  = UserDTOMapper.toDTO(userOptional.get());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("data", userData);
        return data;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticateUser(@RequestBody User user, HttpSession session){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        OTPComponent.verifyUserOTPStatusDuringLogin(user.getEmail());
        if (authentication.isAuthenticated()){
           Object data = getUserDetails(user);
            return ResponseHandler.responseBuilder("authenticated", data, HttpStatus.OK);
        }else{
            throw  new InvalidDataException("Invalid credentials");
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Object> verifyEmail(@Valid @RequestBody OTPVerifyPayload payload){
        OTPComponent.verifyOtp(payload.email, payload.otp);
        return ResponseHandler.responseBuilder("verified successfully", true, HttpStatus.OK);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Object> resendOTP(@RequestBody OTPVerifyPayload payload){
        Optional<User> userOptional = userRepository.findUserByEmail(payload.email);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User not found");
        }
        Optional<OTP> otpOptional = otpRepository.findOTPVerificationByUser_Id(userOptional.get().getId());
        if (otpOptional.isEmpty()){
            throw new NotFoundException("OTP not found");
        }
        otpService.removeOTPById(otpOptional.get().getId());
        Long otpCode = otpComponent.generateOTP();
        otpComponent.sendOTP(payload.email, otpCode);
        otpService.resendOTP(payload.email, otpCode);
        return ResponseHandler.responseBuilder("OTP sent successfully", null, HttpStatus.OK);
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