package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.dto.ChangePasswordDTO;
import com.crumbs.accountservice.dto.CustomerUpdate;
import com.crumbs.accountservice.dto.EnableUser;
import com.crumbs.accountservice.dto.UserDetailsUpdate;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.accountservice.service.UpdateService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/users/password/recover")
    public ResponseEntity<Object> changePassword(@RequestBody @Validated ChangePasswordDTO body) {
        updateService.changePassword(body.getPassword(), body.getConfirmationToken());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDetails> updateUser(
            @PathVariable Long userId,
            @RequestBody @Validated UserDetailsUpdate cred
    ){
        UserDetails user = updateService.updateUser(userId, cred);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<UserDetails> enableUser(
            @PathVariable Long userId,
            @RequestBody @Validated EnableUser enableUser
    ){
        UserDetails user = updateService.enableUser(userId, enableUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PutMapping("/drivers/checkIn/{id}")
    public ResponseEntity<Object> checkInDriver(@PathVariable Long id){
        return new ResponseEntity<>(updateService.checkInDriver(id), HttpStatus.OK);
    }

    @PutMapping("/drivers/checkOut/{id}")
    public ResponseEntity<Object> checkOutDriver(@PathVariable Long id){
        return new ResponseEntity<>(updateService.checkOutDriver(id), HttpStatus.OK);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<Object> requestPasswordChange(@PathVariable String email) {
        updateService.requestPasswordChange(email);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


}
