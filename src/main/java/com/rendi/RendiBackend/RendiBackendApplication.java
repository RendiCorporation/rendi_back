package com.rendi.RendiBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@ConfigurationPropertiesScan
@SpringBootApplication
@EnableScheduling
public class RendiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RendiBackendApplication.class, args);
	}

}
