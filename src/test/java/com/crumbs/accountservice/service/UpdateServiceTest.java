package com.crumbs.accountservice.service;

import com.crumbs.accountservice.MockUtil;
import com.crumbs.accountservice.dto.EnableUser;
import com.crumbs.accountservice.dto.UserDetailsUpdate;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class UpdateServiceTest {

    @Autowired
    UpdateService updateService;

    @MockBean
    UserDetailsRepository userDetailsRepository;

    @BeforeEach
    void beforeEach(){
        UserDetails userDetails = MockUtil.getUser();

        Mockito.when(userDetailsRepository.findByUsername(userDetails.getUsername()))
                .thenReturn(MockUtil.getOptionalUser());

        Mockito.when(userDetailsRepository.findByEmail(userDetails.getEmail()))
                .thenReturn(MockUtil.getOptionalUser());

        Mockito.when(userDetailsRepository.findById(userDetails.getId()))
                .thenReturn(MockUtil.getOptionalUser());

        Mockito.when(userDetailsRepository.save(any(UserDetails.class)))
                .thenReturn(userDetails);

    }

    @Test
    void updateUser() {
        UserDetailsUpdate userDetailsUpdate = MockUtil.getUserDetailsUpdate();
        UserDetails user = MockUtil.getUser();

        UserDetails userDetails = updateService.updateUser(user.getId(), userDetailsUpdate);

        assertEquals(userDetails.getUsername(), user.getUsername());
    }

    @Test
    void enableUser() {
        EnableUser enableUser = MockUtil.getEnableUser();
        UserDetails user = MockUtil.getUser();

        UserDetails userDetails = updateService.enableUser(user.getId(), enableUser);
        assertEquals(userDetails.getUsername(), user.getUsername());
    }
}