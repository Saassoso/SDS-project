package jaxws_kafka_producer.humidite;


import java.util.Random;

public class HumidityVirtualSensor {
    private static boolean l = true;
    private static final SensorServiceImpl sensorService = new SensorServiceImpl();
    private static final Random random = new Random();
    private static int i = 0;

    public static void main(String[] args) {
        Thread sensorThread = new Thread(() -> {
            while (l) {
                double humidity = generateHumidityData();
                sensorService.sendDataFromSensor("01", humidity);
                System.out.println("Humidity : " + humidity + "%");      
                try {
                    Thread.sleep(5000);  
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        sensorThread.start();
    }

    private static double generateHumidityData() {
        double humidity = 50 + (20 * random.nextDouble());
        i++;
        if (i % 10 == 0) {
            humidity = random.nextBoolean() ? 
                30 + (10 * random.nextDouble()) :  
                80 + (10 * random.nextDouble());  
        }     
        return Math.round(humidity * 10.00) / 10.00;  
    }
}