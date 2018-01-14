package io.provenance.model;

import java.util.ArrayList;
import java.util.List;

public class Datapoint {
	
	private String id;
	private List<InputDatapoint> inputDatapoints;
	private Context context;
		
	public Datapoint(String id, Context context) {
		this.id = id;
		this.context = context;
		this.inputDatapoints = new ArrayList<InputDatapoint>();
	}
	
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
	public List<InputDatapoint> getInputDatapoints() {
		return inputDatapoints;
	}
	public void setInputDatapoint(InputDatapoint inputDatapoint) {
		this.inputDatapoints.add(inputDatapoint);
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
