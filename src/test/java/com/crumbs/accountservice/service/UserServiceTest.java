package com.crumbs.accountservice.service;

import com.crumbs.accountservice.MockUtil;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserDetailsRepository userDetailsRepository;


    @BeforeEach
    void beforeEach(){
        UserDetails userDetails = MockUtil.getUser();

        Mockito.when(userDetailsRepository.findAll(any(Example.class),
                any(PageRequest.class)))
        .thenReturn(MockUtil.getPageUsers());
    }

    @Test
    void getUsers() {
        PageRequest pageRequest = MockUtil.getPageRequest();
        Page<UserDetails> listOfUsers = userService.getUsers("", pageRequest, "");

        assertEquals(listOfUsers.getTotalElements(), MockUtil.getPageUsers().getTotalElements());
    }

    @Test
    void getPageRequest() {
        PageRequest pageRequest = MockUtil.getPageRequest();
        PageRequest pageRequest1 = userService.getPageRequest(
                pageRequest.getPageNumber(), pageRequest.getPageSize(), MockUtil.getExtras()
        );

        assertEquals(pageRequest1.getPageNumber(), pageRequest.getPageNumber());
    }
}