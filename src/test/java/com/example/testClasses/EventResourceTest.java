package com.example.testClasses;


import com.example.request.EventRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.dropwizard.testing.FixtureHelpers.fixture;

public class EventResourceTest extends BaseTestClass{

    @Test
    public void testEventPublishSuccess() throws JsonProcessingException {
        String payload = fixture("fixture/event_payload.json");
        EventRequest request = mapper.readValue(payload, EventRequest.class);

        Response response = client.target(getUri("event/publish")).request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testEventPublishFailure() throws JsonProcessingException {
        String payload = fixture("fixture/event_failure_payload.json");
        EventRequest request = mapper.readValue(payload, EventRequest.class);

        Response response = client.target(getUri("event/publish")).request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals( 422, response.getStatus());
    }

}
