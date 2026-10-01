package com.lipari.bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LipariBankApplication {

	private static final Logger log = LoggerFactory.getLogger(LipariBankApplication.class);

	public static void main(String[] args) {
		var context = SpringApplication.run(LipariBankApplication.class, args);

		Environment environment = context.getEnvironment();

		log.info(
				"Application started: {}",
				environment.getProperty("spring.application.name")
		);
	}

}
