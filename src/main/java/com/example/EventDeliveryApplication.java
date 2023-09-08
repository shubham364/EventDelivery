package com.example;

import com.example.config.EventDeliveryConfiguration;
import com.example.config.MongoDBConfiguration;
import com.example.resource.EventResource;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EventDeliveryApplication extends Application<EventDeliveryConfiguration> {

    public static void main(String[] args) throws Exception {
        new EventDeliveryApplication().run(args);
    }

    public void run(EventDeliveryConfiguration config, Environment env)
            throws Exception {
        MongoDBConfiguration mongoDBConfiguration = config.getMongoDBConfiguration();
        MongoClient mongoClient = new MongoClient(mongoDBConfiguration.getMongoHost(), mongoDBConfiguration.getMongoPort());
        MongoManaged mongoManaged = new MongoManaged(mongoClient);
        env.lifecycle().manage(mongoManaged);
        MongoDatabase db = mongoClient.getDatabase(mongoDBConfiguration.getMongoDB());
        env.jersey().register(new EventResource(db));
    }

    @Override
    public void initialize(final Bootstrap<EventDeliveryConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }
}
