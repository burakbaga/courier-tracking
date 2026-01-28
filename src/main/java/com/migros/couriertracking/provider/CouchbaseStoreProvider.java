package com.migros.couriertracking.provider;

import com.couchbase.client.java.Collection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.couriertracking.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouchbaseStoreProvider implements StoreProvider {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CouchbaseStoreProvider.class);
    private final Collection collection;
    private final ObjectMapper mapper = new ObjectMapper();

    public CouchbaseStoreProvider(Collection collection) {
        this.collection = collection;
    }

    @Override
    public List<Store> loadStores() {

        try {
            var content = collection
                    .get("stores")
                    .contentAsArray();

            return mapper.readValue(
                    content.toString(),
                    new TypeReference<List<Store>>() {}
            );

        } catch (Exception e) {
            LOGGER.warn("Could not load stores from Couchbase, using fallback source", e);
            return List.of();
        }
    }
}
