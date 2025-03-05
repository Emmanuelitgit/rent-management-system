package com.rent_management_system.apartment;

import com.rent_management_system.apartmentAddress.ApartmentAddress;
import com.rent_management_system.apartmentAddress.ApartmentAddressRepository;
import com.rent_management_system.components.ProfileNameProvider;
import com.rent_management_system.exception.NotFoundException;
import com.rent_management_system.fileManager.*;
import com.rent_management_system.user.User;
import com.rent_management_system.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Slf4j
@Service
public class ApartmentService implements ApartmentServiceInterface {

    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;
    private final ApartmentDTOMapper apartmentDTOMapper;
    private final ApartmentFileRepository apartmentFileRepository;
    private final MainFileRepository mainFileRepository;
    private final ProfileNameProvider profileNameProvider;
    private final String STORAGE = "uploads";
    private final ApartmentAddressRepository apartmentAddressRepository;
    private final GoogleDriveService googleDriveService;
    private final String GOOGLE_STORAGE_PATH = "https://drive.google.com/uc?id=";

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository, UserRepository userRepository, ApartmentDTOMapper apartmentDTOMapper, ApartmentFileRepository apartmentFileRepository, ProfileNameProvider profileNameProvider, ApartmentAddressRepository apartmentAddressRepository, GoogleDriveService googleDriveService, MainFileRepository mainFileRepository) {
        this.apartmentRepository = apartmentRepository;
        this.userRepository = userRepository;
        this.apartmentDTOMapper = apartmentDTOMapper;
        this.apartmentFileRepository = apartmentFileRepository;
        this.profileNameProvider = profileNameProvider;
        this.apartmentAddressRepository = apartmentAddressRepository;
        this.googleDriveService = googleDriveService;
        this.mainFileRepository = mainFileRepository;
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
     public ApartmentDTO createApartment(Apartment apartment, Long userId, MultipartFile mainFilePayload,
                                         MultipartFile[] files, ApartmentAddress apartmentAddress) throws IOException {
         Optional<User> userOptional = userRepository.findById(userId);
         if (userOptional.isEmpty()) {
             throw new NotFoundException("User not found");
         }

         List<ApartmentFile> apartmentFiles = new ArrayList<>();

         for (MultipartFile filePayload : files) {
             ApartmentFile apartmentFile = new ApartmentFile();
             String fileId = googleDriveService.uploadFile(filePayload);
             apartmentFile.setFileName(GOOGLE_STORAGE_PATH+fileId);
             apartmentFile.setFileId(fileId);
             apartmentFile.setFileType(filePayload.getContentType());
             apartmentFile.setApartment(apartment);
             apartmentFiles.add(apartmentFile);
         }

         // setting files here
         apartment.setApartmentFiles(apartmentFiles);
         String mainFileId = googleDriveService.uploadFile(mainFilePayload);
         MainFile mainFile = new MainFile();
         mainFile.setFileName(GOOGLE_STORAGE_PATH+mainFileId);
         mainFile.setFileType(mainFilePayload.getContentType());
         mainFile.setFileId(mainFileId);
         mainFile.setApartment(apartment);
         apartment.setMainFile(mainFile);

         apartment.setApartmentAddress(apartmentAddress);
         apartmentAddress.setApartment(apartment);

         User user = userOptional.get();
         user.getApartment().add(apartment);
         apartment.setUser(user);
         userRepository.save(user);

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
     * @description: A method to get apartment by user id
     * @date 17-02-2025
     * @param: id
     * @throws NotFoundException- throws NotFoundException if apartment does not exist
     * @return apartment object
     */
    @Override
    @Transactional
    public List<ApartmentDTO> getApartmentByUserId(Long id) {
        Optional<List<Apartment>> apartmentOptional = apartmentRepository.findApartmentByUser_Id(id);
        if (apartmentOptional.isEmpty()){
            throw new NotFoundException("Apartment not found");
        }
        List<Apartment> apartments = apartmentOptional.get();
        if(apartments.isEmpty()){
            throw new NotFoundException("Apartment not found");
        }
        return apartmentDTOMapper.apartmentDTOList(apartments);
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
    @Transactional
    public ApartmentDTO updateApartmentById(Apartment apartment, MultipartFile mainFile, MultipartFile[] files, Long id, ApartmentAddress apartmentAddress) throws IOException {
        // Retrieve existing apartment
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty()) {
            throw new NotFoundException("Apartment not found");
        }

        // Retrieve existing apartment address
        Optional<ApartmentAddress> apartmentAddressOptional = apartmentAddressRepository
                .findById(apartmentOptional.get().getApartmentAddress().getId());
        if (apartmentAddressOptional.isEmpty()) {
            throw new NotFoundException("Apartment address not found");
        }

        // Retrieve existing apartment files
        Optional<List<ApartmentFile>> apartmentFileOptional = apartmentFileRepository
                .findApartmentFilesByApartment_Id(apartmentOptional.get().getId());

        // Get existing files (if available)
        List<ApartmentFile> existingApartmentFiles = apartmentFileOptional.orElse(new ArrayList<>());

        // Update apartment details
        Apartment existingApartment = apartmentOptional.get();
        existingApartment.setName(apartment.getName());
        existingApartment.setBathrooms(apartment.getBathrooms());
        existingApartment.setBedrooms(apartment.getBedrooms());
        existingApartment.setDescription(apartment.getDescription());
        existingApartment.setStatus(apartment.getStatus());
        existingApartment.setIsKitchenPart(apartment.getIsKitchenPart());

        // Update apartment address
        ApartmentAddress existingApartmentAddress = apartmentAddressOptional.get();
        existingApartmentAddress.setApartment(existingApartment);
        existingApartmentAddress.setStreetAddress(apartmentAddress.getStreetAddress());
        existingApartmentAddress.setGpsAddress(apartmentAddress.getGpsAddress());
        existingApartmentAddress.setRegion(apartmentAddress.getRegion());
        existingApartmentAddress.setCity(apartmentAddress.getCity());

        // Ensure apartment files are updated correctly
        List<ApartmentFile> apartmentFiles = new ArrayList<>();
        Iterator<MultipartFile> fileIterator = Arrays.asList(files).iterator();
        Iterator<ApartmentFile> existingFileIterator = existingApartmentFiles.iterator();

        // Update existing files (if possible)
        while (fileIterator.hasNext() && existingFileIterator.hasNext()) {
            MultipartFile filePayload = fileIterator.next();
            ApartmentFile apartmentFile = existingFileIterator.next();

            String updatedFileId = googleDriveService.updateFileById(apartmentFile.getFileId(), filePayload);
            apartmentFile.setFileId(updatedFileId);
            apartmentFile.setFileType(filePayload.getContentType());
            apartmentFile.setFileName(GOOGLE_STORAGE_PATH + updatedFileId);
            apartmentFile.setApartment(existingApartment);

            apartmentFiles.add(apartmentFile);
        }

        // Set updated files
        existingApartment.setApartmentFiles(apartmentFiles);
        existingApartment.setApartmentAddress(existingApartmentAddress);

        // Save updated apartment
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
    public void removeApartmentById(Long id) throws IOException {
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty()){
            throw new NotFoundException("Apartment not found");
        }
        Optional<List<ApartmentFile>> apartmentFileOptional = apartmentFileRepository
                .findApartmentFilesByApartment_Id(apartmentOptional.get().getId());
        if (apartmentFileOptional.isEmpty()){
            throw new NotFoundException("apartment file not found");
        }
        Optional<MainFile> mainFileOptional = mainFileRepository.findMainFileByApartment_Id(apartmentOptional.get().getId());
        if (mainFileOptional.isEmpty()){
            throw new NotFoundException("no main file found");
        }
        MainFile mainFile = mainFileOptional.get();
        List<ApartmentFile> apartmentFiles = apartmentFileOptional.get();
        Apartment apartment = apartmentOptional.get();

        User user = apartment.getUser();
        if (user != null){
            user.setApartment(null);
        }

        for (ApartmentFile apartmentFile:apartmentFiles){
            googleDriveService.removeFileById(apartmentFile.getFileId());
        }
        googleDriveService.removeFileById(mainFileOptional.get().getFileId());

        apartmentRepository.deleteById(id);
    }


//    private void saveFile(MultipartFile file) throws IOException {
//        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        File fileData = new File(STORAGE + File.separator + uniqueFileName);
//        File uploadsDir = new File(STORAGE);
//        if (!uploadsDir.exists()) {
//            uploadsDir.mkdirs();
//        }
//        Files.copy(file.getInputStream(), fileData.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//    }
//
//    private void saveFiles(MultipartFile[] files) throws IOException {
//        for(MultipartFile file:files){
//            String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            File fileData = new File(STORAGE + File.separator + uniqueFileName);
//            File uploadsDir = new File(STORAGE);
//            if (!uploadsDir.exists()) {
//                uploadsDir.mkdirs();
//            }
//            Files.copy(file.getInputStream(), fileData.toPath(), StandardCopyOption.REPLACE_EXISTING);
//        }
//    }
}