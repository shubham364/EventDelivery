package com.example;

import com.example.config.EventDeliveryConfiguration;
import com.example.dao.InitialiseObservers;
import com.example.dao.MongoHealthCheck;
import com.example.exception.EventDeliveryExceptionMapper;
import com.example.guice.GuiceModule;
import com.example.resource.ConsumerResource;
import com.example.resource.EventResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EventDeliveryApplication extends Application<EventDeliveryConfiguration> {

    public static void main(String[] args) throws Exception {
        new EventDeliveryApplication().run(args);
    }

    public void run(EventDeliveryConfiguration config, Environment env) throws Exception {
        Injector injector = Guice.createInjector(new GuiceModule(config));
        InitialiseObservers initialiseObservers = injector.getInstance(InitialiseObservers.class);
        initialiseObservers.initialiseAllObserversOnStartUp();
        env.jersey().register(injector.getInstance(EventDeliveryExceptionMapper.class));
        env.jersey().register(injector.getInstance(EventResource.class));
        env.jersey().register(injector.getInstance(ConsumerResource.class));
        env.healthChecks().register("mongodb-health", injector.getInstance(MongoHealthCheck.class));
    }

    @Override
    protected void bootstrapLogging() {
    }

    @Override
    public void initialize(final Bootstrap<EventDeliveryConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }
}
