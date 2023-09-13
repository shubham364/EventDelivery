package com.example.guice;

import com.example.config.EventDeliveryConfiguration;
import com.example.config.MongoDBConfiguration;
import com.example.observers.Observable;
import com.google.inject.AbstractModule;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class GuiceModule extends AbstractModule {

    private final EventDeliveryConfiguration config;

    public GuiceModule(EventDeliveryConfiguration config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        MongoDatabase mongoDatabase = getMongoDatabase();
        bind(MongoDatabase.class).toInstance(mongoDatabase);
        Observable observable = Observable.getObservable();
        bind(Observable.class).toInstance(observable);
    }

    private MongoDatabase getMongoDatabase(){
        MongoDBConfiguration mongoDBConfiguration = config.getMongoDBConfiguration();
        MongoClient mongoClient = new MongoClient(mongoDBConfiguration.getMongoHost(), mongoDBConfiguration.getMongoPort());
        return mongoClient.getDatabase(mongoDBConfiguration.getMongoDB());
    }
}
