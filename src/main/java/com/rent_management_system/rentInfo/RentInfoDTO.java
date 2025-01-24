package com.rent_management_system.rentInfo;

import com.rent_management_system.apartment.Apartment;
import com.rent_management_system.components.UserDataDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentInfoDTO {
    public Long id;
    public UserDataDTO user;
    public List<Apartment> apartmentList;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
