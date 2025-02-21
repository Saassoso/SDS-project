package jrs.work.wmd.kafkaproducerjaxrs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder({"temperature"})
public class temperature {
	private int temperature;
public temperature(int temperature) {
	this.setTemperature(temperature);
}
public int getTemperature() {
	return temperature;
}
public void setTemperature(int temperature) {
	this.temperature = temperature;
}
}
