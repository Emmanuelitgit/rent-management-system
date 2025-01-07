package com.rent_management_system.User;

import java.util.List;

public interface UserInterface {
    public UserDTO createUser(User user);
    List<UserDTO> getUsers();
}
