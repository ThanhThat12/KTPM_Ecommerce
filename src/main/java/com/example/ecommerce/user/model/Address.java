package com.example.ecommerce.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String recipientName;
    private String phone;
    private String street;
    private String ward;
    private String district;
    private String city;
    private boolean defaultAddress;
}
