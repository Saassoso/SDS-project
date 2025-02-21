package jaxws_kafka_producer;

import javax.xml.ws.Endpoint;

public class SensorPublisher{
    public static void main(String[] args) {
        String url = "http://localhost:8081/ws/tomato/soil-moisture";
        Endpoint.publish(url, new SensorServiceImpl());
        System.out.println("SOAP service for soil-moisture running at: " + url);
        
        // Start Virtual Sensor
        new Thread(() -> SoilMoistureVirtualSensor.main(args)).start();
    }
}
