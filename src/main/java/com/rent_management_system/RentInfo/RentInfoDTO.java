package com.rent_management_system.RentInfo;

import com.rent_management_system.Apartment.Apartment;
import com.rent_management_system.Components.UserDataDTO;
import com.rent_management_system.User.User;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentInfoDTO {
    public Long id;
    public PaymentStatus paymentStatus;
    public PaymentMethod paymentMethod;
    public UserDataDTO user;
    public List<Apartment> apartmentList;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
