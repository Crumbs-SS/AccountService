package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.MockUtil;
import com.crumbs.accountservice.service.DeletionService;
import com.crumbs.accountservice.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RegistrationService registrationService;

    @Test
    void registerCustomer() throws Exception{
        mockMvc.perform(post("/account-service/register/customer")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("CUSTOMER")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getCustomerRegistration())))
                .andExpect(status().isCreated());
    }
    @Test
    void registerDriver() throws Exception {
        mockMvc.perform(post("/account-service/register/driver")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("DRIVER")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getDriverRegistration())))
                .andExpect(status().isCreated());
    }
    @Test
    void registerOwner() throws Exception{
        mockMvc.perform(post("/account-service/register/owner")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("OWNER")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getOwnerRegistration())))
                .andExpect(status().isCreated());
    }
}