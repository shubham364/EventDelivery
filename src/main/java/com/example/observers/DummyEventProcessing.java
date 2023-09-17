package com.example.observers;

import com.example.Service.BackOffRetryService;
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
import org.slf4j.MDC;

import java.util.Random;
import java.util.UUID;

import static com.example.model.Constants.CONSUMER_COLLECTION_NAME;
import static com.example.model.Constants.X_TRACE_ID;

public class DummyEventProcessing implements Runnable {

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
    public void run() {
        String traceId = UUID.randomUUID().toString();
        traceId = traceId.substring(0, 13);
        MDC.put(X_TRACE_ID, traceId);
        startMockingEventProcessing(event.getId());
    }

    private void startMockingEventProcessing(String eventId){
        Event event = eventService.getEventById(eventId, consumer.getTopicName());
        if(event == null)
            return;
        Random rand = new Random();
        int random = rand.nextInt(10000);
        processEventsWithFailure(random, eventId);
        handlePostProcessing(eventId, 0);
        int nextEventId = Integer.parseInt(eventId) + 1;
        startMockingEventProcessing(String.valueOf(nextEventId));
    }

    private void processEventsWithFailure(int randomSleepTime, String eventId){
        Random rand = new Random();
        int random = rand.nextInt(10);

        logger.info(String.valueOf(random));

        // Randomly failing 30% of the events.
        if(random < 3){
            logger.error("Processing failed for eventId - {} for Consumer - {} with retry count - {}.", eventId, consumer.getConsumerId(), consumer.getCurrRetry());
            if(consumer.getCurrRetry() < consumer.getMaxRetry()){
                handlePostProcessing(eventId, consumer.getCurrRetry() + 1);
                BackOffRetryService.waitUntilNextRetry(consumer.getCurrRetry(), consumer.getConsumerId());
                processEventsWithFailure(randomSleepTime, eventId);
            }
            else
                logger.error("Maximum number of retries exhausted for eventId - {} for consumer - {}. Moving to next event.", eventId, consumer.getConsumerId());
        }
        else {
            try {
                // sleeping for a random time between [0, 10) seconds mocking event processing.
                logger.info("Starting processing of eventId - {} for Consumer - {} with retry count - {}.", eventId, consumer.getConsumerId(), consumer.getCurrRetry());
                Thread.sleep(randomSleepTime);
                logger.info("Completed processing of eventId - {} for Consumer - {} with retry count - {}.", eventId, consumer.getConsumerId(), consumer.getCurrRetry());
            } catch (Exception ex){
                logger.error(ex.getMessage());
            }
        }
    }

    private void handlePostProcessing(String eventId, int currRetry){
        consumer.setCursor(eventId);
        consumer.setCurrRetry(currRetry);
        updateConsumer(consumer);
    }

    private void updateConsumer(Consumers consumer){
        Document document = objectMapper.convertValue(consumer, Document.class);
        mongoDAO.update(mongoDatabase.getCollection(CONSUMER_COLLECTION_NAME), consumer.getConsumerId(), document);
    }
}
