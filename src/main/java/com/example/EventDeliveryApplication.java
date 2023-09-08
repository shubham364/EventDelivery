package com.example;

import com.example.config.EventDeliveryConfiguration;
import com.example.resource.EventResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EventDeliveryApplication extends Application<EventDeliveryConfiguration> {

    public static void main(String[] args) throws Exception {
        new EventDeliveryApplication().run(args);
    }

    @Override
    public void run(EventDeliveryConfiguration configuration, Environment environment) throws Exception {
        final int defaultSize = configuration.getDefaultSize();
        System.out.println(defaultSize);
        environment.jersey().register(new EventResource());
    }

    @Override
    public void initialize(final Bootstrap<EventDeliveryConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        super.initialize(bootstrap);
    }
}
