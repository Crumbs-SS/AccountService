package com.crumbs.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class LoginCredentials {
    @NotNull @NotBlank @Size(min = 3, max = 20)
    String username;
    @NotNull @NotBlank @Size(min = 6, max = 200)
    String password;
    @NotNull @NotBlank @Pattern(regexp = "^(customer)|(driver)|(owner)|(admin)$")
    String role;
}
