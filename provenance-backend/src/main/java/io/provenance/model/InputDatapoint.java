package io.provenance.model;

public class InputDatapoint {

	private Datapoint dp;
	private String contrIbution;
	
	public InputDatapoint(Datapoint dp, String contrIbution) {
		this.dp = dp;
		this.contrIbution = contrIbution;
	}

	public Datapoint getDp() {
		return dp;
	}
	public void setDp(Datapoint dp) {
		this.dp = dp;
	}

	public String getContrIbution() {
		return contrIbution;
	}
	public void setContrIbution(String contrIbution) {
		this.contrIbution = contrIbution;
	}
}