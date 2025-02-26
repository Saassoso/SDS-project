package jrs.work.wmd.kafkaproducerjaxrs;

import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/sensor")
public class SensorServiceph {
    private boolean running = false;
    private Thread dataThread;
    private final Random random = new Random();
    private final Producer<String, String> producer;
    
    private final String topicPh = "field.tomato.sensors.ph";
   

    public SensorServiceph() {
        // Kafka Producer Configuration
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    @GET
    @Path("/startph")
    @Produces(MediaType.TEXT_PLAIN)
    public String startSendingData() {
        if (running) {
            return "Data is already being sent!";
        }

        running = true;
        dataThread = new Thread(this::sendDataLoop);
        dataThread.start();

        return "Started sending ph sensor data every 5 seconds!";
    }

    private void sendDataLoop() {
        while (running) {
            try {
                String sensorId = "01";
                double ph = generatePhValue();
              

                String phMessage = "{\"sensorId\": \"" + sensorId + "\", \"ph\": " + ph + "}";
                producer.send(new ProducerRecord<>(topicPh, sensorId, phMessage));

             
                System.out.println("Sent PH: " + phMessage);
           

                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
                running = false;
                break;
            }
        }
    }

    private double generatePhValue() {
        double ph = 6.5 + random.nextGaussian() * 0.5;
        return Math.max(5.5, Math.min(ph, 7.5)); 
    }

 
}

