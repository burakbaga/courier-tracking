package com.migros.couriertracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.couriertracking.model.CourierLocation;
import com.migros.couriertracking.service.CourierProcessingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourierController.class)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CourierProcessingService service;

    @Test
    void test_validLocationAccepted() throws Exception {

        CourierLocation valid = new CourierLocation();
        valid.setCourierId("C1");
        valid.setLat(40.0);
        valid.setLng(29.0);
        valid.setTimestamp(LocalDateTime.now());

        mockMvc.perform(post("/courier/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(valid)))
                .andExpect(status().isOk());

        Mockito.verify(service).process(Mockito.any());
    }

    @Test
    void test_invalidLocationRejected() throws Exception {

        CourierLocation invalid = new CourierLocation();

        mockMvc.perform(post("/courier/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).process(Mockito.any());
    }

    @Test
    void test_invalidLocationReturnsValidationMessage() throws Exception {

        CourierLocation invalid = new CourierLocation();

        mockMvc.perform(post("/courier/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message")
                        .value(org.hamcrest.Matchers.containsString("must not be null")));
    }


}
