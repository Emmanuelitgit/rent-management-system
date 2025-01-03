package com.rent_management_system.DTOMappers;

import com.rent_management_system.DTO.UserDTO;
import com.rent_management_system.Models.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTOMapper {

    public static UserDTO toDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public List<UserDTO> userDTOList(List<User> users){
        return users.stream()
                .map(UserDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
