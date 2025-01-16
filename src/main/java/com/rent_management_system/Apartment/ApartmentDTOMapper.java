package com.rent_management_system.Apartment;
import com.rent_management_system.Components.UserDataDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ApartmentDTOMapper {

    /**
     * @auther Emmanuel Yidana
     * @description: A method to map apartment object to apartmentDTO object
     * @date 016-01-2025
     * @param: apartment object
     * @return apartmentDTO object
     */
    public static ApartmentDTO toDTO(Apartment apartment){
        return new ApartmentDTO(
                apartment.getId(),
                apartment.getName(),
                apartment.getBedrooms(),
                apartment.getBathrooms(),
                apartment.getStatus(),
                apartment.getFile(),
                apartment.getDescription(),
                new UserDataDTO(
                        apartment.getUser().getId(),
                        apartment.getUser().getFirstName(),
                        apartment.getUser().getLastName(),
                        apartment.getUser().getEmail(),
                        apartment.getUser().getRole(),
                        apartment.getUser().getPhone()
                        ),
                apartment.getCreated_at(),
                apartment.updated_at
        );
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to map list of apartments to list of apartmentDTO
     * @date 016-01-2025
     * @param: list of apartments
     * @return list of apartmentDTO
     */
    public List<ApartmentDTO> apartmentDTOList(List<Apartment> apartments){
        return apartments.stream()
                .map(ApartmentDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}