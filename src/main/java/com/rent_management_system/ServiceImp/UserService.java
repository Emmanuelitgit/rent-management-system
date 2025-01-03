package com.rent_management_system.ServiceImp;

import com.rent_management_system.DTO.UserDTO;
import com.rent_management_system.DTOMappers.UserDTOMapper;
import com.rent_management_system.Models.User;
import com.rent_management_system.Repositories.UserRepository;
import com.rent_management_system.ServiceInterface.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserInterface {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(User user) {
        userRepository.save(user);
        return UserDTOMapper.toDTO(user);
    }
}
