package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.CustomerDeleteCredentials;
import com.crumbs.accountservice.dto.DriverDTO;
import com.crumbs.accountservice.mappers.DriverDTOMapper;
import com.crumbs.accountservice.utils.ApiUrl;
import com.crumbs.lib.entity.*;
import com.crumbs.lib.repository.DriverRepository;
import com.crumbs.lib.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.NoSuchElementException;

import static com.crumbs.accountservice.utils.RestTemplateUtils.getHeaders;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = { Exception.class })
public class DeletionService {

    private final UserDetailsRepository userDetailsRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final DriverDTOMapper driverDTOMapper;

    public Customer deleteCustomer(CustomerDeleteCredentials cred) {
        UserDetails user = userDetailsRepository.findByUsername(cred.getUsername()).orElseThrow(NoSuchElementException::new);
        if (!passwordEncoder.matches(cred.getPassword(), user.getPassword())) {
            throw new NoSuchElementException();
        }

        user.getCustomer().setUserStatus(UserStatus.builder()
                .status("DELETED").build());

        userDetailsRepository.save(user);

        return user.getCustomer();
    }

    public DriverDTO deleteDriver(Long driverId, String token){
        Driver driver = driverRepository.findById(driverId).orElseThrow();

        driver.setUserStatus(UserStatus.builder().status("DELETED").build());
        driver.setState(DriverState.builder().state("UNVALIDATED").build());

        driverRepository.save(driver);
        abandonOrder(driver, token);

        return driverDTOMapper.getDriverDTO(driver);
    }

    private void abandonOrder(Driver driver, String token){
        final String url = ApiUrl.getORDER_SERVICE_API_URL()
                + "/drivers/"
                + driver.getUserDetails().getUsername()
                + "/accepted-order";

        HttpEntity<String> headers = getHeaders(Map.of("Authorization", token));
        restTemplate.exchange(url , HttpMethod.DELETE, headers, String.class);
    }

    public UserDetails deleteUser(Long userId, String token){
        String status = "DELETED";
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow();
        UserStatus userStatus = UserStatus.builder().status(status).build();

        setStatusForAllRoles(userStatus, user, token);
        userDetailsRepository.save(user);

        return user;
    }


    private void setStatusForAllRoles(UserStatus userStatus, UserDetails user, String token){
        if(user.getCustomer() != null) {
            user.getCustomer().setUserStatus(userStatus);
            user.getCustomer().getOrders().forEach(order -> {
                final String url = ApiUrl.getORDER_SERVICE_API_URL() + "/orders/" + order.getId();
                HttpEntity<String> headers = getHeaders(Map.of("Authorization", token));
                restTemplate.exchange(url, HttpMethod.DELETE, headers, String.class);
            });
        }
        if(user.getOwner() != null)
            user.getOwner().setUserStatus(userStatus);
        if(user.getDriver() != null)
            deleteDriver(user.getDriver().getId(), token);
        if(user.getAdmin() != null)
            user.getAdmin().setUserStatus(userStatus);
    }

}
