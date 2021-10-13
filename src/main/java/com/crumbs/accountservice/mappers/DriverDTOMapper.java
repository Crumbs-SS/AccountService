package com.crumbs.accountservice.mappers;

import com.crumbs.accountservice.dto.DriverDTO;
import com.crumbs.lib.entity.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverDTOMapper {

    public DriverDTO getDriverDTO(Driver driver){
        return DriverDTO.builder()
                .email(driver.getUserDetails().getEmail())
                .id(driver.getId())
                .firstName(driver.getUserDetails().getFirstName())
                .lastName(driver.getUserDetails().getLastName())
                .licenseId(driver.getLicenseId())
                .phone(driver.getUserDetails().getPhone())
                .state(driver.getState().getState())
                .username(driver.getUserDetails().getUsername())
                .build();
    }
}
