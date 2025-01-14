package com.rent_management_system.RentInfo;

import java.util.List;

public interface RentInfoServiceInterface {
    public RentInfoDTO createRentInfo(RentInfo rentInfo, Long userId, Long apartmentId);
    public void removeRentInfo(Long id);
    public RentInfoDTO getRentInfoById(Long id);
    public List<RentInfoDTO> getRentInfoList();
    public RentInfo updateRentInfoById(RentInfo rentInfo, Long id);
}
