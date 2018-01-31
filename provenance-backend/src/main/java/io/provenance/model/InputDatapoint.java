package io.provenance.model;

public class InputDatapoint {

	private Datapoint dp;
	private String contribution;
	
	public InputDatapoint(Datapoint dp, String contribution) {
		this.dp = dp;
		this.contribution = contribution;
	}

	public Datapoint getDp() {
		return dp;
	}
	public void setDp(Datapoint dp) {
		this.dp = dp;
	}

	public String getContribution() {
		return contribution;
	}
	public void setContribution(String contribution) {
		this.contribution = contribution;
	}
}