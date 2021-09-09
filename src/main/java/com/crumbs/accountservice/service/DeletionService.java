package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.CustomerDeleteCredentials;
import com.crumbs.accountservice.dto.DriverDTO;
import com.crumbs.lib.entity.*;
import com.crumbs.lib.repository.DriverRepository;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;

@Service
@Transactional(rollbackFor = { Exception.class })
public class DeletionService {

    private final UserDetailsRepository userDetailsRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    DeletionService(UserDetailsRepository userDetailsRepository,
                    PasswordEncoder passwordEncoder,
                    DriverRepository driverRepository,
                    RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.driverRepository = driverRepository;
    }

    public void deleteCustomer(CustomerDeleteCredentials cred) {
        UserDetails user = userDetailsRepository.findByUsername(cred.getUsername()).orElseThrow(NoSuchElementException::new);
        if (!passwordEncoder.matches(cred.getPassword(), user.getPassword())) {
            throw new NoSuchElementException();
        }

        userDetailsRepository.delete(user);
    }

    public DriverDTO deleteDriver(Long driverId){
        Driver driver = driverRepository.findById(driverId).orElseThrow();

        driver.setUserStatus(UserStatus.builder().status("DELETED").build());
        driver.setState(DriverState.builder().state("UNVALIDATED").build());

        driverRepository.save(driver);

        restTemplate.put("http://localhost:8010/drivers/{driverId}/abandon",
                        null,
                        driver.getId());

        return DriverDTO.builder()
                .email(driver.getUserDetails().getEmail())
                .id(driver.getId())
                .firstName(driver.getUserDetails().getFirstName())
                .lastName(driver.getUserDetails().getLastName())
                .licenseId(driver.getLicenseId())
                .phone(driver.getUserDetails().getPhone())
                .state(driver.getState().getState())
                .username(driver.getUserDetails().getUsername())
                .build();
    }

    public UserDetails deleteUser(Long userId){
        String status = "DELETED";
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow();
        UserStatus userStatus = UserStatus.builder().status(status).build();

        setStatusForAllRoles(userStatus, user);

        return userDetailsRepository.save(user);
    }


    private void setStatusForAllRoles(UserStatus userStatus, UserDetails user){
        if(user.getCustomer() != null) {
            user.getCustomer().setUserStatus(userStatus);
            user.getCustomer().getOrders().forEach(order ->
                restTemplate.delete("http://localhost:8010/orders/{id}", order.getId()));
        }
        if(user.getOwner() != null)
            user.getOwner().setUserStatus(userStatus);
        if(user.getDriver() != null)
            deleteDriver(user.getDriver().getId());
        if(user.getAdmin() != null)
            user.getAdmin().setUserStatus(userStatus);
    }

}
