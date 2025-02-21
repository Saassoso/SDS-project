package io.conduktor.demos.kafka.Consummer;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBClient {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDBClient(String host, String dbName, String collectionName) {
        this.mongoClient = MongoClients.create("mongodb://" + host);
        this.database = mongoClient.getDatabase(dbName);
        this.collection = database.getCollection(collectionName);
    }

    public void insertData(String sensorId, String message) {
    	long timestamp = System.currentTimeMillis();
        Document document = new Document("sensorId", sensorId)
                .append("message", message).append("timestamp", timestamp);
        collection.insertOne(document);
    }
    public void close() {
        mongoClient.close();
    }
}
