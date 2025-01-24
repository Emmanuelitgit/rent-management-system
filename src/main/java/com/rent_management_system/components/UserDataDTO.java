package com.rent_management_system.components;

import lombok.*;

/**
 * @auther Emmanuel Yidana
 * @description: a centralized class that represents the payload of user object we what to include in certain areas. eg user in apartment, user in rent info
 * @date 016-01-2025
 */
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
