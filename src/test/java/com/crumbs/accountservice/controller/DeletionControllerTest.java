package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.MockUtil;
import com.crumbs.accountservice.service.DeletionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DeletionController.class)
class DeletionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DeletionService deletionService;

    @Test
    void deleteCustomer() throws Exception{
        mockMvc.perform(delete("/account-service/customers")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("CUSTOMER")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getCred())))
                .andExpect(status().isOk());
    }
    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/account-service/users/{userId}", MockUtil.getUser().getId())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void deleteDriver() throws Exception{
        mockMvc.perform(delete("/account-service/drivers/{driverId}", 1l)
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getCred())))
                .andExpect(status().isOk());
    }
}