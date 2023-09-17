package com.example.guice;

import com.example.config.EventDeliveryConfiguration;
import com.example.config.MongoDBConfiguration;
import com.example.observers.Observable;
import com.google.inject.AbstractModule;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import java.net.InetSocketAddress;
import java.util.Collections;


public class GuiceModuleTest  extends AbstractModule {

    private final EventDeliveryConfiguration config;

    public GuiceModuleTest(EventDeliveryConfiguration config) {
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
        // Faking Mongo here.
        MongoServer server = new MongoServer(new MemoryBackend());
        InetSocketAddress serverAddress = server.bind();

        MongoClientSettings settings = MongoClientSettings.builder().applyToClusterSettings(
                builder ->  builder.hosts(Collections.singletonList(new ServerAddress(serverAddress)))).build();

        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase(mongoDBConfiguration.getMongoDB());
    }
}
