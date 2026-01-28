package com.migros.couriertracking.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourierDistance {

    private String courierId;
    private BigDecimal totalDistance;
}
