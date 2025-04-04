package com.rent_management_system.user;

import lombok.*;
import org.springframework.stereotype.Component;

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
