package com.thenerdyaditya.springisawesome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringIsAwesomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringIsAwesomeApplication.class, args);
	}

}
