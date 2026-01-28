package com.migros.couriertracking.service;

import com.migros.couriertracking.model.CourierLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CourierProcessingServiceTest {

    private CourierProcessingService service;
    private DistanceService distanceService;
    private ProximityService proximityService;

    @BeforeEach
    void setUp() {
        distanceService = mock(DistanceService.class);
        proximityService = mock(ProximityService.class);
        service = new CourierProcessingService(distanceService, proximityService);
    }

    @Test
    void test_processCallsBothServices() {

        CourierLocation location = new CourierLocation();
        location.setCourierId("C1");

        service.process(location);

        verify(distanceService).process(location);
        verify(proximityService).process(location);
    }
}
