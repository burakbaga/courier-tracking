package com.migros.couriertracking.provider;

import com.migros.couriertracking.model.Store;
import java.util.List;

public interface StoreProvider {
    List<Store> loadStores();
}
