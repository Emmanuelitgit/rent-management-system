package com.rent_management_system.payment;

import com.rent_management_system.components.UserDataDTO;
import lombok.*;
import lombok.Data;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    public Long id;
    public int amount;
    public String status;
    public String currency;
    public String channel;
    public String transactionDate;
    public String reference;
    public UserDataDTO user;
//    public RentInfoDataDTO rentInfo;
}
