package com.adesso.tinaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.adesso")
public class TinabaApplication {
	public static void main(String[] args) {
		SpringApplication.run(TinabaApplication.class, args);
	}
}
