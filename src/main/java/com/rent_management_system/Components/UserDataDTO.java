package com.rent_management_system.Components;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String role;
    public Long phone;
}
