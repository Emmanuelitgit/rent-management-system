package com.rent_management_system.Payment;

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
