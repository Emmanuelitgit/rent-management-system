package com.rent_management_system.user;

import com.rent_management_system.authentication.OTPComponent;
import com.rent_management_system.exception.InvalidDataException;
import com.rent_management_system.exception.NotFoundException;
import com.rent_management_system.authentication.OTP;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(force = true)
public class UserService implements UserInterface {
    private final long MINUTES = TimeUnit.MINUTES.toMillis(5);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final OTPComponent otpComponent;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper, OTPComponent otpComponent) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
        this.otpComponent = otpComponent;
    }

    // a method that takes User object and mapped it to the OTP object
    private OTP otpDetails(User user){
        OTP otp = new OTP();
        otp.setOtp(otpComponent.generateOTP());
        otp.setCreatedAt(Date.from(Instant.now().plusMillis(MINUTES)));
        otp.setUser(user);
        return otp;
    }

    /**
     * @auther Emmanuel Yidana
     * @description A method to fetch all users
     * @date 016-01-2025
     * @throws NotFoundException - throws a not found exception if no user exist
     * @return UserDTO
     */
    @Override
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()){
            throw new NotFoundException("No data found");
        }
        return userDTOMapper.userDTOList(users);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to create a new user
     * @date 016-01-2025
     * @param user object
     * @throws InvalidDataException- throws InvalidDataException if user already exist
     * @return UserDTO
     */
    @Override
    public UserDTO createUser(User user){
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()){
            throw new InvalidDataException("User Already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole().toUpperCase());
        OTP otpDetails = otpDetails(user);
        user.setOtp(otpDetails);
        otpComponent.sendOTP(user.getEmail(), otpDetails.getOtp(), user.getFirstName());
        userRepository.save(user);
        return UserDTOMapper.toDTO(user);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to return a user by id
     * @date 016-01-2025
     * @param: id
     * @throws NotFoundException - throws NotFoundException if user does not exist
     * @return UserDTO
     */
    public UserDTO getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        return UserDTOMapper.toDTO(user.get());
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to update a user by id
     * @date 016-01-2025
     * @param id, User object
     * @throws NotFoundException- throws NotFoundException if user does not exist
     * @return UserDTO
     */
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

    /**
     * @auther Emmanuel Yidana
     * @description: A method to remove a user by id
     * @date 016-01-2025
     * @param: id
     * @throws NotFoundException- throws NotFoundException if user does not exist
     */
    public void removeUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User not found ");
        }
        userRepository.deleteById(id);
    }
}