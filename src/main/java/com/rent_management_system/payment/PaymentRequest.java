package com.rent_management_system.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.Data;

import java.util.Map;

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
    public Map<String, Object> metadata;
}
