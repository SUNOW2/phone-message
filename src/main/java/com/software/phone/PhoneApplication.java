package com.software.phone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PhoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhoneApplication.class, args);
	}
}
