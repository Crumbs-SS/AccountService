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
import com.crumbs.lib.repository.UserStatusRepository;
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
    private final UserStatusRepository userStatusRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    UpdateService(UserDetailsRepository userDetailsRepository, UserStatusRepository userStatusRepository, DriverRepository driverRepository, DriverStateRepository driverStateRepository,
                  ConfirmationTokenRepository confirmationTokenRepository, PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.driverRepository = driverRepository;
        this.driverStateRepository = driverStateRepository;
        this.userStatusRepository = userStatusRepository;
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
        String status = "REGISTERED";
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow();
        UserStatus userStatus = UserStatus.builder()
                .status(status)
                .build();

        userStatus = userStatusRepository.save(userStatus);
        setStatusForAllRoles(userStatus, enableUser, user);
        return userDetailsRepository.save(user);
    }

    private void setStatusForAllRoles(UserStatus userStatus, EnableUser enableUser, UserDetails user){
        if(enableUser.getCustomer() && user.getCustomer() != null)
            user.getCustomer().setUserStatus(userStatus);
        if(enableUser.getOwner() && user.getOwner() != null)
            user.getOwner().setUserStatus(userStatus);
        if(enableUser.getDriver() && user.getDriver() != null){
            user.getDriver().setState(DriverState.builder()
                    .state("CHECKED_OUT").build());
            user.getDriver().setUserStatus(userStatus);
        }
        if(enableUser.getAdmin() && user.getAdmin() != null)
            user.getAdmin().setUserStatus(userStatus);
    }
    public DriverState checkInDriver(String username){
        UserDetails user = userDetailsRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Driver driver = user.getDriver();
        if (null == driver) { throw new EntityNotFoundException(); }
        DriverState status = driverStateRepository.findById("AVAILABLE").get();
        driver.setState(status);
        driverRepository.save(driver);
        return status;
    }
    public DriverState checkOutDriver(String username){
        UserDetails user = userDetailsRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Driver driver = user.getDriver();
        if (null == driver) { throw new EntityNotFoundException(); }
        DriverState status = driverStateRepository.findById("CHECKED_OUT").get();
        driver.setState(status);
        driverRepository.save(driver);
        return status;
    }

    public void requestPasswordChange(String email) {
        Optional<UserDetails> possibleUser = userDetailsRepository.findByEmail(email);
        if (possibleUser.isPresent()) {
            UserDetails user = possibleUser.get();
            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user
            );

            confirmationTokenRepository.save(confirmationToken);
            String url = "https://api.crumbs-ss.link/email-service/password/" + user.getUsername();
            //emailDTO?
            String result = restTemplate.getForObject(url,String.class);
        }
    }

    public void changePassword(String password, String token) {
        Optional<ConfirmationToken> possibleToken = confirmationTokenRepository.findByToken(token);
        if (possibleToken.isPresent()) {
            ConfirmationToken confirmationToken = possibleToken.get();
            UserDetails user = confirmationToken.getUserDetails();
            user.setPassword(passwordEncoder.encode(password));
            userDetailsRepository.save(user);
        }
        else {
            // throw and exception probably
        }
    }
}
