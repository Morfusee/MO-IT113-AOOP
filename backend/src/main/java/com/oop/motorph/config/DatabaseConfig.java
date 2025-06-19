package com.oop.motorph.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for database-related settings.
 * This class is responsible for setting up and running database configurations
 * when the Spring application starts.
 */
@Configuration
public class DatabaseConfig {

    /**
     * Defines a CommandLineRunner bean for database configuration.
     * This method is executed once the application context is loaded,
     * allowing for tasks such as initial database setup or migrations.
     *
     * @return A CommandLineRunner instance that can perform database-related
     *         operations.
     */
    @Bean
    CommandLineRunner databaseConfigRunner() {
        return args -> {
            // No specific database configuration logic is implemented here yet.
            // This can be extended to include database initialization, migrations,
            // or other setup tasks as needed for the application.
        };
    }
}