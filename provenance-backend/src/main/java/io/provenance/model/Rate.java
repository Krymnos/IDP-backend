package io.provenance.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table("")
public class Rate {

	@PrimaryKey
	private String id;
	private String parameter1;
	private String parameter2;
	
	public Rate() {
		
	}
	public Rate(String id,String parameter1,String parameter2) {
		this.id = id;
		this.parameter1 = parameter1;
		this.parameter2 = parameter2;
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
	
}
