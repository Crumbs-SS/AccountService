package com.crumbs.accountservice.service;

import com.crumbs.accountservice.MockUtil;
import com.crumbs.accountservice.dto.CustomerDeleteCredentials;
import com.crumbs.accountservice.dto.DriverDTO;
import com.crumbs.lib.entity.Customer;
import com.crumbs.lib.entity.Driver;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.DriverRepository;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DeletionServiceTest {

    @Autowired
    DeletionService deletionService;

    @MockBean RestTemplate restTemplate;
    @MockBean UserDetailsRepository userDetailsRepository;
    @MockBean PasswordEncoder passwordEncoder;
    @MockBean DriverRepository driverRepository;


    @Test
    void itShouldDeleteAUser() {
        UserDetails userDetails = MockUtil.getUser();

        when(userDetailsRepository.findById(userDetails.getId())).thenReturn(MockUtil.getOptionalUser());
        when(userDetailsRepository.save(any(UserDetails.class))).thenReturn(userDetails);
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.DELETE),
                ArgumentMatchers.<HttpEntity<String>>any(),
                eq(String.class)
        )).thenReturn(null);

        UserDetails user = deletionService.deleteUser(userDetails.getId(), "FakeToken");

        assertThat(user.getCustomer().getUserStatus().getStatus()).isEqualTo("DELETED");
        assertThat(user.getAdmin().getUserStatus().getStatus()).isEqualTo("DELETED");
        assertThat(user.getOwner().getUserStatus().getStatus()).isEqualTo("DELETED");
    }

    @Test
    void itShouldDeleteACustomer() {
        CustomerDeleteCredentials credentials = MockUtil.getCustomerDeletedCred();
        UserDetails userDetails = MockUtil.getUser();

        when(userDetailsRepository.findByUsername(userDetails.getUsername()))
                .thenReturn(Optional.of(userDetails));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userDetailsRepository.save(any(UserDetails.class))).thenReturn(userDetails);

        Customer customer = deletionService.deleteCustomer(credentials);
        assertThat(customer.getUserStatus().getStatus()).isEqualTo("DELETED");
    }

    @Test
    void itShouldThrow_NoSuchElementException(){
        CustomerDeleteCredentials credentials = MockUtil.getCustomerDeletedCred();
        UserDetails userDetails = MockUtil.getUser();

        when(userDetailsRepository.findByUsername(userDetails.getUsername()))
                .thenReturn(Optional.of(userDetails));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> deletionService.deleteCustomer(credentials))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void itShouldDeleteADriver() {
        Driver driver = MockUtil.getDriver();

        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.DELETE),
                ArgumentMatchers.<HttpEntity<String>>any(),
                eq(String.class)
        )).thenReturn(null);
        when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));

        DriverDTO driverDTO = deletionService.deleteDriver(1L, "FakeToken");

        assertThat(driverDTO.getUserState()).isEqualTo("DELETED");
        assertThat(driverDTO.getState()).isEqualTo("UNVALIDATED");
    }
}