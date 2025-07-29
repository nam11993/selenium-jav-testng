package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;
import java.util.List;

public class GoogleLoginTest extends LocalBrowserTest {

    @Test
    public void testGoogleLoginAuthentication() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000); // Wait for page to fully load
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        String originalWindow = driver.getWindowHandle();
        
        try {
            System.out.println("🌐 Starting Google Login Authentication Test");
            System.out.println("📍 Current URL: " + driver.getCurrentUrl());
            System.out.println("📄 Page title: " + driver.getTitle());
            
            // Step 1: Click Login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            ));
            System.out.println("✅ Found login button with text: " + loginButton.getText());
            loginButton.click();
            Thread.sleep(2000);
            System.out.println("📍 After login click - URL: " + driver.getCurrentUrl());
            
            // Step 2: Click Google login button/option
            WebElement googleLoginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class=\"h-12 w-12 flex items-center justify-center\"]")
            ));
            googleLoginButton.click();
            Thread.sleep(2000);


            // input email and password 
            WebElement inputEmailGoogle = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='email']")
            ));
            inputEmailGoogle.sendKeys("hainam11993@gmail.com");
            
            WebElement nextButtonGoogle = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Next']")
            ));
            nextButtonGoogle.click();
            Thread.sleep(2000);

            WebElement inputPasswordGoogle = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='password']")
            ));
            inputPasswordGoogle.sendKeys("Hainam12@");

            nextButtonGoogle.click();
            Thread.sleep(2000);
            
            
            
            
            
            // Step 9: Verify test completion
            String finalUrl = driver.getCurrentUrl();
            boolean isGooglePage = finalUrl.contains("google.com") || finalUrl.contains("accounts.google");
            System.out.println("✅ Google Login Authentication Test completed");
            assertTrue(isGooglePage, "Should have reached Google authentication page");
            
        } catch (Exception e) {
            System.out.println("❌ Error during Google login test: " + e.getMessage());
            System.out.println("📍 Current URL at error: " + driver.getCurrentUrl());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Test
    public void testGoogleLoginButtonPresence() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            System.out.println("🔍 Testing Google Login Button Presence");
            
            // Click Login button first
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Check if Google login option is available
            boolean googleOptionFound = false;
            String[] googleSelectors = {
                "//button[contains(@class, 'google') or contains(text(), 'Google')]",
                "//button[contains(@class, 'oauth-google')]",
                "//a[contains(@href, 'google') or contains(text(), 'Google')]",
                "//*[contains(text(), 'Continue with Google')]",
                "//*[contains(text(), 'Sign in with Google')]",
                "//*[contains(text(), 'Login with Google')]"
            };
            
            for (String selector : googleSelectors) {
                try {
                    WebElement googleElement = driver.findElement(By.xpath(selector));
                    if (googleElement.isDisplayed()) {
                        googleOptionFound = true;
                        System.out.println("✅ Google login option found: " + googleElement.getText());
                        System.out.println("🔍 Element class: " + googleElement.getAttribute("class"));
                        break;
                    }
                } catch (Exception e) {
                    // Continue searching with next selector
                }
            }
            
            if (googleOptionFound) {
                System.out.println("✅ Google login integration is available on the website");
            } else {
                System.out.println("❌ Google login option not found on the login page");
                
                // Print available authentication options for debugging
                System.out.println("🔍 Available authentication options:");
                List<WebElement> authElements = driver.findElements(By.xpath("//*[contains(text(), 'login') or contains(text(), 'sign') or contains(@class, 'auth') or contains(@class, 'oauth')]"));
                for (WebElement element : authElements) {
                    System.out.println("  - " + element.getTagName() + ": '" + element.getText() + "' | Class: '" + element.getAttribute("class") + "'");
                }
            }
            
            assertTrue(googleOptionFound, "Google login option should be available on the login page");
            
        } catch (Exception e) {
            System.out.println("❌ Error checking Google login button presence: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Test
    public void testGoogleLoginFlow() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🔄 Testing Complete Google Login Flow");
            
            // Step 1: Navigate to login
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Step 2: Find and click Google login
            WebElement googleLoginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'google') or contains(text(), 'Google')] | //a[contains(@href, 'google') or contains(text(), 'Google')] | //*[contains(text(), 'Continue with Google')] | //*[contains(text(), 'Sign in with Google')]")
            ));
            
            String originalWindow = driver.getWindowHandle();
            googleLoginButton.click();
            System.out.println("🚀 Initiated Google login flow");
            
            Thread.sleep(5000); // Wait for Google auth page to load
            
            // Step 3: Handle window switching if needed
            Set<String> allWindows = driver.getWindowHandles();
            if (allWindows.size() > 1) {
                for (String windowHandle : allWindows) {
                    if (!windowHandle.equals(originalWindow)) {
                        driver.switchTo().window(windowHandle);
                        break;
                    }
                }
            }
            
            // Step 4: Verify Google auth page elements
            String currentUrl = driver.getCurrentUrl();
            System.out.println("📍 Current URL in auth flow: " + currentUrl);
            
            boolean onGoogleAuthPage = currentUrl.contains("accounts.google.com") || 
                                     currentUrl.contains("google.com") ||
                                     driver.getPageSource().toLowerCase().contains("google");
            
            if (onGoogleAuthPage) {
                System.out.println("✅ Successfully reached Google authentication page");
                
                // Look for key Google auth elements
                boolean hasEmailField = !driver.findElements(By.xpath("//input[@type='email' or @id='identifierId']")).isEmpty();
                boolean hasGoogleBranding = driver.getPageSource().toLowerCase().contains("google");
                boolean hasSecureConnection = currentUrl.startsWith("https://");
                
                System.out.println("🔐 Email field present: " + hasEmailField);
                System.out.println("🏢 Google branding present: " + hasGoogleBranding);
                System.out.println("🔒 Secure connection (HTTPS): " + hasSecureConnection);
                
                assertTrue(hasEmailField, "Google auth page should have email input field");
                assertTrue(hasGoogleBranding, "Page should contain Google branding");
                assertTrue(hasSecureConnection, "Google auth should use HTTPS");
                
            } else {
                System.out.println("❌ Did not reach Google authentication page");
                System.out.println("📍 Actual URL: " + currentUrl);
                // Take screenshot or log page source for debugging
                System.out.println("🔍 Page title: " + driver.getTitle());
            }
            
            // Step 5: Return to original window
            if (allWindows.size() > 1) {
                driver.close();
                driver.switchTo().window(originalWindow);
                System.out.println("🔙 Returned to original application window");
            }
            
            assertTrue(onGoogleAuthPage, "Should successfully navigate to Google authentication page");
            System.out.println("✅ Google login flow test completed successfully");
            
        } catch (Exception e) {
            System.out.println("❌ Error in Google login flow test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // Helper method to verify Google authentication redirect
    private boolean verifyGoogleAuthRedirect(String url) {
        return url.contains("accounts.google.com") ||
               url.contains("oauth2/auth") ||
               url.contains("google.com/oauth") ||
               url.contains("googleapis.com");
    }
    
    // Helper method to check for Google OAuth parameters
    private boolean hasGoogleOAuthParameters(String url) {
        return url.contains("client_id=") ||
               url.contains("response_type=") ||
               url.contains("scope=") ||
               url.contains("redirect_uri=");
    }
}
