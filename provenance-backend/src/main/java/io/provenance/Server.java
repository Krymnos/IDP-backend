package io.provenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.provenance.config.Config;
import io.provenance.exception.ConfigParseException;

@SpringBootApplication
@ComponentScan("io.provenance")
public class Server {

	public static void main(String[] args) throws ConfigParseException {
		Config.loadFromFile(args[0]);
		SpringApplication.run(Server.class, args);
	}
}
