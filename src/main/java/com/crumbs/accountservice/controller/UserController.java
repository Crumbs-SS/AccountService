package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.dto.DriverDTO;
import com.crumbs.accountservice.service.UserService;
import com.crumbs.lib.entity.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/drivers")
    public ResponseEntity<Page<DriverDTO>> getRestaurants(
            @RequestParam(defaultValue = "") String searchString,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer page
    ){
        PageRequest pageRequest = userService.getPageRequest(page, pageSize, sortField, sortDirection);
        Page<DriverDTO> drivers = userService.getDrivers(pageRequest, searchString, status);

        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }
}
