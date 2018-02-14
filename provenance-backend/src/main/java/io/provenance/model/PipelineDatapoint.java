package io.provenance.model;

public class PipelineDatapoint {
	
	private String id;
	private String successor;
	private Context context;
	
	public PipelineDatapoint(String id) {
		this.id = id;
	}
	
	public PipelineDatapoint(String id, Context context) {
		this.id = id;
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
	public String getSuccessor() {
		return successor;
	}
	public void setSuccessor(String next) {
		this.successor = next;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}