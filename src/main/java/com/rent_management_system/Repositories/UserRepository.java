package com.rent_management_system.Repositories;

import com.rent_management_system.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserByEmail(String email);
}
