package com.rent_management_system.Components;

import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.Models.OTPVerification;
import com.rent_management_system.Models.User;
import com.rent_management_system.Repositories.OTPVerificationRepository;
import com.rent_management_system.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class VerifyOTP {
    private final OTPVerificationRepository otpVerificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public VerifyOTP(OTPVerificationRepository otpVerificationRepository, UserRepository userRepository) {
        this.otpVerificationRepository = otpVerificationRepository;
        this.userRepository = userRepository;
    }

    public boolean verifyOtp(String email, int otp){
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User email not found to send otp");
        }
        User user = userOptional.get();
        Optional<OTPVerification> verificationOptional = otpVerificationRepository.findOTPVerificationByUser_Id(user.getId());
        if (verificationOptional.isPresent()){
            boolean isOtpExpired = Date.from(Instant.now()).before(verificationOptional.get().getCreatedAt());
            boolean isOtpMatch = verificationOptional.get().getOtp() == otp;
            log.info("isExpired:{}", isOtpExpired);
            log.info("isotptMatch:{}", isOtpMatch);
            if (!isOtpExpired || !isOtpMatch){
                otpVerificationRepository.deleteById(verificationOptional.get().getId());
                throw new InvalidDataException("Otp has expired or invalid");
            }
            return true;
        }
        return false;
    }


    public boolean verifyUserOTPStatus(String email){
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User email not found to send otp");
        }
        User user = userOptional.get();
        Optional<OTPVerification> verificationOptional = otpVerificationRepository.findOTPVerificationByUser_Id(user.getId());
        if (verificationOptional.isPresent()){
            throw new NotFoundException("User not verified");
        }
        return true;
    }
}
