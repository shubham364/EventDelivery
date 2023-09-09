package com.example.Service;

import com.example.dao.MongoDAO;
import com.example.model.Constants;
import com.example.model.Event;
import com.example.request.EventRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;

import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;

import java.util.List;

public class EventService {

    private final MongoDatabase mongoDatabase;

    private final ObjectMapper objectMapper;

    private final MongoDAO mongoDAO;

    private static Integer ID = 0;

    public EventService(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        objectMapper = new ObjectMapper();
        mongoDAO = new MongoDAO();
    }

    public Event insertOne(EventRequest eventRequest, String collectionName){
        Event event = getEventPojo(eventRequest, collectionName);
        Document document = objectMapper.convertValue(event, Document.class);
        mongoDAO.insertOne(mongoDatabase.getCollection(collectionName), document);
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
            Event event = objectMapper.convertValue(document, Event.class);
            ID = Integer.parseInt(event.getId());
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
