package io.provenance.model;

import java.util.Date;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table("")
public class NodeHealth {

	@PrimaryKey
	private String id;
	private String parameter1;
	private String parameter2;
	private String status;
	
	public NodeHealth() {
		
	}
	public NodeHealth(String id,String parameter1,String parameter2,String status) {
		this.id = id;
		this.parameter1 = parameter1;
		this.parameter2 = parameter2;
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getParameter1() {
		return parameter1;
	}
	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}
	public String getParameter2() {
		return parameter2;
	}
	public void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
