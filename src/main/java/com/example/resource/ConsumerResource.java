package com.example.resource;

import com.example.Service.ConsumerService;
import com.example.Service.EventService;
import com.example.model.Consumers;
import com.example.observers.Observable;
import com.example.request.CreateConsumerRequest;
import com.example.request.EventRequest;
import com.mongodb.client.MongoDatabase;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/consumer")
public class ConsumerResource {

    private final ConsumerService consumerService;

    public ConsumerResource(MongoDatabase mongoDatabase) {
        Observable observable = Observable.getObservable();
        this.consumerService = new ConsumerService(mongoDatabase, observable, new EventService(mongoDatabase));
    }

    @Path("/create")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createConsumer(CreateConsumerRequest request) {
        Consumers consumer = consumerService.createConsumer(request);
        return Response.ok(consumer).build();
    }
}
