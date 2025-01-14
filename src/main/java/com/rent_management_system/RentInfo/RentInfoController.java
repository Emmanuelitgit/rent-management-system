package com.rent_management_system.RentInfo;

import com.rent_management_system.Response.ResponseHandler;
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

    @PostMapping("/create-rent")
    public ResponseEntity<Object> createRentInfo(@RequestBody RentInfo rentInfo, @RequestParam Long userId, @RequestParam Long apartmentId){
        log.info("In create rentInfo method:===========");
        RentInfoDTO rentData = rentInfoService.createRentInfo(rentInfo, userId, apartmentId);
        return ResponseHandler.responseBuilder("RentInfo created successfully", rentData, HttpStatus.CREATED);
    }

    @GetMapping("/rentInfo-list")
    public ResponseEntity<Object> getRentInfoList(){
        log.info("In get rentInfoList method:==========");
        List<RentInfoDTO> rentInfoList = rentInfoService.getRentInfoList();
        return ResponseHandler.responseBuilder("RentInfo list", rentInfoList, HttpStatus.OK);
    }

    @GetMapping("/rentInfo/{id}")
    public ResponseEntity<Object> getRentInfoById(@PathVariable Long id){
        log.info("In get rentInfo method:============");
        RentInfoDTO rentInfoDTO = rentInfoService.getRentInfoById(id);
        return ResponseHandler.responseBuilder("RentInfo detail", rentInfoDTO, HttpStatus.OK);
    }

    @DeleteMapping("/remove-rentInfo/{id}")
    public ResponseEntity<Object> removeRentInfo(@PathVariable Long id){
        log.info("In remove rent info method:=========");
        rentInfoService.removeRentInfo(id);
        return ResponseHandler.responseBuilder("RentInfo deleted successfully", null, HttpStatus.OK);
    }
}