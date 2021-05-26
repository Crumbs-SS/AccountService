package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.CustomerRegistration;
import com.crumbs.accountservice.dto.DriverRegistration;
import com.crumbs.accountservice.entity.Customer;
import com.crumbs.accountservice.entity.Driver;
import com.crumbs.accountservice.entity.UserDetails;
import com.crumbs.accountservice.exception.EmailNotAvailableException;
import com.crumbs.accountservice.exception.ExistingUserInformationMismatchException;
import com.crumbs.accountservice.exception.UsernameNotAvailableException;
import com.crumbs.accountservice.repository.UserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(rollbackFor = { Exception.class })
public class RegistrationService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserDetailsRepository userDetailsRepository,
                               PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Integer registerCustomer(CustomerRegistration cred) {
        UserDetails user = UserDetails.builder()
                .username(cred.getUsername()).firstName(cred.getFirstName()).lastName(cred.getLastName())
                .password(cred.getPassword()).email(cred.getEmail()).build();

        if (matchingUserExists(user)) {
            System.out.println("a match was found");
            user = userDetailsRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).orElseThrow();
            if (null != user.getCustomer()) {
                // this user already has a customer association. complain
                throw new ExistingUserInformationMismatchException();
            }
        }
        else {
            System.out.println("no match found, encoding password for new user");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Customer customer = Customer.builder().userDetails(user).phone(cred.getPhone()).build();
        user.setCustomer(customer);
        user = userDetailsRepository.save(user);
        return user.getId();
    }

    public Integer registerDriver(DriverRegistration cred) {
        System.out.println(cred);

        UserDetails user = UserDetails.builder()
                .username(cred.getUsername()).firstName(cred.getFirstName()).lastName(cred.getLastName())
                .password(cred.getPassword()).email(cred.getEmail()).build();

        if (matchingUserExists(user)) {
            System.out.println("a match was found");
            user = userDetailsRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).orElseThrow();
            if (null != user.getDriver()) {
                // this user already has a customer association. complain
                throw new ExistingUserInformationMismatchException();
            }
        }
        else {
            System.out.println("no match found, encoding password for new user");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Driver driver = Driver.builder().userDetails(user).licenseId(cred.getLicenseId()).build();
        user.setDriver(driver);
        user = userDetailsRepository.save(user);
        return user.getId();
    }

    private Boolean matchingUserExists(UserDetails newUser) {
        // check by email or username if user already exists
        Optional<UserDetails> userOpt = userDetailsRepository.findByUsernameOrEmail(newUser.getUsername(), newUser.getEmail());
        if (userOpt.isEmpty()) { return false; }

        System.out.println("old user present");
        UserDetails oldUser = userOpt.get();
        System.out.println(oldUser);

        // user entered an existing email with the wrong associated username
        if (!newUser.getUsername().equals(oldUser.getUsername())) {
            System.out.println("username mismatch: old = " + oldUser.getUsername() + " new = " + newUser.getUsername());
            // That email is already in use
            throw new EmailNotAvailableException();
        }
        // user entered an existing username with the wrong associated email
        else if (!newUser.getEmail().equals(oldUser.getEmail())) {
            System.out.println("email mismatch: old = " + oldUser.getEmail() + " new = " + newUser.getEmail());
            // That username is not available
            throw new UsernameNotAvailableException();
        }
        else if (!passwordEncoder.matches(newUser.getPassword(), oldUser.getPassword())) {
            System.out.println("passwords dont match");
            // do something
            throw new ExistingUserInformationMismatchException();
        }
        else if (!newUser.getFirstName().equalsIgnoreCase(oldUser.getFirstName())) {
            System.out.println("first names dont match");
            // do something
            throw new ExistingUserInformationMismatchException();
        }
        else if (!newUser.getLastName().equalsIgnoreCase(oldUser.getLastName())) {
            System.out.println("last names dont match");
            // do something
            throw new ExistingUserInformationMismatchException();
        }
        return true;
    }
}
