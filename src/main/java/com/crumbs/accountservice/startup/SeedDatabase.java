package com.crumbs.accountservice.startup;

import com.crumbs.lib.entity.*;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SeedDatabase implements ApplicationRunner {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    SeedDatabase(UserDetailsRepository userDetailsRepository,
                 PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserDetails user;
        Admin admin;
        Customer customer;
        Driver driver;
        Owner owner;

        user = UserDetails.builder().firstName("1").lastName("1")
                .username("user1").password(passwordEncoder.encode("123456")).email("1@1.com").build();
        customer = Customer.builder().userDetails(user).phone("1234567890").build();
        user.setCustomer(customer);
        userDetailsRepository.save(user);

        user = UserDetails.builder().firstName("2").lastName("2")
                .username("user2").password(passwordEncoder.encode("123456")).email("2@2.com").build();
        customer = Customer.builder().userDetails(user).phone("1234567890").build();
        driver = Driver.builder().userDetails(user).licenseId("54321").build();
        user.setCustomer(customer);
        user.setDriver(driver);
        userDetailsRepository.save(user);

        user = UserDetails.builder().firstName("jim").lastName("brower")
                .username("jbrower").password(passwordEncoder.encode("123456")).email("jim@brower.com").build();
        customer = Customer.builder().userDetails(user).phone("1234567890").build();
        driver = Driver.builder().userDetails(user).licenseId("54321").build();
        user.setCustomer(customer);
        user.setDriver(driver);
        userDetailsRepository.save(user);

        user = UserDetails.builder().firstName("3").lastName("3")
                .username("user3").password(passwordEncoder.encode("123456")).email("3@3.com").build();
        driver = Driver.builder().userDetails(user).licenseId("54321").build();
        user.setDriver(driver);
        userDetailsRepository.save(user);

        user = UserDetails.builder().firstName("4").lastName("4")
                .username("user4").password(passwordEncoder.encode("123456")).email("4@4.com").build();
        customer = Customer.builder().userDetails(user).phone("1234567890").build();
        owner = Owner.builder().userDetails(user).phone("1234567890").build();
        user.setCustomer(customer);
        user.setOwner(owner);
        userDetailsRepository.save(user);
    }
}
