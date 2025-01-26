package com.rent_management_system.authentication;

import com.rent_management_system.exception.NotFoundException;
import com.rent_management_system.exception.UnAuthorizedException;
import com.rent_management_system.user.UserDTO;
import com.rent_management_system.user.UserDTOMapper;
import com.rent_management_system.user.User;
import com.rent_management_system.user.UserRepository;
import com.rent_management_system.response.ResponseHandler;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTAccess jwtAccess;
    private final UserRepository userRepository;
    private final OTPRepository otpRepository;
    private final OTPService otpService;
    private final OTPComponent otpComponent;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JWTAccess jwtAccess, UserRepository userRepository, OTPRepository otpRepository, OTPService otpService, OTPComponent otpComponent) {
        this.authenticationManager = authenticationManager;
        this.jwtAccess = jwtAccess;
        this.otpRepository = otpRepository;
        this.otpService = otpService;
        this.otpComponent = otpComponent;
        this.userRepository = userRepository;
    }

    /**
     * @auther Emmanuel Yidana
     * @description: an insider method used to get user details. it is called in the authenticate method to get user details for authenticating
     * @date 016-01-2025
     * @param user object consisting of username and password
     * @throws NotFoundException - if user does not exist
     * @return user object
     */
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

    /**
     * @auther Emmanuel Yidana
     * @description: a method for authenticating users during login
     * @date 016-01-2025
     * @param user object consisting of username and password
     * @throws UnAuthorizedException - if user provide wrong credentials
     * @return user object including a token
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticateUser(@RequestBody User user, HttpServletResponse response){
        log.info("In authenticate method:===========");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        otpComponent.verifyUserOTPStatusDuringLogin(user.getEmail());
        if (authentication.isAuthenticated()){
           Object data = getUserDetails(user);
           log.info("Authenticated successfully:============");
           // setting token in a cookie
            Cookie cookie = new Cookie("token",jwtAccess.generateToken(user.getEmail()));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(100);
            response.addCookie(cookie);

            return ResponseHandler.responseBuilder("authenticated", data, HttpStatus.OK);
        }else{
            throw  new UnAuthorizedException("Invalid credentials");
        }
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for authenticating users via GITHUB
     * @date 016-01-2025
     */
    @GetMapping("/login/github")
    public ResponseEntity<Void> loginWithGitHub(HttpServletResponse response) throws IOException {
        log.info("In login with github method:=============");
        String authorizationUrl = "/oauth2/authorization/github";
        response.sendRedirect(authorizationUrl);
        log.info("Login with github success:==============");
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for authenticating users via GOOGLE
     * @date 016-01-2025
     */
    @GetMapping("/login/google")
    public ResponseEntity<Void> loginWithGoogle(HttpServletResponse response) throws IOException {
        log.info("In login with google method:=============");
        String authorizationUrl = "/oauth2/authorization/google";
        response.sendRedirect(authorizationUrl);
        log.info("Login with google success:==============");
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for verifying otp against email
     * @date 016-01-2025
     * @return ResponseEntity with boolean
     */
    @PostMapping("/verify-email")
    public ResponseEntity<Object> verifyEmail(@Valid @RequestBody OTPVerifyPayload payload){
        log.info("In verify otp method:=================");
        otpComponent.verifyOtp(payload.email, payload.otp);
        log.info("OTP verified successfully:==============");
        return ResponseHandler.responseBuilder("verified successfully", true, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for resending otp
     * @date 016-01-2025
     * @return ResponseEntity with boolean
     */
    @PostMapping("/resend-otp")
    public ResponseEntity<Object> resendOTP(@RequestBody OTPVerifyPayload payload){
        log.info("In resend otp method:================");
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
        otpComponent.sendOTP(payload.email, otpCode, userOptional.get().getFirstName());
        otpService.resendOTP(payload.email, otpCode);
        log.info("OTP sent successfully:===============");
        return ResponseHandler.responseBuilder("OTP sent successfully", null, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for retrieving all otp
     * @date 016-01-2025
     * @return list of otp
     */
    @GetMapping("/otp")
    public ResponseEntity<Object> getOTPs(){
        List<OTP> otp = otpRepository.findAll();
        return ResponseHandler.responseBuilder("otps", otp, HttpStatus.OK);
    }

}