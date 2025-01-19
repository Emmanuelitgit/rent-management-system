package com.rent_management_system.Payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRequest {
    @NotBlank(message = "Email is required")
    public String email;
    @NotNull(message = "Amount is required")
    public String amount;
}
