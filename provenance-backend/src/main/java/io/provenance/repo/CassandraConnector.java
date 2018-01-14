package io.provenance.repo;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import io.provenance.config.Config;

public class CassandraConnector {

	private static Cluster cluster;
	private static Session session;
	
	public static Session getSession() {
		if(cluster == null) 
			cluster = Cluster.builder().addContactPoint(Config.getDB_IP()).withPort(Config.getDB_Port()).build();
		if(session == null || session.isClosed())
			session = cluster.connect();
		return session;
	}
}
