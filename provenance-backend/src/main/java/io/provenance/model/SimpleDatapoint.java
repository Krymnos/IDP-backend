package io.provenance.model;

import java.util.List;

public class SimpleDatapoint {
	private String id;
	private List<String> inputDatapoints;
	
	public SimpleDatapoint(String id, List<String> inputDatapoints) {
		this.id = id;
		this.inputDatapoints = inputDatapoints;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getInputDatapoints() {
		return inputDatapoints;
	}
	public void setInputDatapoints(List<String> inputDatapoints) {
		this.inputDatapoints = inputDatapoints;
	}
	
	
}
