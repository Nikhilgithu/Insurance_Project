package com.techlabs.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Application11 {

	public static void main(String[] args) {
		SpringApplication.run(Application11.class, args);
	}
	
}
