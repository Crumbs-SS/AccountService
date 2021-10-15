package com.crumbs.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
public class UserDetailsUpdate {
    @NotNull @NotBlank @Size(min = 3, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Username can only contain letters and numbers.")
    private String username;

    @NotNull @NotBlank @Email @Size(min = 5, max = 50)
    private String email;

    @NotNull @NotBlank @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-z']*$", message = "Name can only contain letters and apostrophes.")
    private String firstName;

    @NotNull @NotBlank @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-z']*$", message = "Name can only contain letters and apostrophes.")
    private String lastName;

    @NotNull @NotBlank @Size(min = 10, max = 10)
    @Pattern(regexp = "^[0-9]*$", message = "Phone number can only contain numbers.")
    private String phone;
}
