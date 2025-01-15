package com.rent_management_system.Apartment;

import com.rent_management_system.Response.ResponseHandler;
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
    public ResponseEntity<Object> getApartmentList(){
        log.info("In fetch apartments method:============");
        List<ApartmentDTO> apartmentDTOList = apartmentService.getApartmentList();
        return ResponseHandler.responseBuilder("Apartment list", apartmentDTOList, HttpStatus.OK);
    }

    private String getFileString(MultipartFile[] files){
        var fileName = "";
        for (MultipartFile file : files){
            fileName = file.getOriginalFilename();
        }
        return fileName;
    }

    @PostMapping("/create-apartment/{id}")
    public ResponseEntity<Object> createApartment(
            @RequestParam("name") String name,
            @RequestParam("bedrooms") int bedrooms,
            @RequestParam("bathrooms") int bathrooms,
            @RequestParam("status") ApartmentStatus status,
            @RequestParam("file") MultipartFile[] filePayload,
            @RequestParam("description") String description,
            @PathVariable Long id
    ) throws IOException {

        for(MultipartFile file:filePayload){
            String STORAGE = "uploads";
            File fileData = new File(STORAGE + File.separator + file.getOriginalFilename());
            File uploadsDir = new File(STORAGE);
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
            }
            log.info("FILE CREATED");
            Files.copy(file.getInputStream(), fileData.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        log.info("File String{}", getFileString(filePayload));

        Apartment apartment = new Apartment();
        apartment.setName(name);
        apartment.setBedrooms(bedrooms);
        apartment.setBathrooms(bathrooms);
        apartment.setStatus(status);
//        apartment.setFile(filePayload);
        apartment.setDescription(description);
        log.info("In create apartment method:===========");
        ApartmentDTO apartmentDTO = apartmentService.createApartment(apartment, id);
        return ResponseHandler.responseBuilder("Apartment created successfully", apartmentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/apartment/{id}")
    public ResponseEntity<Object> getApartmentById(@PathVariable Long id){
        log.info("In get apartment by id:============");
        ApartmentDTO apartmentDTO = apartmentService.getApartmentById(id);
        return ResponseHandler.responseBuilder("Apartment details", apartmentDTO, HttpStatus.OK);
    }

    @PostMapping("/update-apartment/{id}")
    public ResponseEntity<Object> updateApartmentById(@RequestBody Apartment apartment, @PathVariable Long id){
        ApartmentDTO apartmentDTO = apartmentService.updateApartmentById(apartment, id);
        return ResponseHandler.responseBuilder("Apartment updated successfully", apartmentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/remove-apartment/{id}")
    public ResponseEntity<Object> removeApartmentById(@PathVariable Long id){
        apartmentService.removeApartmentById(id);
        return ResponseHandler.responseBuilder("Apartment deleted successfully", null, HttpStatus.OK);
    }
}


