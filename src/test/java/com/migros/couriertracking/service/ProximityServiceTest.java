package com.migros.couriertracking.service;

import com.migros.couriertracking.model.CourierLocation;
import com.migros.couriertracking.model.Store;
import com.migros.couriertracking.util.DistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class ProximityServiceTest {

    private ProximityService service;
    private StoreService storeService;
    private DistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        storeService = mock(StoreService.class);
        calculator = mock(DistanceCalculator.class);
        service = new ProximityService(storeService, calculator);
    }

    @Test
    void test_entryRegisteredWhenCourierWithinRadius() throws Exception {

        Store store = new Store();
        store.setName("Migros A");
        store.setLat(40.0);
        store.setLng(29.0);

        when(storeService.getStores())
                .thenReturn(List.of(store));

        when(calculator.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(50.0);

        CourierLocation location = new CourierLocation();
        location.setCourierId("C1");
        location.setLat(40.0);
        location.setLng(29.0);
        location.setTimestamp(LocalDateTime.now());

        service.process(location);

        Map<String, Map<String, LocalDateTime>> lastEntry = getLastEntry();

        assertThat(lastEntry).containsKey("C1");
        assertThat(lastEntry.get("C1")).containsKey("Migros A");
    }

    @Test
    void test_noEntryWhenCourierOutsideRadius() throws Exception {

        Store store = new Store();
        store.setName("Migros A");
        store.setLat(40.0);
        store.setLng(29.0);

        when(storeService.getStores())
                .thenReturn(List.of(store));

        when(calculator.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(200.0);

        CourierLocation location = new CourierLocation();
        location.setCourierId("C1");
        location.setLat(41.0);
        location.setLng(30.0);
        location.setTimestamp(LocalDateTime.now());

        service.process(location);

        Map<String, Map<String, LocalDateTime>> lastEntry = getLastEntry();

        assertThat(lastEntry).isEmpty();
    }

    @Test
    void test_sameStoreNotRegisteredWithinOneMinute() throws Exception {

        Store store = new Store();
        store.setName("Migros A");
        store.setLat(40.0);
        store.setLng(29.0);

        when(storeService.getStores())
                .thenReturn(List.of(store));

        when(calculator.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(50.0);

        LocalDateTime now = LocalDateTime.now();

        CourierLocation first = new CourierLocation();
        first.setCourierId("C1");
        first.setLat(40.0);
        first.setLng(29.0);
        first.setTimestamp(now);

        CourierLocation second = new CourierLocation();
        second.setCourierId("C1");
        second.setLat(40.0);
        second.setLng(29.0);
        second.setTimestamp(now.plusSeconds(30));

        service.process(first);
        service.process(second);

        Map<String, Map<String, LocalDateTime>> lastEntry = getLastEntry();

        assertThat(lastEntry.get("C1")).hasSize(1);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, LocalDateTime>> getLastEntry() throws Exception {

        Field field = ProximityService.class.getDeclaredField("lastEntry");
        field.setAccessible(true);

        return (Map<String, Map<String, LocalDateTime>>) field.get(service);
    }
}
