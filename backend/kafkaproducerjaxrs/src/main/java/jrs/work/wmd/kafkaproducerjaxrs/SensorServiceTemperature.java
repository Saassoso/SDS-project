package jrs.work.wmd.kafkaproducerjaxrs;

import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/sensor")
public class SensorServiceTemperature {
	 private boolean running = false;
	    private Thread dataThread;
	    private final Random random = new Random();
	    private final Producer<String, String> producer;
	    
	    
	    private final String topicTemp = "field.tomato.sensors.temperature";
	    public SensorServiceTemperature() {
	    	
	        Properties props = new Properties();
	        props.put("bootstrap.servers", "localhost:9092");
	        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

	        producer = new KafkaProducer<>(props);
	    }
	    @GET
	    @Path("/startTemperature")
	    @Produces(MediaType.TEXT_PLAIN)
	    public String startSendingData() {
	        if (running) {
	            return "Data is already being sent!";
	        }

	        running = true;
	        dataThread = new Thread(this::sendDataLoop);
	        dataThread.start();

	        return "Started sending temperature sensor data every 5 seconds!";
	    }

	    private void sendDataLoop() {
	        while (running) {
	            try {
	                String sensorId = "01";
	             
	                int temperature = generateTemperatureValue();

	               

	                // Construct and send Temperature message
	                String tempMessage = "{\"sensorId\": \"" + sensorId + "\", \"temperature\": " + temperature + "}";
	                producer.send(new ProducerRecord<>(topicTemp, sensorId, tempMessage));

	             
	                System.out.println("Sent Temperature: " + tempMessage);

	                Thread.sleep(5000);
	            } catch (Exception e) {
	                e.printStackTrace();
	                running = false;
	                break;
	            }
	        }
	    }


	    // Generates temperature values between 13°C and 29°C
	    private int generateTemperatureValue() {
	        return 13 + random.nextInt(17); // Ensures range [13, 29]
	    }
	}


	    

