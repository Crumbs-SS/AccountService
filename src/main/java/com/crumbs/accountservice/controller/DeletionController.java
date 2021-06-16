package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.dto.CustomerDeleteCredentials;
import com.crumbs.accountservice.service.DeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeletionController {

    private final DeletionService deletionService;

    @Autowired
    DeletionController(DeletionService deletionService) {
        this.deletionService = deletionService;
    }

    @DeleteMapping("/customers")
    public ResponseEntity<Object> deleteCustomer(@RequestBody @Validated CustomerDeleteCredentials cred) {
        deletionService.deleteCustomer(cred);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}

