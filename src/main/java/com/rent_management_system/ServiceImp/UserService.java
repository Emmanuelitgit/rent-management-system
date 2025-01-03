package com.rent_management_system.ServiceImp;

import com.rent_management_system.DTO.UserDTO;
import com.rent_management_system.DTOMappers.UserDTOMapper;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.Models.User;
import com.rent_management_system.Repositories.UserRepository;
import com.rent_management_system.ServiceInterface.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
    }

    @Override
    public UserDTO createUser(User user) {
        if (user == null){
            throw new NotFoundException("No data provided");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole().toUpperCase());
        userRepository.save(user);
        return UserDTOMapper.toDTO(user);
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
