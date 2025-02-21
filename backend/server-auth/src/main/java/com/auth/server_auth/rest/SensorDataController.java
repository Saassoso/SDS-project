package com.auth.server_auth.rest;

import com.auth.server_auth.config.MongoConfig;
import com.mongodb.client.*;
import org.bson.Document;
import org.json.JSONObject; // Import for JSON parsing
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Path("/sensors")
public class SensorDataController {
	
	
	private String convertTimestamp(long timestamp) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC")); 
	    return sdf.format(new Date(timestamp));
	}

    @GET
    @Path("/fieldData")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFieldData() {
        try {
            MongoClient mongoClient = MongoConfig.getMongoClient();
            MongoDatabase database = mongoClient.getDatabase("sensorDB");
            MongoCollection<Document> collection = database.getCollection("sensorData");

            List<Document> rawData = collection.find()
                .sort(new Document("timestamp", -1))
                .limit(100)
                .into(new ArrayList<>());

            List<Document> processedData = new ArrayList<>();

            for (Document doc : rawData) {
                Document processedDoc = new Document();

                // Convert timestamp to readable format
                if (doc.containsKey("timestamp")) {
                    long rawTimestamp = doc.getLong("timestamp");
                    processedDoc.append("timestamp", convertTimestamp(rawTimestamp));
                }

                // Parse the message field
                if (doc.containsKey("message")) {
                    try {
                        JSONObject jsonMessage = new JSONObject(doc.getString("message"));
                        System.out.println("Raw MongoDB Data: " + doc.toJson());

                        if (jsonMessage.has("temperature")) {
                            processedDoc.append("temperature", jsonMessage.getDouble("temperature"));
                        }
                        if (jsonMessage.has("ph")) {
                            processedDoc.append("ph", jsonMessage.getDouble("ph"));
                        }
                        if (jsonMessage.has("humidity")) {
                            processedDoc.append("humidity", jsonMessage.getDouble("humidity"));
                        }
                        if (jsonMessage.has("soilmoisture")) {
                            processedDoc.append("soilmoisture", jsonMessage.getDouble("soilmoisture"));
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                processedData.add(processedDoc);
            }

               
            return Response.ok(processedData).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500)
                .entity("Error fetching sensor data")
                .build();
        }
    }
}
