package com.rent_management_system.Apartment;

import com.rent_management_system.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "apartment_tb")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @NotBlank(message = "Name is required")
    public String name;
    @NotNull(message = "Number of bed rooms is required")
    public int bedrooms;
    @NotNull(message = "Number of bath rooms is required")
    public int bathrooms;
    @Enumerated(EnumType.STRING)
    public ApartmentStatus status;
    public String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @UpdateTimestamp
    @Column(updatable = false)
    public LocalDateTime created_at;
    @UpdateTimestamp
    public LocalDateTime updated_at;
}