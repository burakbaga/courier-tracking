package com.migros.couriertracking.controller;


import com.migros.couriertracking.exception.CourierNotFoundException;
import com.migros.couriertracking.service.DistanceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourierQueryController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DistanceService distanceService;

    @Test
    void test_notFoundCourierReturns404() throws Exception {

        when(distanceService.getTotalDistance("UNKNOWN"))
                .thenThrow(new CourierNotFoundException("UNKNOWN"));

        mockMvc.perform(get("/courier/UNKNOWN/distance"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_notFoundCourierReturns404WithBody() throws Exception {

        when(distanceService.getTotalDistance("UNKNOWN"))
                .thenThrow(new CourierNotFoundException("UNKNOWN"));

        mockMvc.perform(get("/courier/UNKNOWN/distance"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(containsString("UNKNOWN")))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path")
                        .value("/courier/UNKNOWN/distance"));
    }
}
