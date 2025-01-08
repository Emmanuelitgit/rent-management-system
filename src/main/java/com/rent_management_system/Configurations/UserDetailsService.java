package com.rent_management_system.Configurations;

import com.rent_management_system.Exception.UnAuthorizedException;
import com.rent_management_system.User.User;
import com.rent_management_system.User.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Slf4j
@Configuration
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findUserByEmail(username);
        if (user.isPresent()){
            User userData = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userData.getEmail())
                    .password(userData.getPassword())
                    .roles(getRoles(userData))
                    .build();
        } else {
            throw new UnAuthorizedException("Username not found");
        }
    }

    public String[] getRoles(User user){
        if (user.getEmail().isEmpty()){
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}
