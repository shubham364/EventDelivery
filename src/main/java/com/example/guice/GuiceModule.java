package com.example.guice;

import com.example.Service.interceptor.LoggingMethodInterceptor;
import com.example.config.EventDeliveryConfiguration;
import com.example.config.MongoDBConfiguration;
import com.example.model.enums.TraceId;
import com.example.observers.Observable;
import com.google.inject.AbstractModule;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

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
        LoggingMethodInterceptor loggingMethodInterceptor = new LoggingMethodInterceptor();
        bindInterceptor(any(), annotatedWith(TraceId.class), loggingMethodInterceptor);
    }

    private MongoDatabase getMongoDatabase(){
        MongoDBConfiguration mongoDBConfiguration = config.getMongoDBConfiguration();
        MongoClient mongoClient = new MongoClient(mongoDBConfiguration.getMongoHost(), mongoDBConfiguration.getMongoPort());
        return mongoClient.getDatabase(mongoDBConfiguration.getMongoDB());
    }
}
