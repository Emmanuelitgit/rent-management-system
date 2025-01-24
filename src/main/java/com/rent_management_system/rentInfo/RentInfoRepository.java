package com.rent_management_system.rentInfo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentInfoRepository extends JpaRepository<RentInfo, Long> {
    Optional<RentInfo> findRentInfoByUser_Id(Long userId);
    Optional<RentInfo> findRentInfoByUser_Email(@NotBlank(message = "Email is required") @Email(message = "Invalid email provided") String userEmail);
}