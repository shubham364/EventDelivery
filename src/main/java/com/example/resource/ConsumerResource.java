package com.example.resource;

import com.example.Service.ConsumerService;
import com.example.model.Consumers;
import com.example.request.CreateConsumerRequest;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/consumer")
public class ConsumerResource {

    private final static Logger logger = LoggerFactory.getLogger(ConsumerResource.class);

    @Inject
    private ConsumerService consumerService;

    public ConsumerResource() {}

    @Path("/create")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createConsumer(CreateConsumerRequest request) {
        Consumers consumer = consumerService.createConsumer(request);
        return Response.ok(consumer).build();
    }
}
