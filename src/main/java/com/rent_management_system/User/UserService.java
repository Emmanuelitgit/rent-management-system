package com.rent_management_system.User;

import com.rent_management_system.Components.OTPComponent;
import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.Authentiication.OTP;
import com.rent_management_system.Authentiication.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService implements UserInterface {
    private final long MINUTES = TimeUnit.MINUTES.toMillis(5);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final OTPService otpVerificationService;
    private final OTPComponent otpComponent;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper, OTPService otpVerificationService, OTPComponent otpComponent) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
        this.otpVerificationService = otpVerificationService;
        this.otpComponent = otpComponent;
    }

    private OTP otpDetails(User user){
        OTP otp = new OTP();
        otp.setOtp(otpComponent.generateOTP());
        otp.setCreatedAt(Date.from(Instant.now().plusMillis(MINUTES)));
        otp.setUser(user);
        return otp;
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()){
            throw new NotFoundException("No data found");
        }
        return userDTOMapper.userDTOList(users);
    }

    @Override
    public UserDTO createUser(User user) {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()){
            throw new InvalidDataException("User Already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole().toUpperCase());
        OTP otpDetails = otpDetails(user);
        user.setOtp(otpDetails);
        boolean isOtpSent = this.otpComponent.sendOTP(user.getEmail(), otpDetails.getOtp());
        if (isOtpSent){
            userRepository.save(user);
            return UserDTOMapper.toDTO(user);
        }
       throw new InvalidDataException("invalid email");
    }

    public UserDTO getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        return UserDTOMapper.toDTO(user.get());
    }

    public UserDTO updateUserById(Long id, User user){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User not found");
        }
        User existingUser = userOptional.get();
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());

        return UserDTOMapper.toDTO(existingUser);
    }

    public void removeUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User not found ");
        }
        userRepository.deleteById(id);
    }
}
