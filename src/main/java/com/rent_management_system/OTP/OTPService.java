package com.rent_management_system.OTP;

import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.User.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private final OTPRepository otpRepository;

    @Autowired
    public OTPService(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }


    @Transactional
    public void removeOTPById(Long id) {
        // Find the OTPVerification by ID
        OTP otp = otpRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("OTP not found"));

        // Remove the association with the User
        User user = otp.getUser();
        if (user != null) {
            user.setOtp(null);
        }

        // Delete the OTPVerification
        otpRepository.delete(otp);
    }

}
