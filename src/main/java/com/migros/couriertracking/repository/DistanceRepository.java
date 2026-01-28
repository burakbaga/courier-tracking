package com.migros.couriertracking.repository;

import com.migros.couriertracking.model.CourierDistance;

import java.util.Optional;

public interface DistanceRepository {

    Optional<CourierDistance> findByCourierId(String courierId);

    void save(CourierDistance distance);
}
