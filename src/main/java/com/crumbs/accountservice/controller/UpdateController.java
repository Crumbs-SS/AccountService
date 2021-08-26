package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.dto.ChangePasswordDTO;
import com.crumbs.accountservice.dto.CustomerUpdate;
import com.crumbs.accountservice.dto.EnableUser;
import com.crumbs.accountservice.dto.UserDetailsUpdate;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.accountservice.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("isAuthenticated()")
public class UpdateController {

    private final UpdateService updateService;

    @Autowired
    UpdateController(UpdateService updateService) {
        this.updateService = updateService;
    }

    @PreAuthorize("hasAuthority('CUSTOMER') and #cred.username == authentication.principal")
    @PutMapping("/customers")
    public ResponseEntity<Object> updateCustomer(@RequestBody @Validated CustomerUpdate cred) {
        UserDetails user = updateService.updateCustomer(cred);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/users/password/recover")
    public ResponseEntity<Object> changePassword(@RequestBody @Validated ChangePasswordDTO body) {
        updateService.changePassword(body.getPassword(), body.getConfirmationToken());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDetails> updateUser(@PathVariable Long userId,
                                                  @RequestBody @Validated UserDetailsUpdate cred) {
        UserDetails user = updateService.updateUser(userId, cred);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<UserDetails> enableUser(@PathVariable Long userId,
                                                  @RequestBody @Validated EnableUser enableUser) {
        UserDetails user = updateService.enableUser(userId, enableUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DRIVER') and #username == authentication.principal")
    @PutMapping("/drivers/checkIn/{username}")
    public ResponseEntity<Object> checkInDriver(@PathVariable String username) {
        return new ResponseEntity<>(updateService.checkInDriver(username), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DRIVER') and #username == authentication.principal")
    @PutMapping("/drivers/checkOut/{username}")
    public ResponseEntity<Object> checkOutDriver(@PathVariable String username) {
        return new ResponseEntity<>(updateService.checkOutDriver(username), HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/users/email/{email}")
    public ResponseEntity<Object> requestPasswordChange(@PathVariable String email) {
        updateService.requestPasswordChange(email);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
