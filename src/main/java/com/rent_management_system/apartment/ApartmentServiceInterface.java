package com.rent_management_system.apartment;

import com.rent_management_system.apartmentAddress.ApartmentAddress;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ApartmentServiceInterface {
    public ApartmentDTO createApartment(Apartment apartment, Long id, MultipartFile mainFile, MultipartFile[] files, ApartmentAddress address) throws IOException;
    public List<ApartmentDTO> getApartmentList();
    public ApartmentDTO getApartmentById(Long id);
    public ApartmentDTO updateApartmentById(Apartment apartment, MultipartFile mainFile, MultipartFile[] files,Long id, ApartmentAddress address) throws IOException;
    public void removeApartmentById(Long id);
    public List<ApartmentDTO> getApartmentByUserId(Long id);
}
