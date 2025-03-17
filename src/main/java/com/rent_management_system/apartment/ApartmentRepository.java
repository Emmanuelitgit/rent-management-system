package com.rent_management_system.apartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    Optional<List<Apartment>> findApartmentByUser_Id(Long userId);

    @Query(value = "SELECT apartment.id, apartment.name, user_tb.first_name FROM apartment_tb apartment JOIN user_tb ON apartment.user_id = user_tb.id", nativeQuery = true)
    Optional<List<Object>> getApartmentsWithUsers(@Param("apartmentId") Long apartmentId);
}