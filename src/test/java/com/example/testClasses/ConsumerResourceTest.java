package com.example.testClasses;


import com.example.request.CreateConsumerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.dropwizard.testing.FixtureHelpers.fixture;

public class ConsumerResourceTest extends BaseTestClass{

    @Test
    public void testCreateConsumerSuccess() throws JsonProcessingException {
        String payload = fixture("fixture/create_consumer_payload.json");
        CreateConsumerRequest request = mapper.readValue(payload, CreateConsumerRequest.class);

        Response response = client.target(getUri("consumer/create")).request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateConsumerFailure() throws JsonProcessingException {
        String payload = fixture("fixture/create_consumer_failure_payload.json");
        CreateConsumerRequest request = mapper.readValue(payload, CreateConsumerRequest.class);

        Response response = client.target(getUri("consumer/create")).request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals(422, response.getStatus());
    }

}
