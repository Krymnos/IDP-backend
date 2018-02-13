package io.provenance.model;

import java.util.List;


public class ProvenanceResultSet {
	private boolean error;
	private List<Datapoint> datapoints;
	private String message;
	
	public ProvenanceResultSet(boolean error, List<Datapoint> datapoints) {
		this.error = error;
		this.datapoints = datapoints;
	}
	
	public ProvenanceResultSet(boolean error, String message) {
		this.error = error;
		this.message = message;
	}
	
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	
	public List<Datapoint> getDatapoints() {
		return datapoints;
	}
	public void setDatapoints(List<Datapoint> datapoints) {
		this.datapoints = datapoints;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}