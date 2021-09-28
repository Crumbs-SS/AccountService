package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.dto.CustomerDeleteCredentials;
import com.crumbs.accountservice.service.DeletionService;
import com.crumbs.lib.entity.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/account-service")
public class DeletionController {

    private final DeletionService deletionService;

    @Autowired
    DeletionController(DeletionService deletionService) {
        this.deletionService = deletionService;
    }

    @PreAuthorize("hasAuthority('CUSTOMER') and #cred.username == authentication.principal")
    @DeleteMapping("/customers")
    public ResponseEntity<Object> deleteCustomer(@RequestBody @Validated CustomerDeleteCredentials cred) {
        deletionService.deleteCustomer(cred);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<UserDetails> deleteUser(@PathVariable Long userId){
        return new ResponseEntity<>(deletionService.deleteUser(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/drivers/{driverId}")
    public ResponseEntity<Object> deleteDriver(@PathVariable Long driverId){
        return new ResponseEntity<>(deletionService.deleteDriver(driverId), HttpStatus.OK);
    }
}

