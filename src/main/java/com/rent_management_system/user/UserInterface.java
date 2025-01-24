package com.rent_management_system.user;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public interface UserInterface {
    public UserDTO createUser(User user) throws MessagingException, IOException;
    List<UserDTO> getUsers();
}
