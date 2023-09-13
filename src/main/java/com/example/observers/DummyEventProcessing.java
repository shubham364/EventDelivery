package com.example.observers;

import com.example.Service.EventService;
import com.example.dao.MongoDAO;
import com.example.model.Consumers;
import com.example.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;

import static com.example.model.Constants.CONSUMER_COLLECTION_NAME;

public class DummyEventProcessing implements Callable {

    private final static Logger logger = LoggerFactory.getLogger(DummyEventProcessing.class);

    private final Event event;

    private final EventService eventService;

    private final MongoDAO mongoDAO;

    private final MongoDatabase mongoDatabase;

    private final ObjectMapper objectMapper;

    private final Consumers consumer;

    @Inject
    public DummyEventProcessing(Event event, EventService eventService, MongoDatabase mongoDatabase, MongoDAO mongoDAO, Consumers consumer) {
        this.event = event;
        this.eventService = eventService;
        this.mongoDatabase = mongoDatabase;
        this.mongoDAO = mongoDAO;
        objectMapper = new ObjectMapper();
        this.consumer = consumer;
    }

    @Override
    public Object call() {
        startMockingEventProcessing(event.getId());
        return null;
    }

    // TODO - Handle failures and retries
    private void startMockingEventProcessing(String eventId){
        Event event = eventService.getEventById(eventId, consumer.getTopicName());
        if(event == null)
            return;
        Random rand = new Random(10000);
        int random = rand.nextInt();
        try {
            // sleeping for a random time between [0, 10) seconds mocking event processing.
            Thread.sleep(random);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        handlePostProcessing(eventId);
        int nextEventId = Integer.parseInt(eventId) + 1;
        startMockingEventProcessing(String.valueOf(nextEventId));
    }

    private void handlePostProcessing(String eventId){
        consumer.setCursor(eventId);
        consumer.setCurrRetry(0);
        updateConsumer(consumer);
    }

    private void updateConsumer(Consumers consumer){
        Document document = objectMapper.convertValue(consumer, Document.class);
        mongoDAO.update(mongoDatabase.getCollection(CONSUMER_COLLECTION_NAME), consumer.getConsumerId(), document);
    }
}
