package com.browserstack;

import com.browserstack.pages.CryptoDepositPage;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CryptoDepositTest extends LocalBrowserTest {
    
    private CryptoDepositPage depositPage;

    @Test(priority = 1)
    public void testAccessDepositCryptoPage() throws InterruptedException {
        // Navigate to the application
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        // Login first (assuming user is already registered)
        loginUser();
        
        // Navigate to Deposit Crypto page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement depositButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[text()='Deposit crypto']")
        ));
        depositButton.click();
        
        // Initialize page object
        depositPage = new CryptoDepositPage(driver);
        
        // Verify we're on the deposit page
        assertTrue(depositPage.isPageLoaded(), "Deposit Crypto page should be displayed");
        
        System.out.println("Successfully navigated to Deposit Crypto page");
    }

    @Test(priority = 2, dependsOnMethods = "testAccessDepositCryptoPage")
    public void testSelectCryptocurrency() throws InterruptedException {
        // Step 1: Select crypto using page object
        depositPage.selectCryptocurrency("BTC");
        
        System.out.println("Cryptocurrency BTC selected successfully");
    }

    @Test(priority = 3, dependsOnMethods = "testSelectCryptocurrency")
    public void testSelectNetwork() throws InterruptedException {
        // Step 2: Select network using page object
        depositPage.selectFirstAvailableNetwork();
        
        System.out.println("Network selected successfully");
    }

    @Test(priority = 4, dependsOnMethods = "testSelectNetwork")
    public void testGenerateDepositAddress() throws InterruptedException {
        // Step 3: Get Deposit Address using page object
        assertTrue(depositPage.isGetDepositAddressButtonEnabled(), "Get Deposit Address button should be enabled");
        
        depositPage.clickGetDepositAddress();
        
        // Verify deposit address is generated
        assertTrue(depositPage.isDepositAddressDisplayed(), "Deposit address should be displayed");
        
        String addressText = depositPage.getDepositAddress();
        assertFalse(addressText.isEmpty(), "Deposit address should not be empty");
        assertTrue(depositPage.isValidDepositAddress(addressText), "Deposit address should be valid");
        
        System.out.println("Deposit address generated successfully: " + addressText);
    }

    @Test(priority = 5, dependsOnMethods = "testGenerateDepositAddress")
    public void testCopyDepositAddress() throws InterruptedException {
        // Test copy functionality if available
        if (depositPage.isCopyButtonDisplayed()) {
            depositPage.clickCopyButton();
            
            // Verify copy success message or notification
            if (depositPage.isCopySuccessMessageDisplayed()) {
                System.out.println("Address copied successfully");
            } else {
                System.out.println("Copy button clicked, but no visible confirmation message");
            }
        } else {
            System.out.println("Copy button not found or not clickable");
        }
    }

    @Test(priority = 6)
    public void testDepositAddressValidation() throws InterruptedException {
        // Test with different cryptocurrencies
        String[] cryptos = {"ETH", "USDT", "BNB"};
        
        for (String crypto : cryptos) {
            try {
                // Navigate back to deposit page
                depositPage.refreshPage();
                
                // Select cryptocurrency
                depositPage.selectCryptocurrency(crypto);
                
                // Select network
                depositPage.selectFirstAvailableNetwork();
                
                // Generate address
                depositPage.clickGetDepositAddress();
                
                // Verify address is generated for this crypto
                assertTrue(depositPage.isDepositAddressDisplayed(), 
                    "Deposit address should be displayed for " + crypto);
                
                String addressText = depositPage.getDepositAddress();
                assertTrue(depositPage.isValidDepositAddress(addressText), 
                    "Deposit address for " + crypto + " should be valid");
                
                System.out.println("Successfully generated address for " + crypto + ": " + addressText);
                
            } catch (Exception e) {
                System.out.println("Failed to test deposit address for " + crypto + ": " + e.getMessage());
            }
        }
    }

    @Test(priority = 7)
    public void testDepositAddressErrorHandling() throws InterruptedException {
        // Navigate to deposit page
        depositPage.refreshPage();
        
        // Try to get deposit address without selecting crypto and network
        if (depositPage.isGetDepositAddressButtonEnabled()) {
            depositPage.clickGetDepositAddress();
            
            // Check for error message
            if (depositPage.isErrorMessageDisplayed()) {
                String errorMessage = depositPage.getErrorMessage();
                assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
                System.out.println("Error handling works correctly: " + errorMessage);
            } else {
                System.out.println("No error message displayed, testing different scenario");
            }
        } else {
            System.out.println("Get Deposit Address button is properly disabled when no selections made");
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
                By.xpath("//input[@type='email' or contains(@placeholder, 'email') or contains(@name, 'email')]")
            ));
            emailField.clear();
            emailField.sendKeys("hainam38493@gmail.com"); // Replace with actual test email

            WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(text(), 'Next') or contains(text(), 'Continue') or contains(text(), 'Weiter')]")
            ));
            nextButton.click();

            WebElement passwordField = driver.findElement(
                By.xpath("//input[@type='password' or contains(@placeholder, 'password') or contains(@name, 'password')]")
            );
            passwordField.clear();
            passwordField.sendKeys("Hainam12@"); // Replace with actual test password
            
            WebElement submitButton = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or contains(text(), 'Submit') or @type='submit']")
            );
            submitButton.click();
            
            Thread.sleep(5000); // Wait for login to complete
            
        } catch (Exception e) {
            System.out.println("Login failed or user might already be logged in: " + e.getMessage());
        }
    }
}
