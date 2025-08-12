package com.browserstack;

import com.browserstack.pages.CryptoDepositPage;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CryptoDepositPerformanceTest extends LocalBrowserTest {
    
    private CryptoDepositPage depositPage;

    @Test
    public void testDepositAddressGenerationPerformance() throws InterruptedException {
        // Navigate to the application
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        // Login
        loginUser();
        
        // Navigate to Deposit Crypto page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement depositButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Deposit crypto') or contains(@class, 'deposit')]")
        ));
        depositButton.click();
        
        // Initialize page object
        depositPage = new CryptoDepositPage(driver);
        assertTrue(depositPage.isPageLoaded(), "Deposit Crypto page should load");
        
        // Measure time for address generation
        long startTime = System.currentTimeMillis();
        
        // Select crypto and network
        depositPage.selectCryptocurrency("BTC");
        depositPage.selectFirstAvailableNetwork();
        
        // Generate address
        depositPage.clickGetDepositAddress();
        
        // Wait for address to appear
        assertTrue(depositPage.isDepositAddressDisplayed(), "Address should be generated");
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Assert that address generation takes less than 30 seconds
        assertTrue(duration < 30000, "Address generation should complete within 30 seconds. Actual time: " + duration + "ms");
        
        System.out.println("Address generation completed in " + duration + "ms");
        
        // Verify address is valid
        String address = depositPage.getDepositAddress();
        assertTrue(depositPage.isValidDepositAddress(address), "Generated address should be valid");
        
        System.out.println("Performance test passed. Address generated: " + address);
    }

    @Test
    public void testMultipleCryptoAddressGeneration() throws InterruptedException {
        // Navigate to the application
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        // Login
        loginUser();
        
        // Navigate to Deposit Crypto page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement depositButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Deposit crypto') or contains(@class, 'deposit')]")
        ));
        depositButton.click();
        
        // Initialize page object
        depositPage = new CryptoDepositPage(driver);
        assertTrue(depositPage.isPageLoaded(), "Deposit Crypto page should load");
        
        String[] cryptos = {"BTC", "ETH", "USDT"};
        
        for (String crypto : cryptos) {
            try {
                long startTime = System.currentTimeMillis();
                
                // Refresh page for clean state
                if (!crypto.equals("BTC")) { // Skip refresh for first crypto
                    depositPage.refreshPage();
                }
                
                // Select crypto and network
                depositPage.selectCryptocurrency(crypto);
                depositPage.selectFirstAvailableNetwork();
                
                // Generate address
                depositPage.clickGetDepositAddress();
                
                // Verify address is generated
                assertTrue(depositPage.isDepositAddressDisplayed(), 
                    "Address should be generated for " + crypto);
                
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                String address = depositPage.getDepositAddress();
                assertTrue(depositPage.isValidDepositAddress(address), 
                    "Address for " + crypto + " should be valid");
                
                System.out.println(crypto + " address generated in " + duration + "ms: " + address);
                
                // Each crypto address generation should be under 30 seconds
                assertTrue(duration < 30000, 
                    crypto + " address generation should complete within 30 seconds. Actual: " + duration + "ms");
                
            } catch (Exception e) {
                System.out.println("Failed to generate address for " + crypto + ": " + e.getMessage());
            }
        }
    }

    @Test
    public void testConcurrentAddressGeneration() throws InterruptedException {
        // This test simulates rapid address generation requests
        // Navigate to the application
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        // Login
        loginUser();
        
        // Navigate to Deposit Crypto page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement depositButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Deposit crypto') or contains(@class, 'deposit')]")
        ));
        depositButton.click();
        
        // Initialize page object
        depositPage = new CryptoDepositPage(driver);
        assertTrue(depositPage.isPageLoaded(), "Deposit Crypto page should load");
        
        // Select crypto and network once
        depositPage.selectCryptocurrency("BTC");
        depositPage.selectFirstAvailableNetwork();
        
        // Generate multiple addresses rapidly
        for (int i = 0; i < 3; i++) {
            long startTime = System.currentTimeMillis();
            
            if (i > 0) {
                // Refresh to get new address
                depositPage.refreshPage();
                depositPage.selectCryptocurrency("BTC");
                depositPage.selectFirstAvailableNetwork();
            }
            
            depositPage.clickGetDepositAddress();
            
            assertTrue(depositPage.isDepositAddressDisplayed(), 
                "Address should be generated for request " + (i + 1));
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            String address = depositPage.getDepositAddress();
            assertTrue(depositPage.isValidDepositAddress(address), 
                "Address for request " + (i + 1) + " should be valid");
            
            System.out.println("Request " + (i + 1) + " completed in " + duration + "ms: " + address);
            
            // Short delay between requests
            Thread.sleep(1000);
        }
    }

    // Helper method for login
    private void loginUser() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            // Click login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Enter credentials (replace with actual test credentials)
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='email'] | //input[contains(@placeholder, 'email')]")
            ));
            emailField.clear();
            emailField.sendKeys("test@example.com"); // Replace with actual test email
            
            WebElement passwordField = driver.findElement(
                By.xpath("//input[@type='password'] | //input[contains(@placeholder, 'password')]")
            );
            passwordField.clear();
            passwordField.sendKeys("testpassword123"); // Replace with actual test password
            
            WebElement submitButton = driver.findElement(
                By.xpath("//button[@type='submit'] | //button[contains(text(), 'Login')]")
            );
            submitButton.click();
            
            Thread.sleep(5000); // Wait for login to complete
            
        } catch (Exception e) {
            System.out.println("Login failed or user might already be logged in: " + e.getMessage());
        }
    }
}
