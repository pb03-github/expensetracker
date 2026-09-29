package com.example.expensetracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Optional: Seed test data on application startup (for development only)
 * Set spring.app.load-test-data=true to enable
 */
@Component
public class TestDataLoader implements ApplicationRunner {
    
    @Value("${spring.app.load-test-data:false}")
    private boolean loadTestData;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (loadTestData) {
            loadTestData();
        }
    }
    
    private void loadTestData() {
        System.out.println("Loading test data...");
        // Test data loading would go here if needed
        // For now, data can be created via API endpoints
    }
}
