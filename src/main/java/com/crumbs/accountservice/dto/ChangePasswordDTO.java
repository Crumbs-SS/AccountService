package com.crumbs.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Builder
public class ChangePasswordDTO {
    @NotNull @NotBlank @Size(min = 6, max = 200)
    String password;
    @NotNull @NotBlank
    String confirmationToken;
}