package com.software.phone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.software.phone.domain")
public class PhoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhoneApplication.class, args);
	}
}
