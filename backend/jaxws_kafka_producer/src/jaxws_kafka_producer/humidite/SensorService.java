package jaxws_kafka_producer.humidite;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface SensorService {
    @WebMethod
    String sendSensorData(String sensorId, double humidity);
}