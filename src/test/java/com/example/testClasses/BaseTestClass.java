package com.example.testClasses;

import com.example.EventDeliveryApplicationTest;
import com.example.config.EventDeliveryConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class BaseTestClass {

    protected static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("config.yaml");

    protected static final Client client = ClientBuilder.newClient();

    protected static final ObjectMapper mapper = new ObjectMapper();

    @ClassRule
    public static final DropwizardAppRule<EventDeliveryConfiguration> RULE = new DropwizardAppRule<>(
            EventDeliveryApplicationTest.class, CONFIG_PATH);

    protected static String getUri(String endPoint){
        return String.format("http://localhost:%d/%s", RULE.getLocalPort(), endPoint);
    }
}
