package com.crumbs.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDeleteCredentials {
    @NotNull @NotBlank @Size(min = 3, max = 20)
    String username;
    @NotNull @NotBlank @Size(min = 6, max = 200)
    String password;
}
