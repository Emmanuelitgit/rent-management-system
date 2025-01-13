package com.rent_management_system.Apartment;

import com.rent_management_system.User.User;
import com.rent_management_system.User.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
public class ApartmentDTO {
    public Long id;
    public String name;
    public int bedrooms;
    public int bathrooms;
    public ApartmentStatus status;
    public String description;
    public UserDTO user;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;

    public ApartmentDTO(Long id, String name, int bedrooms, int bathrooms, ApartmentStatus status, String description, UserDTO user, LocalDateTime created_at, LocalDateTime updated_at) {

        this.id = id;
        this.name = name;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.status = status;
        this.description = description;
        this.user = user;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
