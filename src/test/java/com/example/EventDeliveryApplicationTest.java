package com.example;

import com.example.config.EventDeliveryConfiguration;
import com.example.exception.EventDeliveryExceptionMapper;
import com.example.guice.GuiceModuleTest;
import com.example.resource.ConsumerResource;
import com.example.resource.EventResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EventDeliveryApplicationTest extends Application<EventDeliveryConfiguration> {

    @Override
    public void initialize(final Bootstrap<EventDeliveryConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    protected void bootstrapLogging() {
    }

    @Override
    public void run(EventDeliveryConfiguration config, Environment env) throws Exception {
        Injector injector = Guice.createInjector(new GuiceModuleTest(config));
        env.jersey().register(injector.getInstance(EventDeliveryExceptionMapper.class));
        env.jersey().register(injector.getInstance(EventResource.class));
        env.jersey().register(injector.getInstance(ConsumerResource.class));
    }
}
