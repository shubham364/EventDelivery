package com.example.resource;

import com.example.Service.EventService;
import com.example.model.Event;
import com.mongodb.client.MongoDatabase;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/event")
public class EventResource {

    private final MongoDatabase mongoDatabase;
    private final EventService eventService;

    public EventResource(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        eventService = new EventService(mongoDatabase);
    }

    @Path("/path_param/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTailoredGreetingPathParam(
            @PathParam(value = "name") String name) {
        Event event = new Event();
        event.setId("1");
        event.setPayload(name);
        event.setUserId("1");
        eventService.insertOne(event);
        return Response.ok(event).build();
    }

}
