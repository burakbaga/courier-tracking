package com.migros.couriertracking.integration;

import com.migros.couriertracking.model.CourierLocation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CourierFlowIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void test_fullFlowWithoutDatabase() {

        CourierLocation first = new CourierLocation();
        first.setCourierId("C1");
        first.setLat(40.0);
        first.setLng(29.0);
        first.setTimestamp(LocalDateTime.now());

        CourierLocation second = new CourierLocation();
        second.setCourierId("C1");
        second.setLat(41.0);
        second.setLng(30.0);
        second.setTimestamp(LocalDateTime.now().plusMinutes(1));

        restTemplate.postForEntity("/courier/location", first, Void.class);
        restTemplate.postForEntity("/courier/location", second, Void.class);

        Map response = restTemplate
                .getForObject("/courier/C1/distance", Map.class);

        assertThat(response.get("totalDistance")).isNotNull();
    }
}
