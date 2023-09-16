package com.example.Service;

import com.example.dao.MongoDAO;
import com.example.model.Consumers;
import com.example.observers.Observable;
import com.example.observers.impl.Observer;
import com.example.request.CreateConsumerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.model.Constants.*;

public class ConsumerService {

    private final static Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @Inject
    private MongoDatabase mongoDatabase;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private MongoDAO mongoDAO;

    @Inject
    private EventService eventService;

    @Inject
    private Observable observable;

    public ConsumerService() {}

    public Consumers createConsumer(CreateConsumerRequest request){
        Consumers consumer = getConsumersPojo(request);
        Document document = objectMapper.convertValue(consumer, Document.class);
        logger.info("Adding a new Consumer. {}", consumer);
        mongoDAO.insertOne(mongoDatabase.getCollection(CONSUMER_COLLECTION_NAME), document);
        createObserver(consumer);
        return consumer;
    }

    private void createObserver(Consumers consumer){
        Observer observer = new Observer(consumer, eventService, mongoDatabase, mongoDAO);
        observable.subscribe(observer);
    }


    private Consumers getConsumersPojo(CreateConsumerRequest request){
        Consumers consumer = new Consumers();
        consumer.setConsumerId(request.getConsumerName());
        consumer.setTopicName(request.getTopicName());
        consumer.setMaxRetry(request.getMaxRetry());
        consumer.setCursor("0");
        consumer.setCurrRetry(0);
        return consumer;
    }

}
