package com.rent_management_system.Repositories;

import com.rent_management_system.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
