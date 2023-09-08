package com.example.Service;

import com.example.dao.MongoDAO;
import com.example.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class EventService {

    private final MongoDatabase mongoDatabase;
    private final ObjectMapper objectMapper;
    private final MongoDAO mongoDAO;

    public EventService(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        objectMapper = new ObjectMapper();
        mongoDAO = new MongoDAO();
    }

    public void insertOne(Event event){
        Document document = objectMapper.convertValue(event, Document.class);
        mongoDAO.insertOne(mongoDatabase.getCollection("topics"), document);
    }
}
