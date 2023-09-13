package com.example.observers.impl;

import com.example.Service.EventService;
import com.example.dao.MongoDAO;
import com.example.model.Consumers;
import com.example.model.Event;
import com.example.observers.DummyEventProcessing;
import com.example.observers.IObserver;
import com.google.inject.Inject;
import com.mongodb.client.MongoDatabase;

public class Observer implements IObserver {

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

    private void startProcessingEvent(String eventId){
        Event event = eventService.getEventById(eventId, consumer.getTopicName());
        if(event == null)
            return;
        Thread thread = new Thread(() -> new DummyEventProcessing(event, eventService, mongoDatabase, mongoDAO, consumer));
        thread.start();
        eventBeingProcessed = false;
    }
}