package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.service.UserService;
import com.crumbs.lib.entity.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDetails> userById(@PathVariable int userId) {
        UserDetails user = userService.userById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
