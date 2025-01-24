package com.rent_management_system.user;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDTOMapper {

    /**
     * @auther Emmanuel Yidana
     * @description: A method to map user object to userDTO object
     * @date 016-01-2025
     * @param: user object
     * @return userDTO object
     */
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

    /**
     * @auther Emmanuel Yidana
     * @description: A method to map list of users to list of userDTO
     * @date 016-01-2025
     * @param: list of users
     * @return list of userDTO
     */
    public List<UserDTO> userDTOList(List<User> users){
        return users.stream()
                .map(UserDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
