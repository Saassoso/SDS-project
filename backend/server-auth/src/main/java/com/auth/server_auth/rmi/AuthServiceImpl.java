package com.auth.server_auth.rmi;

import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService {
    private static final long serialVersionUID = 1L;
    private MongoCollection<Document> usersCollection;
    
    public AuthServiceImpl() throws RemoteException {
        super();
        initializeDatabase();
    }
    
    private void initializeDatabase() {
        try {
         
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            
            MongoDatabase database = mongoClient.getDatabase("authDB");
            
            boolean collectionExists = database.listCollectionNames()
                .into(new java.util.ArrayList<>())
                .contains("users");
                
            if (!collectionExists) {
                database.createCollection("users");
                System.out.println("Created 'users' collection");
                usersCollection = database.getCollection("users");
                IndexOptions indexOptions = new IndexOptions().unique(true);
                usersCollection.createIndex(new Document("username", 1), indexOptions);
                System.out.println("Created unique index on username field");
            } else {
                usersCollection = database.getCollection("users");
                System.out.println("Connected to existing 'users' collection");
            }
            
            usersCollection.countDocuments();
            System.out.println("Successfully connected to MongoDB");
            
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        Document user = usersCollection.find(Filters.eq("username", username)).first();
        return user != null && user.getString("password").equals(hashPassword(password));
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        try {
            if (usersCollection.find(Filters.eq("username", username)).first() != null) {
                return false;
            }
            usersCollection.insertOne(new Document("username", username)
                                    .append("password", hashPassword(password)));
            return true;
        } catch (Exception e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}