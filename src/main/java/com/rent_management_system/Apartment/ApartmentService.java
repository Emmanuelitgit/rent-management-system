package com.rent_management_system.Apartment;

import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.User.User;
import com.rent_management_system.User.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApartmentService implements ApartmentServiceInterface {

    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;
    private final ApartmentDTOMapper apartmentDTOMapper;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository, UserRepository userRepository, ApartmentDTOMapper apartmentDTOMapper) {
        this.apartmentRepository = apartmentRepository;
        this.userRepository = userRepository;
        this.apartmentDTOMapper = apartmentDTOMapper;
    }

    @Transactional
    @Override
    public ApartmentDTO createApartment(Apartment apartment, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();
        apartment.setUser(user);
        user.getApartment().add(apartment);
        return ApartmentDTOMapper.toDTO(apartment);
    }

    @Override
    public List<ApartmentDTO> getApartmentList() {
        List<Apartment> apartments = apartmentRepository.findAll();
        if (apartments.isEmpty()){
            throw new NotFoundException("No apartment found");
        }
       return apartmentDTOMapper.apartmentDTOList(apartments);
    }

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

    @Override
    public ApartmentDTO updateApartmentById(Apartment payload, Long id) {
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty()){
            throw new NotFoundException("Apartment not found");
        }
        Apartment apartment = apartmentOptional.get();
        apartment.setName(payload.getName());
        apartment.setBathrooms(payload.getBathrooms());
        apartment.setBedrooms(payload.getBedrooms());
        apartment.setDescription(payload.getDescription());
        apartment.setStatus(payload.getStatus());

        apartmentRepository.save(apartment);
        return ApartmentDTOMapper.toDTO(apartment);

    }

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
