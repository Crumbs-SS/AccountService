package com.crumbs.accountservice.startup;

import com.crumbs.lib.entity.*;
import com.crumbs.lib.repository.DriverStateRepository;
import com.crumbs.lib.repository.UserDetailsRepository;
import com.crumbs.lib.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SeedDatabase implements ApplicationRunner {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserStatusRepository userStatusRepository;
    private final DriverStateRepository driverStateRepository;

    @Autowired
    SeedDatabase(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserDetailsRepository userDetailsRepository,
                 @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserStatusRepository userStatusRepository,
                 @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DriverStateRepository driverStateRepository,
                 PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.userStatusRepository = userStatusRepository;
        this.driverStateRepository = driverStateRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserDetails user;
        Admin admin;
        Customer customer;
        Driver driver;
        Owner owner;

        UserStatus status = userStatusRepository.getById("REGISTERED");
        DriverState state = driverStateRepository.getById("UNVALIDATED");


        user = UserDetails.builder().firstName("1").lastName("1")
                .username("user1").password(passwordEncoder.encode("123456")).email("1@1.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        user.setCustomer(customer);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
        user = null;

        user = UserDetails.builder().firstName("2").lastName("2")
                .username("user2").password(passwordEncoder.encode("123456")).email("2@2.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        driver = Driver.builder().userDetails(user).licenseId("54321").userStatus(status).state(state).build();
        user.setCustomer(customer);
        user.setDriver(driver);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
        user = null;

        user = UserDetails.builder().firstName("3").lastName("3")
                .username("user3").password(passwordEncoder.encode("123456")).email("3@3.com").phone("1234567890").build();
        driver = Driver.builder().userDetails(user).licenseId("54321").userStatus(status).state(state).build();
        user.setDriver(driver);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
        user = null;

        user = UserDetails.builder().firstName("4").lastName("4")
                .username("user4").password(passwordEncoder.encode("123456")).email("4@4.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        owner = Owner.builder().userDetails(user).userStatus(status).build();
        user.setCustomer(customer);
        user.setOwner(owner);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
        user = null;

        user = UserDetails.builder().firstName("jim").lastName("brower")
                .username("jbrower").password(passwordEncoder.encode("123456")).email("jim@brower.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        driver = Driver.builder().userDetails(user).licenseId("54321").userStatus(status).state(state).build();
        user.setCustomer(customer);
        user.setDriver(driver);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
        user = null;
    }
}
