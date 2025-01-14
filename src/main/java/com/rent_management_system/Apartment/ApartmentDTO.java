package com.rent_management_system.Apartment;

import com.rent_management_system.Components.UserDataDTO;
import lombok.*;

import java.time.LocalDateTime;

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
    public String description;
    public UserDataDTO user;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
}
