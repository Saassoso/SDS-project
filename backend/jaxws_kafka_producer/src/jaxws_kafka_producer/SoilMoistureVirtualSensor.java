package jaxws_kafka_producer;


import java.util.Random;

public class SoilMoistureVirtualSensor {
    private static boolean running = true;
    private static final SensorServiceImpl sensorService = new SensorServiceImpl();
    private static final Random random = new Random();
    private static int i = 0;

    public static void main(String[] args) {
        Thread sensorThread = new Thread(() -> {
            while (running) {
                double moisture = generateSoilMoistureData();
                sensorService.sendDataFromSensor("01", moisture);
                System.out.println("Soil moisture : " + moisture + "%");
                
                try {
                	// 5 seconds interval
                    Thread.sleep(5000);  
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        sensorThread.start();
    }

    private static double generateSoilMoistureData() {
        // soil moisture normal level : 50-80%
        double moisture = 50 + (30 * random.nextDouble());
        
        // error every 13 reading to differentiate between humdity error after 10 iterations
        i++;
        if (i % 13 == 0) {
            // Generate error => (20-95%)
            moisture = random.nextBoolean() ? 
                20 + (10 * random.nextDouble()) :  // Low error (20-30%)
                85 + (10 * random.nextDouble());   // High error (85-95%)
        }
        
        return Math.round(moisture * 10.00) / 10.00;  
    }
}