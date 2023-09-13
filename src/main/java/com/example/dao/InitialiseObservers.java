package com.example.dao;

import com.example.Service.EventService;
import com.example.model.Consumers;
import com.example.observers.Observable;
import com.example.observers.impl.Observer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.example.model.Constants.CONSUMER_COLLECTION_NAME;

public class InitialiseObservers {

    private final static Logger logger = LoggerFactory.getLogger(InitialiseObservers.class);

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private Observable observable;

    @Inject
    private MongoDatabase db;

    @Inject
    private MongoDAO mongoDAO;

    @Inject
    private EventService eventService;

    public void initialiseAllObserversOnStartUp(){
        MongoCollection<Document> collection = db.getCollection(CONSUMER_COLLECTION_NAME);
        List<Document> documents = collection.find().into(new ArrayList<>());
        for (Document document : documents){
            Consumers consumer = objectMapper.convertValue(document, Consumers.class);
            logger.info("Got consumer - {}",consumer.toString());
            Observer observer = new Observer(consumer, eventService, db, mongoDAO);
            observable.subscribe(observer);
        }
    }

}
