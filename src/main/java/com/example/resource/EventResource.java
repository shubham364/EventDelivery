package com.example.resource;

import com.example.Service.EventService;
import com.example.model.Event;
import com.example.request.EventRequest;
import com.mongodb.client.MongoDatabase;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/event")
public class EventResource {

    private final EventService eventService;

    public EventResource(MongoDatabase mongoDatabase) {
        eventService = new EventService(mongoDatabase);
    }

    @Path("/path_param/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTailoredGreetingPathParam(
            @PathParam(value = "name") String name) {
        EventRequest event = new EventRequest();
        event.setPayload(name);
        event.setUserId("1");
        eventService.insertOne(event, "topics");
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
