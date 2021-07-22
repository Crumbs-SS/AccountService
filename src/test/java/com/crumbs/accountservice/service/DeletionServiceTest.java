package com.crumbs.accountservice.service;

import com.crumbs.accountservice.MockUtil;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeletionServiceTest {

    @Autowired
    DeletionService deletionService;

    @MockBean
    UserDetailsRepository userDetailsRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach(){
        UserDetails userDetails = MockUtil.getUser();

        Mockito.when(userDetailsRepository.findById(userDetails.getId()))
                .thenReturn(MockUtil.getOptionalUser());
        Mockito.when(userDetailsRepository.save(userDetails))
                .thenReturn(userDetails);

    }

    @Test
    void deleteUser() {
        UserDetails userDetails = MockUtil.getUser();

        assertEquals(userDetails.getId(),
                deletionService.deleteUser(userDetails.getId()).getId());
    }
}