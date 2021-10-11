package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.CustomerRegistration;
import com.crumbs.accountservice.dto.DriverRegistration;
import com.crumbs.accountservice.dto.EmailDTO;
import com.crumbs.accountservice.dto.OwnerRegistration;
import com.crumbs.lib.entity.*;
import com.crumbs.accountservice.exception.EmailNotAvailableException;
import com.crumbs.accountservice.exception.ExistingUserInformationMismatchException;
import com.crumbs.accountservice.exception.UsernameNotAvailableException;
import com.crumbs.lib.repository.ConfirmationTokenRepository;
import com.crumbs.lib.repository.DriverStateRepository;
import com.crumbs.lib.repository.UserDetailsRepository;
import com.crumbs.lib.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional(rollbackFor = { Exception.class })
public class RegistrationService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserStatusRepository userStatusRepository;
    private final DriverStateRepository driverStateRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RegistrationService(UserDetailsRepository userDetailsRepository,
                                UserStatusRepository userStatusRepository,
                               DriverStateRepository driverStateRepository,
                               ConfirmationTokenRepository confirmationTokenRepository,
                               PasswordEncoder passwordEncoder,
                               RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.userDetailsRepository = userDetailsRepository;
        this.userStatusRepository = userStatusRepository;
        this.driverStateRepository = driverStateRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public long registerCustomer(CustomerRegistration cred) {
        UserDetails user = UserDetails.builder()
                .username(cred.getUsername()).firstName(cred.getFirstName()).lastName(cred.getLastName())
                .password(cred.getPassword()).email(cred.getEmail()).phone(cred.getPhone()).build();

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

        UserStatus status = userStatusRepository.getById("PENDING_REGISTRATION");
        Customer customer = Customer.builder().userDetails(user).userStatus(status).build();
        user.setCustomer(customer);
        user = userDetailsRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenRepository.save(confirmationToken);
        EmailDTO emailDTO = EmailDTO.builder().email(cred.getEmail()).name(cred.getFirstName()).token(token).build();
        String url = "https://api.crumbs-ss.link/email-service/confirmation/" + user.getUsername();
        String result = restTemplate.postForObject(url,emailDTO, String.class);

        return user.getId();
    }

    public long registerDriver(DriverRegistration cred) {
        UserDetails user = UserDetails.builder()
                .username(cred.getUsername()).firstName(cred.getFirstName()).lastName(cred.getLastName())
                .password(cred.getPassword()).email(cred.getEmail()).phone(cred.getPhone()).build();

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

        UserStatus status = userStatusRepository.getById("REGISTERED");
        DriverState state = driverStateRepository.getById("CHECKED_OUT");
        Driver driver = Driver.builder().userDetails(user).licenseId(cred.getLicenseId()).userStatus(status).state(state).build();
        user.setDriver(driver);
        user = userDetailsRepository.save(user);
        return user.getId();
    }

    public long registerOwner(OwnerRegistration cred) {

        UserDetails user = UserDetails.builder()
                .username(cred.getUsername()).firstName(cred.getFirstName()).lastName(cred.getLastName())
                .password(cred.getPassword()).email(cred.getEmail()).phone(cred.getPhone()).build();

        if (matchingUserExists(user)) {
            System.out.println("a match was found");
            user = userDetailsRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).orElseThrow();
            if (null != user.getOwner()) {
                // this user already has a owner association. complain
                throw new ExistingUserInformationMismatchException();
            }
        }
        else {
            System.out.println("no match found, encoding password for new user");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        UserStatus status = userStatusRepository.getById("REGISTERED");
        Owner owner = Owner.builder().userDetails(user).userStatus(status).build();
        user.setOwner(owner);
        user = userDetailsRepository.save(user);
        return user.getId();
    }

    private Boolean matchingUserExists(UserDetails newUser) {
        // check by email or username if user already exists
        Optional<UserDetails> userOpt = userDetailsRepository.findByUsernameOrEmail(newUser.getUsername(), newUser.getEmail());
        if (userOpt.isEmpty()) { return false; }

        System.out.println("old user present");
        UserDetails oldUser = userOpt.get();

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
