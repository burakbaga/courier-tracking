package com.migros.couriertracking.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseBeansConfig {

    @Value("${spring.couchbase.bucket-name}")
    private String bucketName;

    @Bean
    public Bucket bucket(Cluster cluster) {
        return cluster.bucket(bucketName);
    }

    @Bean
    public Collection collection(Bucket bucket) {
        return bucket.defaultCollection();
    }
}
