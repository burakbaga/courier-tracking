package com.migros.couriertracking.util;

import com.couchbase.client.java.Collection;
import com.migros.couriertracking.provider.JsonStoreProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StoreDataSeeder implements ApplicationRunner {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(StoreDataSeeder.class);

    private final Collection collection;
    private final JsonStoreProvider jsonProvider;

    public StoreDataSeeder(Collection collection,
                           JsonStoreProvider jsonProvider) {
        this.collection = collection;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public void run(ApplicationArguments args) {

        try {
            collection.get("stores");
            LOGGER.info("Stores document already exists in Couchbase.");

        } catch (Exception ex) {

            LOGGER.info("Stores document not found. Seeding from JSON...");

            try {
                collection.upsert("stores", jsonProvider.loadStores());
                LOGGER.info("Stores document successfully seeded.");

            } catch (Exception seedEx) {
                LOGGER.error("Failed to seed stores document", seedEx);
            }
        }
    }
}
