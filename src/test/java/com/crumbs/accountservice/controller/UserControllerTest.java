package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.MockUtil;
import com.crumbs.accountservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void ownerExists() throws Exception {
        mockMvc.perform(get("/account-service/owners/{username}", MockUtil.getUsername())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void usersById() throws Exception {
        mockMvc.perform(get("/account-service/users/id/{userId}", 1l)
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void userByUsername() throws Exception {
        mockMvc.perform(get("/account-service/users/{username}", MockUtil.getUsername())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/account-service/users")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void getDriverStatus() throws Exception {
        mockMvc.perform(get("/account-service/drivers/status/{username}", MockUtil.getUsername())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("DRIVER")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void getDriverPay() throws Exception {
        mockMvc.perform(get("/account-service/drivers/pay/{username}", MockUtil.getUsername())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("DRIVER")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void getDriverAverageRating() throws Exception {
        mockMvc.perform(get("/account-service/drivers/rating/{username}", MockUtil.getUsername())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("DRIVER")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void getDriverRatings() throws Exception {
        mockMvc.perform(get("/account-service/drivers/ratings/{username}", MockUtil.getUsername())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("DRIVER")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void getDrivers() throws Exception {
        mockMvc.perform(get("/account-service/drivers")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void checkIfDriverIsAvailable() throws Exception {
        mockMvc.perform(get("/account-service/drivers/{username}", MockUtil.getUsername())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}