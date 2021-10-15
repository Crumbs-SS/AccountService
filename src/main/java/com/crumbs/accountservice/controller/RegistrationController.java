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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account-service")
@PreAuthorize("isAuthenticated()")
public class RegistrationController {

    private final RegistrationService registrationService;
    private static final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    private static final String LOCATION = "Location";

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register/customer")
    public ResponseEntity<Object> registerCustomer(@RequestBody @Validated CustomerRegistration cred) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(EXPOSE_HEADERS, LOCATION);
        responseHeaders.set(LOCATION, "/customers/" + cred.getUsername());
        return new ResponseEntity<>(registrationService.registerCustomer(cred), responseHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register/driver")
    public ResponseEntity<Object> registerDriver(@RequestBody @Validated DriverRegistration cred) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(EXPOSE_HEADERS, LOCATION);
        responseHeaders.set(LOCATION, "/drivers/" + cred.getUsername());
        return new ResponseEntity<>(registrationService.registerDriver(cred), responseHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register/owner")
    public ResponseEntity<Object> registerOwner(@RequestBody @Validated OwnerRegistration cred) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(EXPOSE_HEADERS, LOCATION);
        responseHeaders.set(LOCATION, "/owners/" + cred.getUsername());
        return new ResponseEntity<>(registrationService.registerOwner(cred), responseHeaders, HttpStatus.CREATED);
    }
}
