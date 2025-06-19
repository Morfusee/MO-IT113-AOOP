package com.oop.motorph.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Spring Security and CORS settings.
 * This class sets up password encoding, security filters, and Cross-Origin
 * Resource Sharing (CORS) rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines a BCryptPasswordEncoder bean for password encoding.
     * This encoder is used to securely hash passwords.
     *
     * @return A PasswordEncoder instance using BCrypt.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain.
     * This method disables CSRF, allows all requests, and disables frame options
     * for H2 console.
     *
     * @param http The HttpSecurity object to configure.
     * @return A SecurityFilterChain instance.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF protection.
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll() // Allow all requests without authentication.
                ).headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // Disable
                                                                                                    // X-Frame-Options
                                                                                                    // for H2 Console.
        return http.build();
    }

    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings.
     * This bean allows requests from a specific origin (http://localhost:5173) and
     * defines allowed methods and headers.
     *
     * @return A WebMvcConfigurer instance with CORS configurations.
     */
    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply CORS to all endpoints.
                        .allowedOrigins("http://localhost:5173") // Allow requests from this origin.
                        .allowedMethods("GET", "POST", "PATCH", "DELETE") // Specify allowed HTTP methods.
                        .allowedHeaders("*") // Allow all headers.
                        .allowCredentials(true) // Allow sending of cookies and authorization headers.
                        .exposedHeaders("Content-Disposition", "Filename"); // Expose specific headers to the client.
            }
        };
    }
}