package jaxws_kafka_producer;

import java.util.Properties;

import javax.jws.WebService;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

@WebService(endpointInterface = "jaxws_kafka_producer.SensorService")
public class SensorServiceImpl implements SensorService {

    private KafkaProducer<String, String> producer;
    private final String topic = "field.tomato.sensors.soilmoisture";

    public SensorServiceImpl() {
        // Kafka Producer Configuration
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public String sendSensorData(String sensorId, double soilmoisture) {
        String message = "{\"sensorId\": \"" + sensorId + "\", \"soilmoisture\": " + soilmoisture + "}";
        
        // Send data to Kafka
        producer.send(new ProducerRecord<>(topic, sensorId, message));
        
        return "Sensor data sent to Kafka: " + message;
    }

    public void sendDataFromSensor(String sensorId, double soilmoisture) {
        sendSensorData(sensorId, soilmoisture);
    }
}