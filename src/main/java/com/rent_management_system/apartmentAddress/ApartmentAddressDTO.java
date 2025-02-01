package com.rent_management_system.apartmentAddress;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentAddressDTO {
    public Long id;
    public String city;
    public String streetAddress;
    public String gpsAddress;
    public String region;
}
