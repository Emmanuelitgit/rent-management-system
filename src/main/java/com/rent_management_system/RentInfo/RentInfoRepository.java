package com.rent_management_system.RentInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentInfoRepository extends JpaRepository<RentInfo, Long> {
    Optional<RentInfo> findRentInfoByUser_Id(Long userId);
}