package io.provenance.model;

public class NodeStat {

	private String id;
	private String health;
	private double sendRate;
	private double receiveRate;
	
	public NodeStat(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
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