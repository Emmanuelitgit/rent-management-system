package com.rent_management_system.apartment;
import com.rent_management_system.apartmentAddress.ApartmentAddress;
import com.rent_management_system.apartmentAddress.ApartmentAddressDTO;
import com.rent_management_system.components.UserDataDTO;
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
                apartment.getPrice(),
                apartment.getIsKitchenPart(),
                new ApartmentAddressDTO(
                        apartment.getApartmentAddress().getId(),
                        apartment.getApartmentAddress().getCity(),
                        apartment.getApartmentAddress().getStreetAddress(),
                        apartment.getApartmentAddress().getGpsAddress(),
                        apartment.getApartmentAddress().getRegion()
                ),
                apartment.getApartmentFiles(),
                apartment.getMainFile(),
                apartment.getDescription(),
                new UserDataDTO(
                        apartment.getUser().getId(),
                        apartment.getUser().getFirstName(),
                        apartment.getUser().getLastName(),
                        apartment.getUser().getEmail(),
                        apartment.getUser().getRole(),
                        apartment.getUser().getPhone()
                        )
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