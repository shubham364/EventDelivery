package com.example.resource;

import com.example.Service.ConsumerService;
import com.example.exception.EventDeliveryException;
import com.example.model.Consumers;
import com.example.model.enums.TraceId;
import com.example.request.CreateConsumerRequest;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/consumer")
public class ConsumerResource {

    private final static Logger logger = LoggerFactory.getLogger(ConsumerResource.class);

    @Inject
    private ConsumerService consumerService;

    public ConsumerResource() {}

    @Path("/get/{consumer_name}")
    @GET
    @TraceId
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getConsumer(@PathParam(value = "consumer_name") String consumerName) throws EventDeliveryException {
        Consumers consumer = consumerService.getConsumer(consumerName);
        logger.info(consumer.toString());
        return Response.ok(consumer).build();
    }

    @Path("/create")
    @POST
    @TraceId
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createConsumer(@Valid CreateConsumerRequest request) {
        Consumers consumer = consumerService.createConsumer(request);
        logger.info("Created a new consumer - {}", consumer);
        return Response.ok(consumer).build();
    }
}
