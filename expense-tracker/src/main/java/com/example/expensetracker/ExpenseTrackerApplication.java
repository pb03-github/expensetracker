package com.example.expensetracker;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Spring Boot Application
 */
@SpringBootApplication
public class ExpenseTrackerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackerApplication.class, args);
    }
    
    /**
     * Configure OpenAPI documentation
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Expense Tracker API")
                .version("1.0.0")
                .description("Backend-first personal expense tracker. V1 is a Java 21 / Spring Boot modular monolith backed by PostgreSQL (or H2 for local development).")
                .contact(new Contact()
                    .name("Support")
                    .url("https://github.com"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT")));
    }
}
