package com.migros.couriertracking.service;

import com.migros.couriertracking.model.Store;
import com.migros.couriertracking.provider.CouchbaseStoreProvider;
import com.migros.couriertracking.provider.JsonStoreProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final CouchbaseStoreProvider couchbaseProvider;
    private final JsonStoreProvider jsonProvider;

    public StoreService(CouchbaseStoreProvider couchbaseProvider,
                        JsonStoreProvider jsonProvider) {
        this.couchbaseProvider = couchbaseProvider;
        this.jsonProvider = jsonProvider;
    }

    public List<Store> getStores() {
        List<Store> stores = couchbaseProvider.loadStores();

        if (stores == null || stores.isEmpty()) {
            return jsonProvider.loadStores();
        }

        return stores;
    }
}
