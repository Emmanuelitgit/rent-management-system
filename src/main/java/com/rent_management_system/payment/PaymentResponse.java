package com.rent_management_system.payment;

import lombok.*;

@lombok.Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private boolean status;
    private String message;
    private Data data;
}
