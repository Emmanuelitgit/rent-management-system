package com.rent_management_system.apartmentAddress;

import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentAddressRepository extends JpaRepository<ApartmentAddress, Long> {
}