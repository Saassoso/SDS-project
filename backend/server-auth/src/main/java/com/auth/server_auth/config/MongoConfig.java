package com.auth.server_auth.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoConfig {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    
    public static MongoClient getMongoClient() {
        return MongoClients.create(CONNECTION_STRING);
    }
}
