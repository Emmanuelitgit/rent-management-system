package com.rent_management_system.RentInfo;

import com.rent_management_system.Apartment.Apartment;
import com.rent_management_system.Components.UserDataDTO;
import com.rent_management_system.User.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RentDTOMapper {

    public static RentInfoDTO toDTO(RentInfo rentInfo){
        return new RentInfoDTO(
                rentInfo.getId(),
                rentInfo.getStatus(),
                rentInfo.getPaymentMethod(),
                new UserDataDTO(
                        rentInfo .getUser().getId(),
                        rentInfo .getUser().getFirstName(),
                        rentInfo .getUser().getLastName(),
                        rentInfo .getUser().getEmail(),
                        rentInfo.getUser().getRole(),
                        rentInfo .getUser().getPhone()
                ),
                rentInfo.getApartment(),
                rentInfo.getCreatedAt(),
                rentInfo.getUpdatedAt()
        );
    }

    public List<RentInfoDTO> rentInfoDTOList(List<RentInfo> rentInfoList){
        return rentInfoList.stream()
                .map(RentDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
