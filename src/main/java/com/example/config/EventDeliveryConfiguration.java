package com.example.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class EventDeliveryConfiguration extends Configuration {
    @NotNull
    private final int defaultSize;

    @NotNull
    @JsonProperty("mongoConfig")
    private MongoDBConfiguration mongoDBConfiguration;

    @JsonCreator
    public EventDeliveryConfiguration(@JsonProperty("defaultSize") final int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public int getDefaultSize() {
        return defaultSize;
    }

    public MongoDBConfiguration getMongoDBConfiguration() {
        return mongoDBConfiguration;
    }

    public void setMongoDBConfiguration(MongoDBConfiguration mongoDBConfiguration) {
        this.mongoDBConfiguration = mongoDBConfiguration;
    }
}
