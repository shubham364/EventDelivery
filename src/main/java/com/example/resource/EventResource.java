package com.example.resource;

import com.example.Service.EventService;
import com.example.exception.EventDeliveryException;
import com.example.model.Event;
import com.example.request.EventRequest;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/event")
public class EventResource {

    private final static Logger logger = LoggerFactory.getLogger(EventResource.class);

    @Inject
    private EventService eventService;

    public EventResource() {}

    @Path("/get/{topic_name}/{user_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getEvent(@PathParam(value = "topic_name") String topicName, @PathParam(value = "event_id") String userId) throws EventDeliveryException {
        Event event = eventService.getEvent(topicName, "topics");
        return Response.ok(event).build();
    }

    @Path("/publish")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response publish(@QueryParam(value = "topic_name") @DefaultValue("rudderstack") String topicName,
                            EventRequest eventRequest) {
        Event event = eventService.insertOne(eventRequest, topicName);
        return Response.ok(event).build();
    }

    @Path("/create_topic/{topic_name}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTopic(@PathParam(value = "topic_name") String topicName) {
        eventService.createCollection(topicName);
        return Response.ok(topicName).build();
    }

}
