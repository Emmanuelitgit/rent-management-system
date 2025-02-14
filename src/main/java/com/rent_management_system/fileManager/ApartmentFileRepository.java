package com.rent_management_system.fileManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentFileRepository extends JpaRepository<ApartmentFile, Long> {
    Optional<List<ApartmentFile>> findApartmentFilesByApartment_Id(Long apartmentId);
}
