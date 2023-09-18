package com.example.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class MongoDBConfiguration {

    @JsonProperty
    @NotEmpty
    public String mongoHost;

    @JsonProperty
    public int mongoPort;

    @JsonProperty
    @NotEmpty
    public String mongoDB;

    public String getMongoHost() {
        return mongoHost;
    }

    public void setMongoHost(String mongoHost) {
        this.mongoHost = mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(int mongoPort) {
        this.mongoPort = mongoPort;
    }

    public String getMongoDB() {
        return mongoDB;
    }

    public void setMongoDB(String mongoDB) {
        this.mongoDB = mongoDB;
    }
}
