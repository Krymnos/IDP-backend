package io.provenance.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import io.provenance.exception.ConfigParseException;

public class Config {

	private static  String DB_IP;
	private static int DB_PORT;
	private static String KEYSPACE;
	
	public static String getDB_IP() {
		return DB_IP;
	}
	
	public static int getDB_Port() {
		return DB_PORT;
	}
	
	public static String getKEYSPACE() {
		return KEYSPACE;
	}
	
	public static void loadFromFile(String configPath) throws ConfigParseException {
		InputStream inputStream = null;
		Properties properties = new Properties();
		try {
			inputStream = new FileInputStream(configPath);
			properties.loadFromXML(inputStream);
			DB_IP = properties.getProperty("DB_IP");
			DB_PORT = Integer.parseInt(properties.getProperty("DB_PORT"));
			KEYSPACE = properties.getProperty("KEYSPACE");
			inputStream.close();
		} catch(FileNotFoundException fnex) {
			throw new ConfigParseException("Problem in loading configuration file.");
		} catch(IOException ioex) {
			throw new ConfigParseException("Problem in parsing configuration file.");
		}
	}
}
