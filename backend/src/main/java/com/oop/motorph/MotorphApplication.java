package com.oop.motorph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.oop.motorph"})
public class MotorphApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotorphApplication.class, args);
	}

}
