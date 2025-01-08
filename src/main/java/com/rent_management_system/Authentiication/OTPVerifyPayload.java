package com.rent_management_system.Authentiication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class OTPVerifyPayload {
    @NotBlank(message = "Email cannot be null or blank")
    public String email;
    @NotNull(message = "OTP cannot be null or blank")
    public Long otp;
}
