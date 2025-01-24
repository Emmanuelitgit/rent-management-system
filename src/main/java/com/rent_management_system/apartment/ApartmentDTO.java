package com.rent_management_system.apartment;

import com.rent_management_system.components.UserDataDTO;
import com.rent_management_system.fileManager.ApartmentFile;
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
    public boolean isKitchenPart;
    public String description;
    public UserDataDTO user;
    public List<ApartmentFile> apartmentFiles;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
}