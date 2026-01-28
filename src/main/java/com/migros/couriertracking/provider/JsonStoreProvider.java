package com.migros.couriertracking.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.couriertracking.model.Store;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class JsonStoreProvider implements StoreProvider {

    @Override
    public List<Store> loadStores() {

        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("stores.json");

            return mapper.readValue(is, new TypeReference<List<Store>>() {});
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load stores from JSON", e);
        }
    }
}
