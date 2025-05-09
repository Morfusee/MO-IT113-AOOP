package com.oop.motorph.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class DatabaseConfig {

    @Bean
    CommandLineRunner databaseConfigRunner() {
        return args -> {
        };
    }
}
