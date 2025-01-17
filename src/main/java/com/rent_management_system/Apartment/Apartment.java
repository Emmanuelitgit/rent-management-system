package com.rent_management_system.Apartment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rent_management_system.FileManager.ApartmentFile;
import com.rent_management_system.RentInfo.RentInfo;
import com.rent_management_system.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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
    @UpdateTimestamp
    @Column(updatable = false)
    public LocalDateTime created_at;
    @UpdateTimestamp
    public LocalDateTime updated_at;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    @ManyToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RentInfo> rentInfo;
    @OneToMany(mappedBy = "apartment",cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    public List<ApartmentFile> apartmentFiles;
}