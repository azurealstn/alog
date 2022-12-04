package com.azurealstn.alog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class AlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlogApplication.class, args);
	}

}
