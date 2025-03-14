package com.rent_management_system.apartmentAddress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rent_management_system.apartment.Apartment;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApartmentAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long id;
    @Column
    public String city;
    @Column
    public String streetAddress;
    @Column
    public String gpsAddress;
    @Column
    public String region;
    @OneToOne
    @JoinColumn(name = "apartment_id")
    @JsonIgnore
    private Apartment apartment;
}