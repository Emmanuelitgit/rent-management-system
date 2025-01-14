package com.rent_management_system.User;

import com.rent_management_system.Apartment.Apartment;
import com.rent_management_system.RentInfo.RentInfo;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String role;
    public Long phone;
}
