package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.service.UserService;
import com.crumbs.lib.entity.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/users")
    public ResponseEntity<Page<UserDetails>> getUsers(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "asc") String orderBy,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size){

        Map<String, String> extras = Map.of("orderBy", orderBy, "sortBy", sortBy);
        PageRequest pageRequest = userService.getPageRequest(page, size, extras);

        Page<UserDetails> users = userService.getUsers(pageRequest, filter);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
