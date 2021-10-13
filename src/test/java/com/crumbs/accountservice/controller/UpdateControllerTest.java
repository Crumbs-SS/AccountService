package com.crumbs.accountservice.controller;

import com.crumbs.accountservice.MockUtil;
import com.crumbs.accountservice.service.UpdateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UpdateController.class)
class UpdateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UpdateService updateService;

    @Test
    void updateCustomer() throws Exception {
        mockMvc.perform(put("/account-service/customers")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("CUSTOMER")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getUserDetailsUpdate())))
                .andExpect(status().isOk());
    }
    @Test
    void changePassword() throws Exception {
        mockMvc.perform(post("/account-service/users/password/recover")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("DRIVER")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getChangePasswordDTO())))
                .andExpect(status().isOk());
    }
    @Test
    void updateUser() throws Exception {
        mockMvc.perform(put("/account-service/users/{userId}", MockUtil.getUser().getId())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getUserDetailsUpdate())))
                .andExpect(status().isOk());
    }

    @Test
    void enableUser() throws Exception {
        mockMvc.perform(put("/account-service/users/{userId}/status", MockUtil.getUser().getId())
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("ADMIN")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getEnableUser())))
                .andExpect(status().isOk());
    }
    @Test
    void checkInDriver() throws Exception {
        mockMvc.perform(put("/account-service/drivers/checkIn/{username}", "correctUsername")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("DRIVER")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void CheckOutDriver() throws Exception {
        mockMvc.perform(put("/account-service/drivers/checkOut/{username}", "correctUsername")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("DRIVER")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void requestPasswordChange() throws Exception {
        mockMvc.perform(get("/account-service/users/email/{email}", "email")
                .header("Authorization", ("Bearer " + MockUtil.createMockJWT("DRIVER")))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}