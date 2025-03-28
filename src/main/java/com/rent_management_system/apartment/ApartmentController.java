package com.rent_management_system.apartment;

import com.rent_management_system.apartmentAddress.ApartmentAddress;
import com.rent_management_system.exception.NotFoundException;
import com.rent_management_system.response.ResponseHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api")
public class ApartmentController {
    private final ApartmentService apartmentService;
    private final Apartment apartment = new Apartment();
    private final ApartmentAddress apartmentAddress = new ApartmentAddress();
    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentController(ApartmentService apartmentService, ApartmentRepository apartmentRepository, ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/apartment-list")
    public ResponseEntity<Object> getApartmentList(HttpServletResponse response){
        log.info("In fetch apartments method:============");
        List<ApartmentDTO> apartmentDTOList = apartmentService.getApartmentList();
        return ResponseHandler.responseBuilder("Apartment list", apartmentDTOList, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to create an apartment
     * @date 016-01-2025.
     * @param: id, name, bedrooms, bathrooms, bathrooms, status, file, description
     * @return apartment object
     */
    @PostMapping("/create-apartment/{userId}")
    public ResponseEntity<Object> createApartment(
            @RequestParam("name") String name,
            @RequestParam("bedrooms") int bedrooms,
            @RequestParam("bathrooms") int bathrooms,
            @RequestParam("price") int price,
            @RequestParam("isKitchenPart") int isKitchenPart,
            @RequestParam("additionalFiles") MultipartFile[] subFiles,
            @RequestParam("mainFile") MultipartFile mainFile,
            @RequestParam("city") String city,
            @RequestParam("region") String region,
            @RequestParam("gpsAddress") String gpsAddress,
            @RequestParam("streetAddress") String streetAddress,
            @RequestParam("description") String description,
            @PathVariable Long userId
    ) throws IOException {

        log.info("In create apartment method:===========");

        apartment.setName(name);
        apartment.setBedrooms(bedrooms);
        apartment.setBathrooms(bathrooms);
        apartment.setStatus(ApartmentStatus.AVAILABLE);
        apartment.setIsKitchenPart(isKitchenPart);
        apartment.setPrice(price);
        apartment.setDescription(description);

        apartmentAddress.setGpsAddress(gpsAddress);
        apartmentAddress.setStreetAddress(streetAddress);
        apartmentAddress.setRegion(region);
        apartmentAddress.setCity(city);

        ApartmentDTO apartmentDTO = apartmentService.createApartment(apartment, userId, mainFile, subFiles, apartmentAddress);
        log.info("apartment created successfully:===========");
        return ResponseHandler.responseBuilder("Apartment created successfully", apartmentDTO, HttpStatus.CREATED);
    }

    /**
     * @auther Emmanuel Yidana
     * @description A method to get apartment by property owner id
     * @date 17-02-2025.
     * @param: id
     * @return apartment object
     */
    @GetMapping("/apartment/property-owner/{userId}")
    public ResponseEntity<Object> getApartmentByUserId(@PathVariable Long userId){
        log.info("In get apartment by user id==========");
        List<ApartmentDTO> apartmentDTO = apartmentService.getApartmentByUserId(userId);
        return ResponseHandler.responseBuilder("Apartment details", apartmentDTO, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description A method to get apartment by id
     * @date 016-01-2025.
     * @param: id
     * @return apartment object
     */
    @GetMapping("/apartment/{id}")
    public ResponseEntity<Object> getApartmentById(@PathVariable Long id){
        log.info("In get apartment by id==========");
        ApartmentDTO apartmentDTO = apartmentService.getApartmentById(id);
        return ResponseHandler.responseBuilder("Apartment details", apartmentDTO, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to update apartment by id
     * @date 016-01-2025
     * @param: id, apartment object
     * @return updated apartment object
     */
    @PostMapping("/update-apartment/{id}")
    public ResponseEntity<Object> updateApartmentById(
            @RequestParam("name") String name,
            @RequestParam("bedrooms") int bedrooms,
            @RequestParam("bathrooms") int bathrooms,
            @RequestParam("price") int price,
            @RequestParam("isKitchenPart") int isKitchenPart,
            @RequestParam("additionalFiles") MultipartFile[] subFiles,
            @RequestParam("mainFile") MultipartFile mainFile,
            @RequestParam("city") String city,
            @RequestParam("region") String region,
            @RequestParam("gpsAddress") String gpsAddress,
            @RequestParam("streetAddress") String streetAddress,
            @RequestParam("description") String description,
            @PathVariable Long id
    ) throws IOException {

        log.info("In update apartment by ID method:============");
        log.info("additionalfiles:======{}", Arrays.toString(subFiles));
        apartment.setName(name);
        apartment.setBedrooms(bedrooms);
        apartment.setBathrooms(bathrooms);
        apartment.setStatus(ApartmentStatus.AVAILABLE);
        apartment.setIsKitchenPart(isKitchenPart);
        apartment.setPrice(price);
        apartment.setDescription(description);

        apartmentAddress.setGpsAddress(gpsAddress);
        apartmentAddress.setStreetAddress(streetAddress);
        apartmentAddress.setRegion(region);
        apartmentAddress.setCity(city);

        ApartmentDTO apartmentDTO = apartmentService.updateApartmentById(apartment, mainFile, subFiles, id, apartmentAddress);
        log.info("Apartment updated successfully:============");
        return ResponseHandler.responseBuilder("Apartment updated successfully", apartmentDTO, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to remove apartment by id
     * @date 016-01-2025
     * @param: id
     * @return ResponseEntity with null object
     */
    @DeleteMapping("/remove-apartment/{id}")
    public ResponseEntity<Object> removeApartmentById(@PathVariable Long id) throws IOException {
        apartmentService.removeApartmentById(id);
        return ResponseHandler.responseBuilder("Apartment deleted successfully", null, HttpStatus.OK);
    }

    // just testing custom queries
    @GetMapping("/get-apartments")
    public Optional<List<Object>> getApartmentsWithUsers(@RequestParam("apartmentId") Long apartmentId){
        Optional<List<Object>> apartments = apartmentRepository.getApartmentsWithUsers(apartmentId);
        ApartmentResponse apartmentResponse = modelMapper.map(apartments, ApartmentResponse.class);
        if (apartments.isEmpty()){
            throw new NotFoundException("not found");
        }
        return apartments;
    }
}