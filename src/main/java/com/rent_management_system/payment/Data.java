package com.rent_management_system.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@lombok.Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    private String authorization_url;
    private String access_code;
    private String reference;
}
