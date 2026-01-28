package com.migros.couriertracking.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HaversineDistanceCalculatorTest {

    private HaversineDistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new HaversineDistanceCalculator();
    }

    @Test
    void distance_shouldBeZero_whenSamePoint() {

        double distance = calculator.calculate(
                40.0, 29.0,
                40.0, 29.0
        );

        assertThat(distance).isEqualTo(0.0);
    }

    @Test
    void distance_shouldBePositive_whenDifferentPoints() {

        double distance = calculator.calculate(
                40.0, 29.0,
                41.0, 30.0
        );

        assertThat(distance).isGreaterThan(0);
    }

    @Test
    void distance_shouldBeApproximatelyCorrect_betweenKnownLocations() {

        double distance = calculator.calculate(
                40.9923307, 29.1244229,
                40.986106, 29.1161293
        );

        assertThat(distance)
                .isBetween(900.0, 1100.0);
    }

    @Test
    void distance_shouldBeSymmetric() {

        double d1 = calculator.calculate(
                40.0, 29.0,
                41.0, 30.0
        );

        double d2 = calculator.calculate(
                41.0, 30.0,
                40.0, 29.0
        );

        assertThat(d1).isEqualTo(d2);
    }
}
