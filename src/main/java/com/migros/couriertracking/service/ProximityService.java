package com.migros.couriertracking.service;

import com.migros.couriertracking.model.CourierLocation;
import com.migros.couriertracking.model.Store;
import com.migros.couriertracking.provider.CouchbaseStoreProvider;
import com.migros.couriertracking.util.DistanceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProximityService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ProximityService.class);


    private static final double RADIUS = 100;

    private final StoreService storeService;
    private final DistanceCalculator calculator;

    private final Map<String, Map<String, LocalDateTime>> lastEntry = new ConcurrentHashMap<>();

    public ProximityService(StoreService storeService,
                            DistanceCalculator calculator) {
        this.storeService = storeService;
        this.calculator = calculator;
    }

    public void process(CourierLocation location) {

        List<Store> stores = storeService.getStores();

        for (Store store : stores) {

            double dist = calculator.calculate(
                    location.getLat(), location.getLng(),
                    store.getLat(), store.getLng()
            );

            if (dist <= RADIUS) {
                handleEntry(location, store);
            }
        }
    }

    private void handleEntry(CourierLocation location, Store store) {

        lastEntry.putIfAbsent(
                location.getCourierId(),
                new ConcurrentHashMap<>()
        );

        Map<String, LocalDateTime> storeMap =
                lastEntry.get(location.getCourierId());

        LocalDateTime lastTime = storeMap.get(store.getName());

        if (lastTime == null ||
                Duration.between(lastTime, location.getTimestamp())
                        .toMinutes() >= 1) {

            LOGGER.info(
                    "Courier {} entered store {} at {}",
                    location.getCourierId(),
                    store.getName(),
                    location.getTimestamp()
            );


            storeMap.put(store.getName(), location.getTimestamp());
        }
    }
}
