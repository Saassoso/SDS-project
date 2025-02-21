package jaxws_kafka_producer.humidite;

import javax.xml.ws.Endpoint;

public class SensorPublisher{
    public static void main(String[] args) {
        String url = "http://localhost:8082/ws/tomato/humidity";
        Endpoint.publish(url, new SensorServiceImpl());
        System.out.println("SOAP service for humidity running at: " + url);
        new Thread(() -> HumidityVirtualSensor.main(args)).start();
    }
}
