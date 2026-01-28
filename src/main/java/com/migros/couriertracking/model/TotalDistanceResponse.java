package com.migros.couriertracking.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalDistanceResponse {

    private String courierId;
    private BigDecimal totalDistance;

    public TotalDistanceResponse(String courierId, BigDecimal totalDistance) {
        this.courierId = courierId;
        this.totalDistance = totalDistance;
    }
}
