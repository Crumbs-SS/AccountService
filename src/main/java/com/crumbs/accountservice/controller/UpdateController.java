package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.dto.CustomerUpdate;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.accountservice.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateController {

    private final UpdateService updateService;

    @Autowired
    UpdateController(UpdateService updateService) {
        this.updateService = updateService;
    }

    @PutMapping("/customers")
    public ResponseEntity<Object> updateCustomer(@RequestBody @Validated CustomerUpdate cred) {
        UserDetails user = updateService.updateCustomer(cred);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
