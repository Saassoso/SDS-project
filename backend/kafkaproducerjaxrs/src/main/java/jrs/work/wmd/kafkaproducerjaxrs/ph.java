package jrs.work.wmd.kafkaproducerjaxrs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"ph"})
public class ph{
	private double ph;
	
	public ph() {}
	public ph(double ph) {
		this.setPh(ph);
		
		
	}
	public double getPh() {
		return ph;
	}
	public void setPh(double ph) {
		this.ph = ph;
	}
	

}