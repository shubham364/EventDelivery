package com.example.dao;

import com.example.Service.EventService;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.example.model.Constants.ID;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;

public class MongoDAO {

    private final static Logger logger = LoggerFactory.getLogger(MongoDAO.class);

    public void insertOne(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }

    public void insertMany(MongoCollection<Document> collection, List<Document> documents) {
        collection.insertMany(documents);
    }

    public List<Document> find(MongoCollection<Document> collection) {
        return collection.find().into(new ArrayList<>());
    }

    public List<Document> findByKey(MongoCollection<Document> collection, String key, String value) {
        return collection.find(eq(key, value)).into(new ArrayList<>());
    }

    public List<Document> findByCriteria(MongoCollection<Document> collection, String key, int lessThanValue, int greaterThanValue, int sortOrder) {
        List<Document> documents = new ArrayList<>();
        FindIterable iterable = collection.find(and(lt(key, lessThanValue),
                gt(key, greaterThanValue))).sort(new Document(key, sortOrder));
        iterable.into(documents);
        return documents;
    }

    public Document update(MongoCollection<Document> collection, String id, Document document){
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put(ID, id);
        collection.replaceOne(dbObject, document);
        return document;
    }

    public Document getLatestDoc(MongoCollection<Document> collection){
        List<Document> docs = collection.find().sort(descending("$natural")).limit(1).into(new ArrayList<>());
        return docs.get(0);
    }

    public void deleteOne(MongoCollection<Document> collection, String key, String value) {
        collection.deleteOne(eq(key, value));
    }
}
