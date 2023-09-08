package com.example.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class MongoDAO {

    public void insertOne(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }

    public void insertMany(MongoCollection<Document> collection, List<Document> documents) {
        collection.insertMany(documents);
    }

    public List<Document> find(MongoCollection<Document> collection) {
        return collection.find().into(new ArrayList<Document>());
    }

    public List<Document> findByKey(MongoCollection<Document> collection, String key, String value) {
        return collection.find(eq(key, value)).into(new ArrayList<Document>());
    }

    public List<Document> findByCriteria(MongoCollection<Document> collection, String key, int lessThanValue, int greaterThanValue, int sortOrder) {
        List<Document> documents = new ArrayList<>();
        FindIterable iterable = collection.find(and(lt(key, lessThanValue),
                gt(key, greaterThanValue))).sort(new Document(key, sortOrder));
        iterable.into(documents);
        return documents;
    }

    public void deleteOne(MongoCollection<Document> collection, String key, String value) {
        collection.deleteOne(eq(key, value));
    }
}
