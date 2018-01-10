package io.provenance.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private String id;
	private String name;
	private String successor;
	private List<String> predecessor;
	
	public Node(String id, String name, String next) {
		this.id = id;
		this.name = name;
		this.successor = next;
		this.predecessor = new ArrayList<String>();
	}
	
	public Node(String pre) {
		this.predecessor = new ArrayList<String>();
		this.predecessor.add(pre);
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

	public List<String> getPredecessor() {
		return predecessor;
	}
	public void setPredecessor(String pre) {
		this.predecessor.add(pre);
	}
}
