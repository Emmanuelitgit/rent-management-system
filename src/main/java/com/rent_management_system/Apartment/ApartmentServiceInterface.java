package com.rent_management_system.Apartment;

import java.util.List;

public interface ApartmentServiceInterface {
    public ApartmentDTO createApartment(Apartment apartment, Long id);
    public List<ApartmentDTO> getApartmentList();
    public ApartmentDTO getApartmentById(Long id);
    public ApartmentDTO updateApartmentById(Apartment apartment,Long id);
    public void removeApartmentById(Long id);
}
