package com.rent_management_system.FileManager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApartmentFileRepository extends JpaRepository<ApartmentFile, Long> {
    Optional<ApartmentFile> findApartmentFileByApartment_Id(Long apartmentId);
}
