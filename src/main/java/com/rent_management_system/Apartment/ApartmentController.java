package com.rent_management_system.Apartment;

import com.rent_management_system.Response.ResponseHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApartmentController {
    private final ApartmentService apartmentService;

    @Autowired
    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
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
    @PostMapping("/create-apartment/{id}")
    public ResponseEntity<Object> createApartment(
            @RequestParam("name") String name,
            @RequestParam("bedrooms") int bedrooms,
            @RequestParam("bathrooms") int bathrooms,
            @RequestParam("price") int price,
            @RequestParam("isKitchenPart") boolean isKitchenPart,
            @RequestParam("status") ApartmentStatus status,
            @RequestParam("file") MultipartFile[] filePayload,
            @RequestParam("description") String description,
            @PathVariable Long id
    ) throws IOException {
        saveFile(filePayload);
        Apartment apartment = new Apartment();
        apartment.setName(name);
        apartment.setBedrooms(bedrooms);
        apartment.setBathrooms(bathrooms);
        apartment.setStatus(status);
        apartment.setKitchenPart(isKitchenPart);
        apartment.setPrice(price);
        apartment.setDescription(description);
        log.info("In create apartment method:===========");
        ApartmentDTO apartmentDTO = apartmentService.createApartment(apartment, id, filePayload);
        return ResponseHandler.responseBuilder("Apartment created successfully", apartmentDTO, HttpStatus.CREATED);
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
        log.info("In get apartment by id:{}");
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
            @RequestParam("isKitchenPart") boolean isKitchenPart,
            @RequestParam("status") ApartmentStatus status,
            @RequestParam("file") MultipartFile[] filePayload,
            @RequestParam("description") String description,
            @PathVariable Long id
    ) throws IOException {
        saveFile(filePayload);
        Apartment apartment = new Apartment();
        apartment.setName(name);
        apartment.setBedrooms(bedrooms);
        apartment.setBathrooms(bathrooms);
        apartment.setStatus(status);
        apartment.setKitchenPart(isKitchenPart);
        apartment.setPrice(price);
        apartment.setDescription(description);
        ApartmentDTO apartmentDTO = apartmentService.updateApartmentById(apartment, filePayload, id);
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
    public ResponseEntity<Object> removeApartmentById(@PathVariable Long id){
        apartmentService.removeApartmentById(id);
        return ResponseHandler.responseBuilder("Apartment deleted successfully", null, HttpStatus.OK);
    }

    private void saveFile(MultipartFile[] files) throws IOException {
        for(MultipartFile file:files){
            String STORAGE = "uploads";
            File fileData = new File(STORAGE + File.separator + file.getOriginalFilename());
            File uploadsDir = new File(STORAGE);
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
            }
            Files.copy(file.getInputStream(), fileData.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}