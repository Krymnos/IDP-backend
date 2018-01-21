package io.provenance.model;

public class Node {
	
	private String id;
	private String name;
	private String successor;
	
	public Node(String id, String name, String next) {
		this.id = id;
		this.name = name;
		this.successor = next;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSuccessor() {
		return successor;
	}
	public void setSuccessor(String next) {
		this.successor = next;
	}
}