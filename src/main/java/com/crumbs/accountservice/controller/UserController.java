package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.dto.DriverDTO;
import com.crumbs.accountservice.service.UserService;
import com.crumbs.accountservice.startup.SeedDatabase;
import com.crumbs.lib.entity.DriverRating;
import com.crumbs.lib.entity.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/account-service")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/owners/{username}")
    public ResponseEntity<Long> ownerExists(@PathVariable String username) {
        return new ResponseEntity<>(userService.ownerExists(username), HttpStatus.OK);
    }

    // only admins should be able to access userIds
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/id/{userId}")
    public ResponseEntity<UserDetails> userById(@PathVariable int userId) {
        UserDetails user = userService.userById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // this endpoint exposes information from other roles of a user, should be changed
    @PreAuthorize("#username == authentication.principal")
    @GetMapping("/users/{username}")
    public ResponseEntity<UserDetails> userByUsername(@PathVariable String username) {
        UserDetails user = userService.userByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserDetails>> getUsers(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "") String filterBy,
            @RequestParam(defaultValue = "asc") String orderBy,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size) {
        Map<String, String> extras = Map.of("orderBy", orderBy, "sortBy", sortBy);
        PageRequest pageRequest = userService.getPageRequest(page, size, extras);
        Page<UserDetails> users = userService.getUsers(query, pageRequest, filterBy);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('DRIVER') and #username == authentication.principal")
    @GetMapping("/drivers/status/{username}")
    public ResponseEntity<String> getDriverStatus(@PathVariable String username) {
        return new ResponseEntity<>(userService.getDriverStatus(username), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DRIVER') and #username == authentication.principal")
    @GetMapping("/drivers/pay/{username}")
    public ResponseEntity<Float> getDriverPay(@PathVariable String username) {
        return new ResponseEntity<>(userService.getDriverPay(username), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DRIVER') and #username == authentication.principal")
    @GetMapping("/drivers/rating/{username}")
    public ResponseEntity<Double> getDriverAverageRating(@PathVariable String username) {
        return new ResponseEntity<>(userService.getDriverAverageRating(username), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DRIVER') and #username == authentication.principal")
    @GetMapping("/drivers/ratings/{username}")
    public ResponseEntity<List<DriverRating>> getDriverRatings(@PathVariable String username) {
        return new ResponseEntity<>(userService.getDriverRatings(username), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/drivers")
    public ResponseEntity<Page<DriverDTO>> getDrivers(
            @RequestParam(defaultValue = "") String searchString,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer page) {
        PageRequest pageRequest = userService.getPageRequest(page, pageSize, sortField, sortDirection);
        Page<DriverDTO> drivers = userService.getDrivers(pageRequest, searchString, status);
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/drivers/{username}")
    public ResponseEntity<Object> checkIfDriverIsAvailable(@PathVariable String username) {
        return new ResponseEntity<>(userService.checkIfDriverIsAvailable(username), HttpStatus.OK);
    }

    @GetMapping("/seed")
    public void seedDatabase(){
        SeedDatabase.run();
    }
}
