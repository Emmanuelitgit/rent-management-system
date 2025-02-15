package com.rent_management_system.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rent_management_system.apartment.Apartment;
import com.rent_management_system.authentication.OTP;
import com.rent_management_system.payment.Payment;
import com.rent_management_system.rentInfo.RentInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long id;
    @Column
    @NotBlank(message = "First name is required")
    @Schema(description = "provide your first name here", example = "Yidana")
    public String firstName;
    @Column
    @NotBlank(message = "Last name is required")
    @Schema(description = "enter your last name here", example = "Emmanuel")
    public String lastName;
    @Column
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email provided")
    @Schema(description = "enter email address here", example = "eyidana001@gmail.com")
    public String email;
    @Column
    @NotNull(message = "phone number is required")
    @Schema(description = "enter phone number here", example = "0597893082")
    public Long phone;
    @Column(columnDefinition = "varchar(255) default 'USER'")
    @Schema(description = "user role here", example = "USER")
    public String role;
    @Column
    @NotBlank(message = "Password is required")
    @Schema(description = "enter password here", example = "124655")
    public String password;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private OTP otp;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Apartment> apartment;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RentInfo> rentInfo;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<Payment> payment;
}