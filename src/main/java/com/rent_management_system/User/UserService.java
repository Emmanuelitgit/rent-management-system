package com.rent_management_system.User;

import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.OTP.OTP;
import com.rent_management_system.OTP.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService implements UserInterface {
    private final long MINUTES = TimeUnit.MINUTES.toMillis(10);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final OTPService otpVerificationService;
    private final com.rent_management_system.Components.OTP otp;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper, OTPService otpVerificationService, com.rent_management_system.Components.OTP otp) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
        this.otpVerificationService = otpVerificationService;
        this.otp = otp;
    }

    @Override
    public UserDTO createUser(User user) {
        if (user == null){
            throw new NotFoundException("No data provided");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole().toUpperCase());
        OTP otp = new OTP();
        otp.setOtp(1234);
        otp.setCreatedAt(Date.from(Instant.now().plusMillis(MINUTES)));
        otp.setUser(user);
        user.setOtp(otp);
        boolean isOtpSent = this.otp.sendOTP(user.getEmail());
        if (isOtpSent){
            userRepository.save(user);
            return UserDTOMapper.toDTO(user);
        }
       throw new InvalidDataException("invalid email");
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()){
            throw new NotFoundException("No data found");
        }
        return userDTOMapper.userDTOList(users);
    }
}
