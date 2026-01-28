package com.migros.couriertracking.service;

import com.migros.couriertracking.model.CourierLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CourierProcessingService {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CourierProcessingService.class);

    private final DistanceService distanceService;
    private final ProximityService proximityService;

    public CourierProcessingService(DistanceService distanceService,
                                    ProximityService proximityService) {
        this.distanceService = distanceService;
        this.proximityService = proximityService;
    }

    public void process(CourierLocation location) {

        LOGGER.debug(
                "Processing location for courier {}: {}, {}",
                location.getCourierId(),
                location.getLat(),
                location.getLng()
        );

        distanceService.process(location);

        proximityService.process(location);
    }
}
