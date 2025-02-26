package io.conduktor.demos.kafka.Consummer;
import org.json.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consummer {

    private static final Logger log = LoggerFactory.getLogger(Consummer.class);
    private final KafkaConsumer<String, String> consumer;
    private final MongoDBClient mongoDBClient;
    private final String tcpHost;
    private final int tcpPort;
    private volatile boolean running = true;  

    public Consummer(String bootstrapServers, String groupId, String[] topics, String mongoHost, String dbName, String collectionName, String tcpHost, int tcpPort) {
        Properties properties = KafkaConsumerConfig.createConsumerConfig(bootstrapServers, groupId);
        consumer = new KafkaConsumer<>(properties);
        mongoDBClient = new MongoDBClient(mongoHost, dbName, collectionName);
        this.tcpHost = tcpHost;
        this.tcpPort = tcpPort;

        consumer.subscribe(Arrays.asList(topics));
    }

    public void startConsuming() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    log.info("Consumed message from topic: " + record.topic());
                    log.info("Key: " + record.key() + ", Value: " + record.value());
                    log.info("Partition: " + record.partition() + ", Offset: " + record.offset());

                    mongoDBClient.insertData(record.key(), record.value());


                    if (record.topic().equals("field.tomato.sensors.temperature")) {
                        try {
                            JSONObject json = new JSONObject(record.value()); 
                            if (json.has("temperature")) {
                                double temp = json.getDouble("temperature");
                                if ((temp > 30 || temp < 15)) { 
                                    sendAlert("Critical temperature detected: " + temp + "°C");
                                }
                            }

                            if (json.has("ph")) {
                                double ph = json.getDouble("ph");
                                if ((ph < 6.0 || ph > 7.0)) { 
                                    sendAlert("Abnormal pH level detected: " + ph);
                                }
                            }

                            if (json.has("humidity")) {
                                double humidity = json.getDouble("humidity");
                                if ((humidity < 30 || humidity > 40)||(humidity < 80 || humidity > 90) ) { 
                                    sendAlert("Abnormal humidity detected: " + humidity + "%");
                                }
                            }

                            if (json.has("soilmoisture")) {
                                double soilMoisture = json.getDouble("soilmoisture");
                                if ((soilMoisture < 30 || soilMoisture > 40)||(soilMoisture < 80 || soilMoisture > 90)) { 
                                    sendAlert("Abnormal soil moisture detected: " + soilMoisture + "%");
                                }
                            }
                         
                        } catch (Exception e) {
                            log.error("Error parsing temperature JSON: " + record.value(), e);
                        }
                    }
                }
                consumer.commitSync(); 
            }
        } catch (WakeupException e) {
            log.info("Consumer shutting down...");
        } catch (Exception e) {
            log.error("Unexpected error occurred.", e);
        } finally {
            consumer.close();
            mongoDBClient.close();
        }
    }

    private void sendAlert(String message) {
        try (
        	Socket socket = new Socket(tcpHost, tcpPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
            log.info("TCP Alert Sent: " + message);
        } catch (IOException e) {
            log.error("Failed to send TCP alert", e);
        }
    }

    public static void main(String[] args) {
        String[] topics = {"field.tomato.sensors.temperature", "field.tomato.sensors.ph", "field.tomato.sensors.humidity", "field.tomato.sensors.soilmoisture"};

        Consummer c = new Consummer(
                "localhost:9092",
                "tomato-data",
                topics,
                "localhost",
                "sensorDB",
                "sensorData",
                "localhost",
                5001 
        );

        c.startConsuming();
    }
}

