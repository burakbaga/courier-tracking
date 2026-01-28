package com.migros.couriertracking.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourierLocation {

    @NotBlank(message = "courierId cannot be blank")
    private String courierId;

    @NotNull
    @DecimalMin(value = "-90.0", message = "latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "latitude must be <= 90")
    private Double lat;

    @NotNull
    @DecimalMin(value = "-180.0", message = "longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "longitude must be <= 180")
    private Double lng;

    @NotNull(message = "timestamp is required")
    private LocalDateTime timestamp;
}
