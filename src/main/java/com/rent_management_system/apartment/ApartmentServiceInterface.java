package com.rent_management_system.apartment;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApartmentServiceInterface {
    public ApartmentDTO createApartment(Apartment apartment, Long id, MultipartFile[] files);
    public List<ApartmentDTO> getApartmentList();
    public ApartmentDTO getApartmentById(Long id);
    public ApartmentDTO updateApartmentById(Apartment apartment, MultipartFile[] files,Long id);
    public void removeApartmentById(Long id);
}
