package com.rent_management_system.User;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDTOMapper {

    public static UserDTO toDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getPhone()
        );
    }

    public List<UserDTO> userDTOList(List<User> users){
        return users.stream()
                .map(UserDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
