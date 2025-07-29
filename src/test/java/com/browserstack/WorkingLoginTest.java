package com.browserstack;

import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
import static org.testng.Assert.assertTrue;

public class WorkingLoginTest extends LocalBrowserTest {

    private final String BASE_URL = "https://fcex-fe-718949727112.asia-southeast1.run.app/en";

    @Test
    public void testValidEmailLogin() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            // Step 1: Click Login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            ));
            System.out.println("✅ Found login button: " + loginButton.getText());
            loginButton.click();
            Thread.sleep(2000);
            
            // Step 2: Enter valid email
            WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='email' or contains(@placeholder, 'email') or contains(@name, 'email')]")
            ));
            emailInput.clear();
            emailInput.sendKeys("hainam38493@gmail.com");
            System.out.println("✅ Entered email");
            
            // Check if this is ZITADEL multi-step login (look for Next/Continue button)
            List<WebElement> nextButtons = driver.findElements(
                By.xpath("//button[contains(text(), 'Next') or contains(text(), 'Continue') or contains(text(), 'Weiter')]")
            );
            
            if (!nextButtons.isEmpty()) {
                System.out.println("🔄 Detected ZITADEL multi-step login - clicking Next/Continue button");
                nextButtons.get(0).click();
                Thread.sleep(3000);
                System.out.println("✅ Clicked Next/Continue button");
            }
            
            // Step 3: Enter password (should be available now after Next click if multi-step)
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='password' or contains(@placeholder, 'password') or contains(@name, 'password')]")
            ));
            passwordInput.clear();
            passwordInput.sendKeys("Test@123456");
            System.out.println("✅ Entered password");
            
            // Step 4: Click login/sign in button
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or contains(text(), 'Submit') or @type='submit']")
            ));
            signInButton.click();
            System.out.println("✅ Clicked sign in button");
            Thread.sleep(5000);
            
            // Step 5: Verify successful login
            String currentUrl = driver.getCurrentUrl();
            System.out.println("📍 URL after login: " + currentUrl);
            
            // Check for indicators of successful login
            boolean loginSuccessful = currentUrl.contains("/dashboard") || 
                                    currentUrl.contains("/account") ||
                                    currentUrl.contains("/profile") ||
                                    driver.getPageSource().toLowerCase().contains("logout") ||
                                    driver.getPageSource().toLowerCase().contains("welcome");
            
            assertTrue(loginSuccessful, "Should be logged in successfully");
            System.out.println("✅ Login successful verification passed");
            
            // Step 6: Handle 2FA Setup after successful login
            handle2FASetupAfterLogin(wait);
            
        } catch (Exception e) {
            System.out.println("❌ Error during valid email login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void handle2FASetupAfterLogin(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔐 Handling 2FA Setup after successful login...");
        
        try {
            // Wait a moment for any redirects or dialogs to appear
            Thread.sleep(3000);
            
            // Check if 2FA setup dialog appears
            List<WebElement> twoFactorDialog = driver.findElements(By.xpath(
                "//*[contains(text(), 'Set up 2-Factor')] | " +
                "//*[contains(text(), '2-Factor')] | " +
                "//*[contains(text(), 'Two-Factor Authentication')]"
            ));
            
            if (twoFactorDialog.size() > 0) {
                System.out.println("✅ 2FA setup dialog detected");
                
                // Look for Skip option first
                List<WebElement> skipElements = driver.findElements(By.xpath(
                    "//button[contains(text(), 'Skip')] | " +
                    "//a[contains(text(), 'Skip')] | " +
                    "//*[contains(@class, 'skip')]"
                ));
                
                if (skipElements.size() > 0) {
                    skipElements.get(0).click();
                    Thread.sleep(2000);
                    System.out.println("✅ Skipped 2FA setup");
                } else {
                    System.out.println("⚠️ No skip option found, trying to handle 2FA setup");
                    // Handle other 2FA options if needed
                }
                
            } else {
                System.out.println("ℹ️ No 2FA setup dialog found - login complete");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Error during 2FA handling: " + e.getMessage());
            // Don't throw exception as 2FA might be optional
        }
    }
}
