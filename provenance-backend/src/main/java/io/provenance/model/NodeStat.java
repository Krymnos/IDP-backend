package io.provenance.model;

public class NodeStat {

	private String id;
	private String nodeHealth;
	private String provenanceDaemonHealth;
	private String pipelineDaemonHealth;
	private String outgoingChannelHealth;
	private double sendRate;
	private double receiveRate;
	
	public NodeStat(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public String getNodeHealth() {
		return nodeHealth;
	}

	public void setNodeHealth(String nodeHealth) {
		this.nodeHealth = nodeHealth;
	}

	public String getProvenanceDaemonHealth() {
		return provenanceDaemonHealth;
	}

	public void setProvenanceDaemonHealth(String provenanceDaemonHealth) {
		this.provenanceDaemonHealth = provenanceDaemonHealth;
	}

	public String getPipelineDaemonHealth() {
		return pipelineDaemonHealth;
	}

	public void setPipelineDaemonHealth(String pipelineDaemonHealth) {
		this.pipelineDaemonHealth = pipelineDaemonHealth;
	}
	
	public String getOutgoingChannelHealth() {
		return outgoingChannelHealth;
	}

	public void setOutgoingChannelHealth(String outgoingChannelHealth) {
		this.outgoingChannelHealth = outgoingChannelHealth;
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