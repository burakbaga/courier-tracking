package com.migros.couriertracking.repository;

import com.couchbase.client.java.Collection;
import com.migros.couriertracking.model.CourierDistance;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CouchbaseDistanceRepository implements DistanceRepository {

    private final Collection collection;

    public CouchbaseDistanceRepository(Collection collection) {
        this.collection = collection;
    }

    private String key(String courierId) {
        return "distance::" + courierId;
    }

    @Override
    public Optional<CourierDistance> findByCourierId(String courierId) {

        try {
            return Optional.of(
                    collection.get(key(courierId))
                            .contentAs(CourierDistance.class)
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(CourierDistance distance) {
        collection.upsert(key(distance.getCourierId()), distance);
    }
}
