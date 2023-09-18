package com.example.dao;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoHealthCheck extends HealthCheck {

    private final static Logger logger = LoggerFactory.getLogger(MongoHealthCheck.class);

    @Inject
    MongoDatabase mongoDatabase;

    @Override
    protected Result check() throws Exception {
        try {
            MongoIterable<String> allCollections = mongoDatabase.listCollectionNames();
            for (String collection : allCollections) {
                logger.info("MongoDB collection: " + collection);
            }
            return Result.healthy("MongoDatabase is up");
        } catch (Exception me) {
            return Result.healthy("MongoDatabase is down/not connected");
        }
    }
}
