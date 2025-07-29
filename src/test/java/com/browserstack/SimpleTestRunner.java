package com.browserstack;

import com.codeborne.selenide.Configuration;

public class SimpleTestRunner {
    public static void main(String[] args) {
        try {
            // Set configuration for headless mode to avoid browser popup issues
            Configuration.headless = true;
            Configuration.browser = "chrome";
            Configuration.timeout = 10000;
            
            System.out.println("Starting LoginTest...");
            LoginTest loginTest = new LoginTest();
            loginTest.testValidEmailLogin();
            System.out.println("Test completed successfully!");
        } catch (Exception e) {
            System.out.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
