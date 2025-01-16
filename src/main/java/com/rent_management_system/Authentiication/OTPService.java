package com.rent_management_system.Authentiication;

import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.User.User;
import com.rent_management_system.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

@Service
public class OTPService {

    private final long MINUTES = TimeUnit.MINUTES.toMillis(5);
    private final OTPRepository otpRepository;
    private final UserRepository userRepository;

    @Autowired
    public OTPService(OTPRepository otpRepository, UserRepository userRepository ) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
    }

    /**
     * @auther Emmanuel Yidana
     * @description a method to remove otp by id
     * @date 016-01-2025
     * @param: id
     * @throws NotFoundException - if otp not found
     */
    @Transactional
    public void removeOTPById(Long id) {
        OTP otp = otpRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("OTP not found"));

        User user = otp.getUser();
        if (user != null) {
            user.setOtp(null);
        }
        otpRepository.delete(otp);
    }

    /**
     * @auther Emmanuel Yidana
     * @description A method to resend otp code
     * @date 016-01-2025
     * @param email, otpCode
     * @throws NotFoundException - if provided email does not exist
     */
    @Transactional
    public void resendOTP(String email, Long otpCode){
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User with the provided email cannot be found");
        }
        User user = userOptional.get();
        OTP otp = new OTP();
        otp.setCreatedAt(Date.from(Instant.now().plusMillis(MINUTES)));
        otp.setStatus(false);
        otp.setOtp(otpCode);
        otp.setUser(user);
        user.setOtp(otp);
        otpRepository.save(otp);
    }

}