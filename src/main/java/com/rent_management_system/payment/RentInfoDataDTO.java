package com.rent_management_system.payment;

import com.rent_management_system.rentInfo.PaymentMethod;
import com.rent_management_system.rentInfo.PaymentStatus;
import lombok.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentInfoDataDTO {
    private Long id;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
