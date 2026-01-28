package com.migros.couriertracking.service;

import com.migros.couriertracking.exception.CourierNotFoundException;
import com.migros.couriertracking.model.CourierDistance;
import com.migros.couriertracking.model.CourierLocation;
import com.migros.couriertracking.repository.DistanceRepository;
import com.migros.couriertracking.util.DistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class DistanceServiceTest {

    private DistanceService service;
    private DistanceRepository repository;
    private DistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        repository = mock(DistanceRepository.class);
        calculator = mock(DistanceCalculator.class);
        service = new DistanceService(calculator, repository);
    }

    @Test
    void test_firstLocationDoesNotSaveDistance() {

        CourierLocation first = new CourierLocation();
        first.setCourierId("C1");
        first.setLat(40.0);
        first.setLng(29.0);

        service.process(first);

        verify(repository, never()).save(any());
    }

    @Test
    void test_secondLocationAccumulatesDistance() {

        CourierLocation first = new CourierLocation();
        first.setCourierId("C1");
        first.setLat(40.0);
        first.setLng(29.0);

        CourierLocation second = new CourierLocation();
        second.setCourierId("C1");
        second.setLat(41.0);
        second.setLng(30.0);

        when(calculator.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(120.0);

        CourierDistance existing = new CourierDistance();
        existing.setCourierId("C1");
        existing.setTotalDistance(BigDecimal.ZERO);

        when(repository.findByCourierId("C1"))
                .thenReturn(Optional.of(existing));

        service.process(first);
        service.process(second);

        verify(repository).save(argThat(saved ->
                saved.getTotalDistance().compareTo(BigDecimal.valueOf(120.0)) == 0
        ));
    }

    @Test
    void test_getTotalDistanceThrowsExceptionWhenNotFound() {

        when(repository.findByCourierId("C1"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTotalDistance("C1"))
                .isInstanceOf(CourierNotFoundException.class)
                .hasMessageContaining("C1");
    }

}
