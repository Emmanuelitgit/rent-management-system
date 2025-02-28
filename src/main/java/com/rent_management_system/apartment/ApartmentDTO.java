package com.rent_management_system.apartment;

import com.rent_management_system.apartmentAddress.ApartmentAddress;
import com.rent_management_system.apartmentAddress.ApartmentAddressDTO;
import com.rent_management_system.components.UserDataDTO;
import com.rent_management_system.fileManager.ApartmentFile;
import com.rent_management_system.fileManager.MainFile;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentDTO {
    public Long id;
    public String name;
    public int bedrooms;
    public int bathrooms;
    public ApartmentStatus status;
    public int price;
    public int isKitchenPart;
    public ApartmentAddressDTO apartmentAddress;
    public List<ApartmentFile> apartmentFiles;
    public MainFile mainFile;
    public String description;
    public UserDataDTO user;
}