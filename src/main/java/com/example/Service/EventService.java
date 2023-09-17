package com.example.Service;

import com.example.dao.MongoDAO;
import com.example.exception.EventDeliveryException;
import com.example.model.Constants;
import com.example.model.Event;
import com.example.observers.Observable;
import com.example.request.EventRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.List;

import static com.example.model.Constants.USER_ID;

public class EventService {

    private final static Logger logger = LoggerFactory.getLogger(EventService.class);

    @Inject
    private MongoDatabase mongoDatabase;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private MongoDAO mongoDAO;

    @Inject
    private Observable observable;

    private static Integer ID = 0;

    public EventService() {}

    public Event getEvent(String topicName, String userId) throws EventDeliveryException {
        MongoCollection<Document> collection = mongoDatabase.getCollection(topicName);
        if(collection == null){
            logger.error("Topic does not exist with topic name - {}", topicName);
            throw new EventDeliveryException("Topic does not exist.", Response.Status.BAD_REQUEST.getStatusCode());
        }
        List<Document> documents = mongoDAO.findByKey(collection, USER_ID, userId);
        if(CollectionUtils.isEmpty(documents)){
            logger.error("Event with userId {} does not exist.", userId);
            throw new EventDeliveryException("Event with userId does not exist.", Response.Status.BAD_REQUEST.getStatusCode());
        }
        return objectMapper.convertValue(documents.get(0), Event.class);
    }

    public Event publish(EventRequest eventRequest, String collectionName){
        Event event = getEventPojo(eventRequest, collectionName);
        Document document = objectMapper.convertValue(event, Document.class);
        mongoDAO.insertOne(mongoDatabase.getCollection(collectionName), document);
        observable.notifyObservers(event.getId(), collectionName);
        return event;
    }

    private Event getEventPojo(EventRequest eventRequest, String collectionName){
        Event event = new Event();
        event.setUserId(eventRequest.getUserId());
        event.setPayload(eventRequest.getPayload());
        event.setId(generateId(collectionName));
        return event;
    }

    private synchronized String generateId(String collectionName){
        if(ID == 0){
            Document document = mongoDAO.getLatestDoc(mongoDatabase.getCollection(collectionName));
            if(document != null) {
                Event event = objectMapper.convertValue(document, Event.class);
                ID = Integer.parseInt(event.getId());
            }
        }
        ID += 1;
        return ID.toString();
    }

    public Event getEventById(String id, String topicName){
        List<Document> documents = mongoDAO.findByKey(mongoDatabase.getCollection(topicName), Constants.ID, id);
        if(CollectionUtils.isNotEmpty(documents)){
            Document document = documents.get(0);
            return objectMapper.convertValue(document, Event.class);
        }
        return null;
    }

    public void createCollection(String collectionName){
        mongoDatabase.createCollection(collectionName);
    }
}
