package io.provenance.model;

public class NodeStat {

	private String id;
	private String color;
	private double sendRate;
	private double receiveRate;
	
	public NodeStat(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public double getSendRate() {
		return sendRate;
	}
	public void setSendRate(double sendRate) {
		this.sendRate = sendRate;
	}
	
	public double getReceiveRate() {
		return receiveRate;
	}
	public void setReceiveRate(double receiveRate) {
		this.receiveRate = receiveRate;
	}
}