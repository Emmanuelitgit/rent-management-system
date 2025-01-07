package com.rent_management_system.Components;

import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.User.User;
import com.rent_management_system.OTP.OTPRepository;
import com.rent_management_system.User.UserRepository;
import com.rent_management_system.OTP.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class OTP {
    private final OTPRepository otpRepository;
    private final UserRepository userRepository;
    private final OTPService otpVerificationService;
    private JavaMailSender mailSender;

    @Autowired
    public OTP(OTPRepository otpRepository, UserRepository userRepository, OTPService otpVerificationService) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.otpVerificationService = otpVerificationService;
    }


    public boolean sendOTP(String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("OTP Verification");
        message.setText("Hi, kindly enter the opt code below to be verified");
        message.setFrom("eyidana001@gmail.com");
        message.setTo(email);
        mailSender.send(message);
        return true;
    }


    public boolean verifyOtp(String email, int otp){
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User email not found to send otp");
        }
        User user = userOptional.get();
        Optional<com.rent_management_system.OTP.OTP> verificationOptional = otpRepository.findOTPVerificationByUser_Id(user.getId());
        if (verificationOptional.isPresent()){
            boolean expirationTime = Date.from(Instant.now()).before(verificationOptional.get().getCreatedAt());
            boolean isOtpMatch = verificationOptional.get().getOtp() == otp;
            log.info("OTP EXPIRED:{}", expirationTime);
            log.info("OTP MATCH:{}", isOtpMatch);
            if (!expirationTime || !isOtpMatch){
                otpVerificationService.removeOTPById(verificationOptional.get().getId());
                throw new InvalidDataException("Otp has expired or invalid");
            }
            otpVerificationService.removeOTPById(verificationOptional.get().getId());
            return true;
        }
        throw new NotFoundException("No OTP code associated with the provided email!");
    }


    public boolean verifyUserOTPStatusDuringLogin(String email){
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User email not found to send otp");
        }
        User user = userOptional.get();
        Optional<com.rent_management_system.OTP.OTP> verificationOptional = otpRepository.findOTPVerificationByUser_Id(user.getId());
        if (verificationOptional.isPresent()){
            throw new NotFoundException("User not verified");
        }
        return true;
    }
}