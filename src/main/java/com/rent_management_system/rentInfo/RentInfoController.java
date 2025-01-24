package com.rent_management_system.rentInfo;

import com.rent_management_system.response.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class RentInfoController {
    private final RentInfoService rentInfoService;

    @Autowired
    public RentInfoController(RentInfoService rentInfoService) {
        this.rentInfoService = rentInfoService;
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to create rent info
     * @date 016-01-2025
     * @param: rentInfo object
     * @return rentInfoDTO object
     */
    @PostMapping("/create-rent")
    public ResponseEntity<Object> createRentInfo(@RequestBody RentInfo rentInfo, @RequestParam Long userId, @RequestParam Long apartmentId){
        log.info("In create rentInfo method:===========");
        RentInfoDTO rentData = rentInfoService.createRentInfo(rentInfo, userId, apartmentId);
        return ResponseHandler.responseBuilder("RentInfo created successfully", rentData, HttpStatus.CREATED);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to fetch all rent info
     * @date 016-01-2025
     * @return list of rentInfoDTO
     */
    @GetMapping("/rentInfo-list")
    public ResponseEntity<Object> getRentInfoList(){
        log.info("In get rentInfoList method:==========");
        List<RentInfoDTO> rentInfoList = rentInfoService.getRentInfoList();
        return ResponseHandler.responseBuilder("RentInfo list", rentInfoList, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to get rent info by id
     * @date 016-01-2025
     * @param: id
     * @return rentInfoDTO object
     */
    @GetMapping("/rentInfo/{id}")
    public ResponseEntity<Object> getRentInfoById(@PathVariable Long id){
        log.info("In get rentInfo method:============");
        RentInfoDTO rentInfoDTO = rentInfoService.getRentInfoById(id);
        return ResponseHandler.responseBuilder("RentInfo detail", rentInfoDTO, HttpStatus.OK);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to remove rent info by id
     * @date 016-01-2025
     * @param: id
     */
    @DeleteMapping("/remove-rentInfo/{id}")
    public ResponseEntity<Object> removeRentInfo(@PathVariable Long id){
        log.info("In remove rent info method:=========");
        rentInfoService.removeRentInfo(id);
        return ResponseHandler.responseBuilder("RentInfo deleted successfully", null, HttpStatus.OK);
    }
}