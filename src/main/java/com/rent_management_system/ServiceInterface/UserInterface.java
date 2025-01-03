package com.rent_management_system.ServiceInterface;

import com.rent_management_system.DTO.UserDTO;
import com.rent_management_system.Models.User;

public interface UserInterface {
    public UserDTO createUser(User user);
}
