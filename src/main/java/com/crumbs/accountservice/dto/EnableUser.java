package com.crumbs.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnableUser {

    private Boolean customer = false;
    private Boolean driver = false;
    private Boolean owner = false;
    private Boolean admin = false;
}
