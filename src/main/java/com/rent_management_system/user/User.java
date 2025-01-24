package com.rent_management_system.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rent_management_system.apartment.Apartment;
import com.rent_management_system.authentication.OTP;
import com.rent_management_system.payment.Payment;
import com.rent_management_system.rentInfo.RentInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    public String firstName;
    @Column
    @NotBlank(message = "Last name is required")
    public String lastName;
    @Column
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email provided")
    public String email;
    @Column
//    @NotNull(message = "phone number is required")
    public Long phone;
    @Column(columnDefinition = "varchar(255) default 'USER'")
    public String role;
    @Column
    @NotBlank(message = "Password is required")
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
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    public List<Payment> payment;
}