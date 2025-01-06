package com.rent_management_system.ServiceImp;

import com.rent_management_system.DTO.UserDTO;
import com.rent_management_system.DTOMappers.UserDTOMapper;
import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.Models.OTPVerification;
import com.rent_management_system.Models.User;
import com.rent_management_system.Repositories.UserRepository;
import com.rent_management_system.ServiceInterface.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements UserInterface {
    private final long MINUTES = TimeUnit.MINUTES.toMillis(10);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final OTPVerificationService otpVerificationService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper, OTPVerificationService otpVerificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
        this.otpVerificationService = otpVerificationService;
    }

    @Override
    public UserDTO createUser(User user) {
        if (user == null){
            throw new NotFoundException("No data provided");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole().toUpperCase());
        OTPVerification otpVerification = new OTPVerification();
        otpVerification.setOtp(1234);
        otpVerification.setCreatedAt(Date.from(Instant.now().plusMillis(MINUTES)));
        otpVerification.setUser(user);
        user.setOtpVerification(otpVerification);
        boolean isOtpSent = otpVerificationService.sendOTP(user.getEmail());
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
