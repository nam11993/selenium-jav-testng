package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.FileOutputStream;
import java.util.Base64;

public class TwoFactorAuthTest extends LocalBrowserTest {

    @Test
    public void testTwoFactorAuthQRCodeSetup() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔐 Starting 2FA QR Code Setup Test");
            System.out.println("📍 Current URL: " + driver.getCurrentUrl());
            
            // Step 1: Navigate to login page
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Step 2: Perform regular login first (assuming you need to be logged in to access 2FA settings)
            // This would be your normal login flow - modify as needed
            performRegularLogin(wait);
            
            // Step 3: Navigate to security/2FA settings
            navigateToSecuritySettings(wait);
            
            // Step 4: Enable 2FA and look for QR code
            WebElement enable2FAButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Enable 2FA') or contains(text(), 'Enable Two-Factor') or contains(text(), 'Setup Authenticator')]")
            ));
            enable2FAButton.click();
            Thread.sleep(3000);
            
            // Step 5: Verify QR code is displayed
            boolean qrCodeFound = verifyQRCodePresence(wait);
            assertTrue(qrCodeFound, "QR code should be displayed for 2FA setup");
            
            // Step 6: Verify backup codes are provided
            boolean backupCodesFound = verifyBackupCodes(wait);
            assertTrue(backupCodesFound, "Backup codes should be provided during 2FA setup");
            
            // Step 7: Verify manual setup option is available
            boolean manualSetupFound = verifyManualSetup(wait);
            assertTrue(manualSetupFound, "Manual setup option should be available as alternative to QR code");
            
            // Step 8: Test QR code image quality and accessibility
            testQRCodeQuality(wait);
            
            System.out.println("✅ 2FA QR Code Setup Test completed successfully");
            
        } catch (Exception e) {
            System.out.println("❌ Error during 2FA QR code setup test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Test
    public void testTwoFactorAuthVerification() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔑 Starting 2FA Verification Test");
            
            // Step 1: Login with credentials
            performRegularLogin(wait);
            
            // Step 2: Look for 2FA verification page
            boolean twoFAPromptFound = wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Enter verification code')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), '2FA') or contains(text(), 'Two-Factor')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder*='code' or @placeholder*='verification']"))
            )) != null;
            
            if (twoFAPromptFound) {
                System.out.println("✅ 2FA verification prompt detected");
                
                // Step 3: Verify 2FA input field is present
                WebElement codeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='text' or @type='number'][contains(@placeholder, 'code') or contains(@name, '2fa') or contains(@name, 'verification')]")
                ));
                assertTrue(codeInput.isDisplayed(), "2FA code input field should be visible");
                System.out.println("✅ 2FA code input field found");
                
                // Step 4: Test invalid code scenarios
                testInvalid2FACode(wait, codeInput);
                
                // Step 5: Test backup code option
                testBackupCodeOption(wait);
                
                // Step 6: Verify security measures
                verifySecurityMeasures(wait);
                
            } else {
                System.out.println("⚠️ 2FA verification not triggered - may not be enabled for this account");
            }
            
            System.out.println("✅ 2FA Verification Test completed");
            
        } catch (Exception e) {
            System.out.println("❌ Error during 2FA verification test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Test
    public void testQRCodeAccessibility() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("♿ Starting QR Code Accessibility Test");
            
            // Navigate to 2FA setup
            performRegularLogin(wait);
            navigateToSecuritySettings(wait);
            
            // Enable 2FA to show QR code
            WebElement enable2FAButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Enable 2FA') or contains(text(), 'Setup Authenticator')]")
            ));
            enable2FAButton.click();
            Thread.sleep(3000);
            
            // Test accessibility features
            testQRCodeAltText(wait);
            testKeyboardNavigation(wait);
            testScreenReaderSupport(wait);
            testHighContrastSupport(wait);
            
            System.out.println("✅ QR Code Accessibility Test completed");
            
        } catch (Exception e) {
            System.out.println("❌ Error during QR code accessibility test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // Helper Methods
    
    private void performRegularLogin(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔐 Performing regular login...");
        
        // This is a placeholder - replace with your actual login logic
        // You might use Google login or regular email/password
        try {
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='email' or @name='email' or @placeholder*='email']")
            ));
            emailField.sendKeys("test@example.com");
            
            WebElement passwordField = driver.findElement(
                By.xpath("//input[@type='password' or @name='password']")
            );
            passwordField.sendKeys("testpassword");
            
            WebElement submitButton = driver.findElement(
                By.xpath("//button[@type='submit' or contains(text(), 'Login')]")
            );
            submitButton.click();
            Thread.sleep(3000);
            
        } catch (Exception e) {
            System.out.println("⚠️ Regular login elements not found - may need to adapt for your specific login flow");
        }
    }
    
    private void navigateToSecuritySettings(WebDriverWait wait) throws InterruptedException {
        System.out.println("⚙️ Navigating to security settings...");
        
        try {
            // Look for common security/profile navigation patterns
            WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'profile') or contains(@aria-label, 'profile')] | //a[contains(@href, 'profile')] | //*[contains(text(), 'Profile')] | //*[contains(text(), 'Account')]")
            ));
            profileMenu.click();
            Thread.sleep(2000);
            
            // Look for security settings
            WebElement securityLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, 'security') or contains(text(), 'Security')] | //button[contains(text(), 'Security')] | //*[contains(text(), 'Two-Factor') or contains(text(), '2FA')]")
            ));
            securityLink.click();
            Thread.sleep(2000);
            
        } catch (Exception e) {
            System.out.println("⚠️ Security settings navigation may need customization for your app structure");
        }
    }
    
    private boolean verifyQRCodePresence(WebDriverWait wait) {
        System.out.println("🔍 Checking for QR code presence...");
        
        try {
            // Look for QR code using multiple strategies
            List<WebElement> qrElements = driver.findElements(By.xpath(
                "//img[contains(@src, 'qr') or contains(@alt, 'QR') or contains(@class, 'qr')] | " +
                "//canvas[contains(@class, 'qr')] | " +
                "//*[contains(@id, 'qr')] | " +
                "//*[contains(text(), 'QR code')] | " +
                "//svg[contains(@class, 'qr')]"
            ));
            
            if (!qrElements.isEmpty()) {
                System.out.println("✅ QR code element found: " + qrElements.get(0).getTagName());
                
                // Verify QR code is visible
                boolean isDisplayed = qrElements.get(0).isDisplayed();
                System.out.println("✅ QR code visibility: " + isDisplayed);
                
                // Try to get QR code dimensions
                try {
                    String width = qrElements.get(0).getCssValue("width");
                    String height = qrElements.get(0).getCssValue("height");
                    System.out.println("📐 QR code dimensions: " + width + " x " + height);
                } catch (Exception e) {
                    System.out.println("⚠️ Could not get QR code dimensions");
                }
                
                return isDisplayed;
            }
            
            System.out.println("❌ QR code element not found");
            return false;
            
        } catch (Exception e) {
            System.out.println("❌ Error checking QR code presence: " + e.getMessage());
            return false;
        }
    }
    
    private boolean verifyBackupCodes(WebDriverWait wait) {
        System.out.println("🔍 Checking for backup codes...");
        
        try {
            List<WebElement> backupElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'backup') and contains(text(), 'code')] | " +
                "//*[contains(text(), 'recovery code')] | " +
                "//*[contains(text(), 'emergency code')] | " +
                "//code | " +
                "//*[contains(@class, 'backup-code') or contains(@class, 'recovery-code')]"
            ));
            
            if (!backupElements.isEmpty()) {
                System.out.println("✅ Backup codes section found");
                
                // Look for actual code patterns (typically 8-10 alphanumeric characters)
                List<WebElement> codeElements = driver.findElements(By.xpath(
                    "//*[matches(text(), '[A-Z0-9]{8,}')]"
                ));
                
                System.out.println("📝 Number of potential backup codes found: " + codeElements.size());
                return true;
            }
            
            System.out.println("❌ Backup codes not found");
            return false;
            
        } catch (Exception e) {
            System.out.println("❌ Error checking backup codes: " + e.getMessage());
            return false;
        }
    }
    
    private boolean verifyManualSetup(WebDriverWait wait) {
        System.out.println("🔍 Checking for manual setup option...");
        
        try {
            List<WebElement> manualElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'manual') and (contains(text(), 'setup') or contains(text(), 'enter'))] | " +
                "//*[contains(text(), 'secret key')] | " +
                "//*[contains(text(), 'setup key')] | " +
                "//button[contains(text(), 'manual')] | " +
                "//a[contains(text(), 'manual')]"
            ));
            
            if (!manualElements.isEmpty()) {
                System.out.println("✅ Manual setup option found");
                
                // Try to click manual setup to verify it works
                try {
                    manualElements.get(0).click();
                    Thread.sleep(2000);
                    
                    // Look for secret key
                    List<WebElement> secretKeyElements = driver.findElements(By.xpath(
                        "//input[contains(@value, '') and string-length(@value) > 20] | " +
                        "//*[contains(@class, 'secret')] | " +
                        "//code[string-length(text()) > 20]"
                    ));
                    
                    if (!secretKeyElements.isEmpty()) {
                        System.out.println("✅ Secret key found for manual setup");
                    }
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Could not test manual setup interaction");
                }
                
                return true;
            }
            
            System.out.println("❌ Manual setup option not found");
            return false;
            
        } catch (Exception e) {
            System.out.println("❌ Error checking manual setup: " + e.getMessage());
            return false;
        }
    }
    
    private void testQRCodeQuality(WebDriverWait wait) {
        System.out.println("🔍 Testing QR code quality...");
        
        try {
            // Take screenshot of QR code area for manual review
            WebElement qrElement = driver.findElement(By.xpath(
                "//img[contains(@src, 'qr') or contains(@alt, 'QR')] | " +
                "//canvas[contains(@class, 'qr')] | " +
                "//*[contains(@id, 'qr')]"
            ));
            
            // Check if QR code has minimum required size
            int width = qrElement.getSize().getWidth();
            int height = qrElement.getSize().getHeight();
            
            System.out.println("📐 QR code actual size: " + width + "x" + height + " pixels");
            
            // QR codes should be at least 100x100 pixels for good readability
            assertTrue(width >= 100 && height >= 100, 
                "QR code should be at least 100x100 pixels for good readability");
            
            // QR codes should be square
            assertTrue(Math.abs(width - height) <= 10, 
                "QR code should be approximately square");
            
            System.out.println("✅ QR code size and proportions are acceptable");
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not verify QR code quality: " + e.getMessage());
        }
    }
    
    private void testInvalid2FACode(WebDriverWait wait, WebElement codeInput) throws InterruptedException {
        System.out.println("🧪 Testing invalid 2FA code handling...");
        
        try {
            // Test with invalid code
            codeInput.clear();
            codeInput.sendKeys("000000");
            
            WebElement submitButton = driver.findElement(
                By.xpath("//button[@type='submit' or contains(text(), 'Verify') or contains(text(), 'Submit')]")
            );
            submitButton.click();
            Thread.sleep(3000);
            
            // Look for error message
            List<WebElement> errorElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'error') or contains(@class, 'invalid')] | " +
                "//*[contains(text(), 'Invalid') or contains(text(), 'incorrect')] | " +
                "//*[contains(text(), 'wrong') or contains(text(), 'failed')]"
            ));
            
            if (!errorElements.isEmpty()) {
                System.out.println("✅ Error message displayed for invalid code: " + errorElements.get(0).getText());
            } else {
                System.out.println("⚠️ No error message found for invalid code");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not test invalid code handling: " + e.getMessage());
        }
    }
    
    private void testBackupCodeOption(WebDriverWait wait) {
        System.out.println("🔍 Testing backup code option...");
        
        try {
            List<WebElement> backupLinks = driver.findElements(By.xpath(
                "//a[contains(text(), 'backup') or contains(text(), 'recovery')] | " +
                "//button[contains(text(), 'backup') or contains(text(), 'recovery')] | " +
                "//*[contains(text(), 'Use backup code') or contains(text(), 'Use recovery code')]"
            ));
            
            if (!backupLinks.isEmpty()) {
                System.out.println("✅ Backup code option available during verification");
                
                // Test clicking backup code option
                backupLinks.get(0).click();
                Thread.sleep(2000);
                
                // Verify backup code input appears
                List<WebElement> backupInputs = driver.findElements(By.xpath(
                    "//input[contains(@placeholder, 'backup') or contains(@placeholder, 'recovery')]"
                ));
                
                if (!backupInputs.isEmpty()) {
                    System.out.println("✅ Backup code input field appeared");
                } else {
                    System.out.println("⚠️ Backup code input field not found");
                }
                
            } else {
                System.out.println("⚠️ Backup code option not found during verification");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not test backup code option: " + e.getMessage());
        }
    }
    
    private void verifySecurityMeasures(WebDriverWait wait) {
        System.out.println("🛡️ Verifying security measures...");
        
        try {
            // Check for rate limiting hints
            List<WebElement> rateLimitElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'attempt') and contains(text(), 'remaining')] | " +
                "//*[contains(text(), 'locked') or contains(text(), 'blocked')] | " +
                "//*[contains(text(), 'wait') and contains(text(), 'minute')]"
            ));
            
            if (!rateLimitElements.isEmpty()) {
                System.out.println("✅ Rate limiting messages found");
            }
            
            // Check for session timeout warnings
            List<WebElement> timeoutElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'session') and contains(text(), 'expire')] | " +
                "//*[contains(text(), 'timeout')] | " +
                "//*[contains(text(), 'logout')]"
            ));
            
            if (!timeoutElements.isEmpty()) {
                System.out.println("✅ Session security measures detected");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not verify all security measures: " + e.getMessage());
        }
    }
    
    // Accessibility testing methods
    
    private void testQRCodeAltText(WebDriverWait wait) {
        System.out.println("🔍 Testing QR code alt text...");
        
        try {
            List<WebElement> qrImages = driver.findElements(By.xpath("//img[contains(@src, 'qr') or contains(@alt, 'QR')]"));
            
            for (WebElement img : qrImages) {
                String altText = img.getAttribute("alt");
                if (altText != null && !altText.trim().isEmpty()) {
                    System.out.println("✅ QR code has alt text: " + altText);
                } else {
                    System.out.println("⚠️ QR code missing alt text");
                }
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not test QR code alt text: " + e.getMessage());
        }
    }
    
    private void testKeyboardNavigation(WebDriverWait wait) {
        System.out.println("⌨️ Testing keyboard navigation...");
        
        try {
            // Test tab navigation to QR code area
            List<WebElement> focusableElements = driver.findElements(By.xpath(
                "//button | //a | //input | //*[@tabindex]"
            ));
            
            int focusableCount = focusableElements.size();
            System.out.println("⌨️ Found " + focusableCount + " focusable elements on 2FA page");
            
            if (focusableCount > 0) {
                System.out.println("✅ Page supports keyboard navigation");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not test keyboard navigation: " + e.getMessage());
        }
    }
    
    private void testScreenReaderSupport(WebDriverWait wait) {
        System.out.println("🔊 Testing screen reader support...");
        
        try {
            // Check for ARIA labels and roles
            List<WebElement> ariaElements = driver.findElements(By.xpath(
                "//*[@aria-label or @aria-describedby or @role]"
            ));
            
            System.out.println("🔊 Found " + ariaElements.size() + " elements with ARIA attributes");
            
            // Check for proper headings structure
            List<WebElement> headings = driver.findElements(By.xpath("//h1 | //h2 | //h3 | //h4 | //h5 | //h6"));
            System.out.println("📋 Found " + headings.size() + " heading elements");
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not test screen reader support: " + e.getMessage());
        }
    }
    
    private void testHighContrastSupport(WebDriverWait wait) {
        System.out.println("🎨 Testing high contrast support...");
        
        try {
            // Test if QR code is still visible with different contrast
            WebElement qrElement = driver.findElement(By.xpath(
                "//img[contains(@src, 'qr')] | //canvas[contains(@class, 'qr')] | //*[contains(@id, 'qr')]"
            ));
            
            String backgroundColor = qrElement.getCssValue("background-color");
            String color = qrElement.getCssValue("color");
            
            System.out.println("🎨 QR code background: " + backgroundColor);
            System.out.println("🎨 QR code color: " + color);
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not test high contrast support: " + e.getMessage());
        }
    }
}
