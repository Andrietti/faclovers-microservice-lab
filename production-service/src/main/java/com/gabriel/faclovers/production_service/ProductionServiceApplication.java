package com.gabriel.faclovers.production_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ProductionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductionServiceApplication.class, args);
	}

}
