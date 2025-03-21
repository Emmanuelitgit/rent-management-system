package com.rent_management_system.rentInfo;

import com.rent_management_system.components.UserDataDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RentDTOMapper {

    /**
     * @auther Emmanuel Yidana
     * @description: A method to map rentInfo object to rentInfoDTO object
     * @date 016-01-2025
     * @param: rentInfo object
     * @return rentInfoDTO object
     */
    public static RentInfoDTO toDTO(RentInfo rentInfo){
        return new RentInfoDTO(
                rentInfo.getId(),
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

    /**
     * @auther Emmanuel Yidana
     * @description: A method to map list of rentInfo to list of rentInfoDTO
     * @date 016-01-2025
     * @param: list of rentInfo
     * @return list of rentInfoDTO
     */
    public List<RentInfoDTO> rentInfoDTOList(List<RentInfo> rentInfoList){
        return rentInfoList.stream()
                .map(RentDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
