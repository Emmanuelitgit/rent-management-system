package com.rent_management_system.fileManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MainFileRepository extends JpaRepository<MainFile, Long> {
    Optional<MainFile> findMainFileByApartment_Id(Long apartmentId);
}
