package com.identlite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class IdentliteApplication {
	public static void main(String[] args) {
		SpringApplication.run(IdentliteApplication.class, args);
	}
}
