package com.rent_management_system.Apartment;

import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.FileManager.ApartmentFile;
import com.rent_management_system.FileManager.ApartmentFileRepository;
import com.rent_management_system.User.User;
import com.rent_management_system.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ApartmentService implements ApartmentServiceInterface {

    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;
    private final ApartmentDTOMapper apartmentDTOMapper;
    private final ApartmentFileRepository apartmentFileRepository;
    private final String FILE_BASEURL_PROD = "https://rent-management-system-uyyb.onrender.com/";
    private final String FILE_BASEURL_DEV = " http://localhost:5000/";

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository, UserRepository userRepository, ApartmentDTOMapper apartmentDTOMapper, ApartmentFileRepository apartmentFileRepository) {
        this.apartmentRepository = apartmentRepository;
        this.userRepository = userRepository;
        this.apartmentDTOMapper = apartmentDTOMapper;
        this.apartmentFileRepository = apartmentFileRepository;
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to create apartment. we associate the apartment with the owner thus the user creating it
     * @date 016-01-2025
     * @param: id, apartment object
     * @throws NotFoundException- throws NotFoundException if user does not exist
     * @return apartmentDTO object
     */
     @Transactional
     @Override
     public ApartmentDTO createApartment(Apartment apartment, Long id, MultipartFile[] files) {
         Optional<User> userOptional = userRepository.findById(id);
         if (userOptional.isEmpty()) {
             throw new NotFoundException("User not found");
         }

         List<ApartmentFile> apartmentFiles = new ArrayList<>();

         for (MultipartFile filePayload : files) {
             ApartmentFile apartmentFile = new ApartmentFile();
             apartmentFile.setFile(FILE_BASEURL_DEV+filePayload.getOriginalFilename());
             apartmentFile.setApartment(apartment);
             apartmentFiles.add(apartmentFile);
             apartmentFileRepository.save(apartmentFile);
         }

         apartment.setApartmentFiles(apartmentFiles);

         User user = userOptional.get();
         apartment.setUser(user);
         user.getApartment().add(apartment);

         return ApartmentDTOMapper.toDTO(apartment);
     }


    /**
     * @auther Emmanuel Yidana
     * @description: A to fetch all apartments
     * @date 016-01-2025
     * @throws NotFoundException- throws NotFoundException if no apartment exist
     * @return list of apartments
     */
    @Override
    public List<ApartmentDTO> getApartmentList() {
        List<Apartment> apartments = apartmentRepository.findAll();
        if (apartments.isEmpty()){
            throw new NotFoundException("No apartment found");
        }
       return apartmentDTOMapper.apartmentDTOList(apartments);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to get apartment by id
     * @date 016-01-2025
     * @param: id
     * @throws NotFoundException- throws NotFoundException if apartment does not exist
     * @return apartment object
     */
    @Override
    @Transactional
    public ApartmentDTO getApartmentById(Long id) {
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty()){
            throw new NotFoundException("Apartment not found");
        }
        Apartment apartment = apartmentOptional.get();
        return ApartmentDTOMapper.toDTO(apartment);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to update apartment by id
     * @date 016-01-2025
     * @param: id, apartment object
     * @throws NotFoundException- throws NotFoundException if apartment does not exist
     * @return updated apartment object
     */
    @Override
    public ApartmentDTO updateApartmentById(Apartment payload, MultipartFile[] files, Long id) {
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty()){
            throw new NotFoundException("Apartment not found");
        }
        Optional<ApartmentFile> apartmentFileOptional = apartmentFileRepository.findApartmentFileByApartment_Id(apartmentOptional.get().getId());
        if (apartmentOptional.isEmpty()){
            throw new NotFoundException("Apartment file not found");
        }
//        ApartmentFile apartmentFiles = apartmentFileOptional.get().getFile();

        Apartment existingApartment = apartmentOptional.get();
        existingApartment.setName(payload.getName());
        existingApartment.setBathrooms(payload.getBathrooms());
        existingApartment.setBedrooms(payload.getBedrooms());
        existingApartment.setDescription(payload.getDescription());
        existingApartment.setStatus(payload.getStatus());


        apartmentRepository.save(existingApartment);
        return ApartmentDTOMapper.toDTO(existingApartment);

    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to remove apartment by id
     * @date 016-01-2025
     * @param: id
     * @throws NotFoundException- throws NotFoundException if apartment does not exist
     */
    @Override
    @Transactional
    public void removeApartmentById(Long id) {
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty()){
            throw new NotFoundException("Apartment not found");
        }
        Apartment apartment = apartmentOptional.get();

        User user = apartment.getUser();
        if (user != null){
            user.setApartment(null);
        }
        apartmentRepository.deleteById(id);
    }
}
