package com.rent_management_system.Apartment;

import com.rent_management_system.User.UserDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApartmentDTOMapper {

    public static ApartmentDTO toDTO(Apartment apartment){
        return new ApartmentDTO(
                apartment.getId(),
                apartment.getName(),
                apartment.getBedrooms(),
                apartment.getBathrooms(),
                apartment.getStatus(),
                apartment.getDescription(),
                new UserDTO(
                        apartment.getUser().getId(),
                        apartment.getUser().getFirstName(),
                        apartment.getUser().getLastName(),
                        apartment.getUser().getEmail(),
                        apartment.getUser().getRole()
                        ),
                apartment.getCreated_at(),
                apartment.updated_at
        );
    }

    public List<ApartmentDTO> apartmentDTOList(List<Apartment> apartments){
        return apartments.stream()
                .map(ApartmentDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}