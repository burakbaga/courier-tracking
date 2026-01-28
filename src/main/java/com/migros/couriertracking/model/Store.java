package com.migros.couriertracking.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

@Document
@Getter
@Setter
public class Store {

    @Id
    private String name;

    private double lat;
    private double lng;

}
