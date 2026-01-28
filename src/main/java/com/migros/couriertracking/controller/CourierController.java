package com.migros.couriertracking.controller;

import com.migros.couriertracking.model.CourierLocation;
import com.migros.couriertracking.service.CourierProcessingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courier")
public class CourierController {

    private final CourierProcessingService service;

    public CourierController(CourierProcessingService service) {
        this.service = service;
    }

    @PostMapping("/location")
    public void sendLocation(@Valid @RequestBody CourierLocation location) {
        service.process(location);
    }
}
