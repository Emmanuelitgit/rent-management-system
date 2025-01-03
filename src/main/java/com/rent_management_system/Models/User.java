package com.rent_management_system.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    @Column(columnDefinition = "varchar(255) default 'USER'")
    @NotBlank(message = "Role is required")
    public String role;
    @Column
    @NotBlank(message = "Password is required")
    public String password;
}