package com.rent_management_system.Repositories;

import com.rent_management_system.Models.OTPVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPVerificationRepository extends JpaRepository<OTPVerification, Long> {
    Optional<OTPVerification> findOTPVerificationByUser_Id(Long userId);
}
