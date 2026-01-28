package com.migros.couriertracking.controller;

import com.migros.couriertracking.model.TotalDistanceResponse;
import com.migros.couriertracking.service.DistanceService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/courier")
public class CourierQueryController {

    private final DistanceService distanceService;

    public CourierQueryController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    @GetMapping("/{id}/distance")
    public TotalDistanceResponse getDistance(@PathVariable String id) {
        BigDecimal total = distanceService.getTotalDistance(id);
        return new TotalDistanceResponse(id, total);
    }
}
