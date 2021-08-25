package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.CustomerUpdate;
import com.crumbs.accountservice.dto.EnableUser;
import com.crumbs.accountservice.dto.UserDetailsUpdate;
import com.crumbs.accountservice.exception.EmailNotAvailableException;
import com.crumbs.accountservice.exception.UsernameNotAvailableException;
import com.crumbs.lib.entity.*;
import com.crumbs.lib.repository.ConfirmationTokenRepository;
import com.crumbs.lib.repository.DriverRepository;
import com.crumbs.lib.repository.DriverStateRepository;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = { Exception.class })
public class UpdateService {

    private final UserDetailsRepository userDetailsRepository;
    private final DriverRepository driverRepository;
    private final DriverStateRepository driverStateRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    UpdateService(UserDetailsRepository userDetailsRepository, DriverRepository driverRepository, DriverStateRepository driverStateRepository,
                  ConfirmationTokenRepository confirmationTokenRepository, PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.driverRepository = driverRepository;
        this.driverStateRepository = driverStateRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails updateCustomer(CustomerUpdate cred) {
        UserDetails user = userDetailsRepository.findByUsername(cred.getUsername()).orElseThrow(NoSuchElementException::new);
        user.setUsername(cred.getUsername());
        user.setEmail(cred.getEmail());
        user.setFirstName(cred.getFirstName());
        user.setLastName(cred.getLastName());
        user = userDetailsRepository.save(user);

        return user;
    }

    public UserDetails updateUser(Long userId, UserDetailsUpdate cred){
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow();

        user.setFirstName(cred.getFirstName());
        user.setLastName(cred.getLastName());
        user.setPhone(cred.getPhone());

        boolean usernameTaken =  userDetailsRepository.findByUsername(cred.getUsername())
                .orElse(user).getId().equals(user.getId());
        boolean emailTaken = userDetailsRepository.findByEmail(cred.getEmail())
                .orElse(user).getId().equals(user.getId());

        if( !usernameTaken || !emailTaken ){
            throw !usernameTaken ? new UsernameNotAvailableException() : new EmailNotAvailableException();
        }

        user.setUsername(cred.getUsername());
        user.setEmail(cred.getEmail());


        return userDetailsRepository.save(user);
    }

    public UserDetails enableUser(Long userId, EnableUser enableUser){
        String status = "ACTIVE";
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow();
        UserStatus userStatus = UserStatus.builder()
                .status(status)
                .build();

        setStatusForAllRoles(userStatus, enableUser, user);
        return userDetailsRepository.save(user);
    }

    private void setStatusForAllRoles(UserStatus userStatus, EnableUser enableUser, UserDetails user){
        if(enableUser.getCustomer() && user.getCustomer() != null)
            user.getCustomer().setUserStatus(userStatus);
        if(enableUser.getOwner() && user.getOwner() != null)
            user.getOwner().setUserStatus(userStatus);
        if(enableUser.getDriver() && user.getDriver() != null)
            user.getDriver().setUserStatus(userStatus);
        if(enableUser.getAdmin() && user.getAdmin() != null)
            user.getAdmin().setUserStatus(userStatus);
    }
    public DriverState checkInDriver(Long id){
        Driver driver = driverRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        DriverState status = driverStateRepository.findById("AVAILABLE").get();
        driver.setState(status);
        driverRepository.save(driver);
        return status;
    }
    public DriverState checkOutDriver(Long id){
        Driver driver = driverRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        DriverState status = driverStateRepository.findById("CHECKED_OUT").get();
        driver.setState(status);
        driverRepository.save(driver);
        return status;
    }

    public void requestPasswordChange(String email) {
        Optional<UserDetails> possibleUser = userDetailsRepository.findByEmail(email);
        if (possibleUser.isPresent()) {
            // make a call to email service to send the password email
            System.out.println("sending email to " + email);
            UserDetails user = possibleUser.get();
            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user
            );

            confirmationTokenRepository.save(confirmationToken);
            String url = "http://localhost:8100/email/" + email + "/token/" + token;
            String result = restTemplate.getForObject(url,String.class);

        }
        else {
            System.out.println("not sending an email");
            // if the email doesnt match an existing user, we want to keep that information from the user
            // since it could lead to security leaks
        }
    }

    public void changePassword(String password, String token) {
        Optional<ConfirmationToken> possibleToken = confirmationTokenRepository.findByToken(token);
        if (possibleToken.isPresent()) {
            ConfirmationToken confirmationToken = possibleToken.get();
            UserDetails user = confirmationToken.getUserDetails();
            System.out.println("test1");
            user.setPassword(passwordEncoder.encode(password));
            System.out.println("test2");
            userDetailsRepository.save(user);
            System.out.println("test3");
        }
        else {
            // throw and exception probably
        }
    }
}
