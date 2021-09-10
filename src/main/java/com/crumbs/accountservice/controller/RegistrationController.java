package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.dto.CustomerRegistration;
import com.crumbs.accountservice.dto.DriverRegistration;
import com.crumbs.accountservice.dto.OwnerRegistration;
import com.crumbs.accountservice.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAuthenticated()")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/customers/register")
    public ResponseEntity<Object> registerCustomer(@RequestBody @Validated CustomerRegistration cred) {
        long userId = registrationService.registerCustomer(cred);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "Location");
        responseHeaders.set("Location", "/customers/" + cred.getUsername());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/drivers/register")
    public ResponseEntity<Object> registerDriver(@RequestBody @Validated DriverRegistration cred) {
        long userId = registrationService.registerDriver(cred);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "Location");
        responseHeaders.set("Location", "/drivers/" + cred.getUsername());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/owners/register")
    public ResponseEntity<Object> registerOwner(@RequestBody @Validated OwnerRegistration cred) {
        long userId = registrationService.registerOwner(cred);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "Location");
        responseHeaders.set("Location", "/owners/" + cred.getUsername());
        return new ResponseEntity<>(userId, responseHeaders, HttpStatus.CREATED);
    }
}
