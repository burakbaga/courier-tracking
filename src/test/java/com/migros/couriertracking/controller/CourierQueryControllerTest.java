package com.migros.couriertracking.controller;

import com.migros.couriertracking.service.DistanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourierQueryController.class)
class CourierQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DistanceService distanceService;

    @Test
    void test_returnsTotalDistanceSuccessfully() throws Exception {

        Mockito.when(distanceService.getTotalDistance("C1"))
                .thenReturn(BigDecimal.valueOf(250.75));

        mockMvc.perform(get("/courier/C1/distance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courierId").value("C1"))
                .andExpect(jsonPath("$.totalDistance").value(250.75));
    }
}
