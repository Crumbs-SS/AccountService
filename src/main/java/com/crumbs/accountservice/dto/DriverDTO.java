package com.crumbs.accountservice.dto;

import lombok.Data;

@Data
public class DriverDTO {
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String licenseId;

    private String state;
}
