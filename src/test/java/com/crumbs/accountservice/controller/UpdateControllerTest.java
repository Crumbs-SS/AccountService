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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    void updateUser() throws Exception {
        mockMvc.perform(put("/users/{userId}", MockUtil.getUser().getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getUserDetailsUpdate())))
                .andExpect(status().isOk());
    }

    @Test
    void enableUser() throws Exception {
        mockMvc.perform(put("/users/{userId}/status", MockUtil.getUser().getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(MockUtil.getEnableUser())))
                .andExpect(status().isOk());
    }
}