package com.migros.couriertracking.service;

import com.migros.couriertracking.exception.CourierNotFoundException;
import com.migros.couriertracking.model.CourierDistance;
import com.migros.couriertracking.model.CourierLocation;
import com.migros.couriertracking.repository.DistanceRepository;
import com.migros.couriertracking.util.DistanceCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DistanceService {

    private final DistanceCalculator calculator;
    private final DistanceRepository repository;

    private final Map<String, CourierLocation> lastLocation =
            new ConcurrentHashMap<>();

    public DistanceService(DistanceCalculator calculator,
                           DistanceRepository repository) {
        this.calculator = calculator;
        this.repository = repository;
    }

    public void process(CourierLocation location) {

        CourierLocation last = lastLocation.get(location.getCourierId());

        if (last != null) {

            double dist = calculator.calculate(
                    last.getLat(), last.getLng(),
                    location.getLat(), location.getLng()
            );

            BigDecimal increment = BigDecimal.valueOf(dist);

            CourierDistance current = repository
                    .findByCourierId(location.getCourierId())
                    .orElseGet(() -> {
                        CourierDistance d = new CourierDistance();
                        d.setCourierId(location.getCourierId());
                        d.setTotalDistance(BigDecimal.ZERO);
                        return d;
                    });

            current.setTotalDistance(
                    current.getTotalDistance().add(increment)
            );

            repository.save(current);
        }

        lastLocation.put(location.getCourierId(), location);
    }

    public BigDecimal getTotalDistance(String courierId) {

        return repository
                .findByCourierId(courierId)
                .map(d -> d.getTotalDistance()
                        .setScale(2, RoundingMode.HALF_UP))
                .orElseThrow(() -> new CourierNotFoundException(courierId));
    }
}
