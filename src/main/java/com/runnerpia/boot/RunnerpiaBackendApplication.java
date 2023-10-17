package com.runnerpia.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RunnerpiaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunnerpiaBackendApplication.class, args);
	}

}
