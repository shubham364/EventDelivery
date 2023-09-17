package com.example.observers.impl;

import com.example.Service.EventService;
import com.example.dao.MongoDAO;
import com.example.model.Consumers;
import com.example.model.Event;
import com.example.observers.DummyEventProcessing;
import com.example.observers.IObserver;
import com.google.inject.Inject;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Observer implements IObserver {

    private final static Logger logger = LoggerFactory.getLogger(Observer.class);

    private final Consumers consumer;

    private final EventService eventService;

    private final MongoDAO mongoDAO;

    private final MongoDatabase mongoDatabase;

    private boolean eventBeingProcessed;

    @Inject
    public Observer(Consumers consumer, EventService eventService, MongoDatabase mongoDatabase, MongoDAO mongoDAO) {
        this.consumer = consumer;
        this.eventService = eventService;
        this.mongoDatabase = mongoDatabase;
        this.mongoDAO = mongoDAO;
        eventBeingProcessed = false;
    }

    @Override
    public boolean isProcessingEvent() {
        return eventBeingProcessed;
    }

    @Override
    public String getSubscribedTopicName() {
        return consumer.getTopicName();
    }

    @Override
    public void relayEvent(String eventId) {
        eventBeingProcessed = true;
        startProcessingEvent(eventId);
    }

    @Override
    public void restartEventProcessing() {
        eventBeingProcessed = true;
        startProcessingEvent(consumer.getCursor());
    }

    @Override
    public String getIdentifier() {
        return consumer.toString();
    }

    private void startProcessingEvent(String eventId){
        Event event = eventService.getEventById(eventId, consumer.getTopicName());
        if(event == null) {
            eventBeingProcessed = false;
            return;
        }
        Thread thread = new Thread(new DummyEventProcessing(event, eventService, mongoDatabase, mongoDAO, consumer));
        thread.start();
        eventBeingProcessed = false;
    }
}
