package com.rent_management_system.rentInfo;

import com.rent_management_system.apartment.Apartment;
import com.rent_management_system.apartment.ApartmentRepository;
import com.rent_management_system.apartment.ApartmentStatus;
import com.rent_management_system.exception.InvalidDataException;
import com.rent_management_system.exception.NotFoundException;
import com.rent_management_system.user.User;
import com.rent_management_system.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentInfoService implements RentInfoServiceInterface{

    private final RentInfoRepository rentInfoRepository;
    private final UserRepository userRepository;
    private final ApartmentRepository apartmentRepository;
    private final RentDTOMapper rentDTOMapper;

    @Autowired
    public RentInfoService(RentInfoRepository rentInfoRepository, UserRepository userRepository, ApartmentRepository apartmentRepository, RentDTOMapper rentDTOMapper) {
        this.rentInfoRepository = rentInfoRepository;
        this.userRepository = userRepository;
        this.apartmentRepository = apartmentRepository;
        this.rentDTOMapper = rentDTOMapper;
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to create rent info
     * @date 016-01-2025
     * @param: rentInfo, userId, apartmentId
     * @throws NotFoundException - if user or apartment is not found
     * @throws InvalidDataException - if apartment is occupied already
     * @return rentInfoDTO object
     */
    @Override
    @Transactional
    public RentInfoDTO createRentInfo(RentInfo rentInfo, Long userId, Long apartmentId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(apartmentId);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User not found");
        }
        if (apartmentOptional.isEmpty()){
            throw new NotFoundException("Apartment not found");
        }
        Apartment apartment = apartmentOptional.get();
        User user = userOptional.get();

        rentInfo.setApartment(List.of(apartment));
        rentInfo.setUser(user);
        user.setRentInfo(List.of(rentInfo));
        if (apartment.getStatus().equals(ApartmentStatus.OCCUPIED)){
            throw new InvalidDataException("Apartment already occupied");
        }
        apartment.setStatus(ApartmentStatus.OCCUPIED);
        apartmentRepository.save(apartment);
        rentInfoRepository.save(rentInfo);

        return RentDTOMapper.toDTO(rentInfo);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to remove rent info by id
     * @date 016-01-2025
     * @param: id
     * @throws NotFoundException - if rent info does not exist
     */
    @Override
    @Transactional
    public void removeRentInfo(Long id) {
        Optional<RentInfo> rentInfoOptional = rentInfoRepository.findById(id);
        if (rentInfoOptional.isEmpty()){
            throw new NotFoundException("Rent info not found");
        }
        RentInfo rentInfo = rentInfoOptional.get();
        if (rentInfo.getUser() != null){
            rentInfo.setUser(null);
        }

        rentInfoRepository.deleteById(id);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to get rent info by id
     * @date 016-01-2025
     * @param: id
     * @throws NotFoundException - if rent info does not exist
     * @return rentInfoDTO object
     */
    @Override
    @Transactional
    public RentInfoDTO getRentInfoById(Long id) {
        Optional<RentInfo> rentInfoOptional = rentInfoRepository.findById(id);
        if (rentInfoOptional.isEmpty()){
            throw new NotFoundException("Rent info not found");
        }
        RentInfo rentInfo = rentInfoOptional.get();
        return RentDTOMapper.toDTO(rentInfo);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to fetch all rent info
     * @date 016-01-2025
     * @throws NotFoundException - if no rent info is found
     * @return list of rentInfoDTO
     */
    @Override
    @Transactional
    public List<RentInfoDTO> getRentInfoList() {
        List<RentInfo> rentInfoList = rentInfoRepository.findAll();
        if (rentInfoList.isEmpty()){
            throw new NotFoundException("No rent info found");
        }
        return rentDTOMapper.rentInfoDTOList(rentInfoList);
    }

    @Override
    public RentInfo updateRentInfoById(RentInfo rentInfo, Long id) {
     return null;
    }
}
