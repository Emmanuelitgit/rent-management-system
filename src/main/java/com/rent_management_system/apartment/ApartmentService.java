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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        // retrieving existing apartment data from the db. throw exception it does not exist
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty()){
            throw new NotFoundException("Apartment not found");
        }

        // retrieving existing apartment address data from the db. throw exception it does not exist
        Optional<ApartmentAddress> apartmentAddressOptional = apartmentAddressRepository
                .findById(apartmentOptional.get().getApartmentAddress().getId());
        if (apartmentAddressOptional.isEmpty()){
            throw new NotFoundException("Apartment address not found");
        }

        // retrieving existing apartment files  from the db. throw exception it does not exist
        Optional<List<ApartmentFile>> apartmentFileOptional = apartmentFileRepository
                .findApartmentFilesByApartment_Id(apartmentOptional.get().getId());
        if (apartmentFileOptional.isEmpty()){
            throw new NotFoundException("Apartment file not found");
        }

        // updating existing apartment details
        Apartment existingApartment = apartmentOptional.get();
        existingApartment.setName(apartment.getName());
        existingApartment.setBathrooms(apartment.getBathrooms());
        existingApartment.setBedrooms(apartment.getBedrooms());
        existingApartment.setDescription(apartment.getDescription());
        existingApartment.setStatus(apartment.getStatus());
        existingApartment.setIsKitchenPart(apartment.getIsKitchenPart());
//        existingApartment.setMainFile(profileNameProvider.getFilePropertyPath()+mainFile.getOriginalFilename());

        // updating existing apartment address
        ApartmentAddress existingApartmentAddress = apartmentAddressOptional.get();
        existingApartmentAddress.setApartment(existingApartment);
        existingApartmentAddress.setStreetAddress(apartmentAddress.getStreetAddress());
        existingApartmentAddress.setGpsAddress(apartmentAddress.getGpsAddress());
        existingApartmentAddress.setRegion(apartmentAddress.getRegion());
        existingApartmentAddress.setCity(apartmentAddress.getCity());


        // looping through existing and incoming files
        List<ApartmentFile> apartmentFiles = new ArrayList<>();
        for (MultipartFile filePayload : files) {
            List<ApartmentFile> existingApartmentFiles = apartmentFileOptional.get();
            for (ApartmentFile apartmentFile:existingApartmentFiles){
                apartmentFile.setFileName(profileNameProvider.getFilePropertyPath()+filePayload.getOriginalFilename());
                apartmentFile.setApartment(existingApartment);
                apartmentFiles.add(apartmentFile);
            }
        }
        // adding the updated files to existing apartment
        existingApartment.setApartmentFiles(apartmentFiles);

        // adding the updated apartment address to existing apartment
        existingApartment.setApartmentAddress(existingApartmentAddress);

        // saving the updated apartment
        apartmentRepository.save(existingApartment);

        saveFiles(files);
        saveFile(mainFile);

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


    private void saveFile(MultipartFile file) throws IOException {
        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File fileData = new File(STORAGE + File.separator + uniqueFileName);
        File uploadsDir = new File(STORAGE);
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs();
        }
        Files.copy(file.getInputStream(), fileData.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }

    private void saveFiles(MultipartFile[] files) throws IOException {
        for(MultipartFile file:files){
            String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File fileData = new File(STORAGE + File.separator + uniqueFileName);
            File uploadsDir = new File(STORAGE);
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
            }
            Files.copy(file.getInputStream(), fileData.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}