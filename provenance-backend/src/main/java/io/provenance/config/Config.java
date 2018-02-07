package io.provenance.config;

import io.provenance.exception.ConfigParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private static  String DB_IP;
	private static int DB_PORT;
	private static String KEYSPACE;
	private static String TABLE;
	
	public static String getDB_IP() {
		return DB_IP;
	}
	
	public static int getDB_Port() {
		return DB_PORT;
	}
	
	public static String getKEYSPACE() {
		return KEYSPACE;
	}
	
	public static String getTABLE() {
		return TABLE;
	}
	
	public static void loadFromFile(String configPath) throws ConfigParseException {
		InputStream inputStream = null;
		Properties properties = new Properties();
		try {
			if(System.getenv("CONF_LOC") != null && System.getenv("CONF_LOC").toUpperCase().equals("EVAR")){
				if(System.getenv("DB_IP") != null){
					DB_IP = System.getenv("DB_IP");
				}

				if(System.getenv("DB_PORT") != null){
					DB_PORT = Integer.parseInt(System.getenv("DB_PORT"));
				}

				if(System.getenv("KEYSPACE") != null){
					KEYSPACE = System.getenv("KEYSPACE");
				}

				if(System.getenv("TABLE") != null){
					TABLE = System.getenv("TABLE");
				}
			}
			else {
				inputStream = new FileInputStream(configPath);
				properties.loadFromXML(inputStream);
				DB_IP = properties.getProperty("DB_IP");
				DB_PORT = Integer.parseInt(properties.getProperty("DB_PORT"));
				KEYSPACE = properties.getProperty("KEYSPACE");
				TABLE = properties.getProperty("TABLE");
				inputStream.close();
			}
		} catch(FileNotFoundException fnex) {
			throw new ConfigParseException("Problem in loading configuration file.");
		} catch(IOException ioex) {
			throw new ConfigParseException("Problem in parsing configuration file.");
		}
	}
}