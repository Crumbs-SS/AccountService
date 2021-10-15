package com.crumbs.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class CustomerUpdate {
    @NotNull @NotBlank @Size(min = 3, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Username can only contain letters and numbers.")
    String username;

    @NotNull @NotBlank @Email @Size(min = 5, max = 50)
    String email;

    @NotNull @NotBlank @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-z']*$", message = "Name can only contain letters and apostrophes.")
    String firstName;

    @NotNull @NotBlank @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-z']*$", message = "Name can only contain letters and apostrophes.")
    String lastName;
}
