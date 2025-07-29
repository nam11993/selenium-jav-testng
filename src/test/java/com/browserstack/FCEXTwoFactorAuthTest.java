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

public class FCEXTwoFactorAuthTest extends LocalBrowserTest {

    private final String BASE_URL = "https://fcex-fe-718949727112.asia-southeast1.run.app/en";
    private final String SECURITY_URL = BASE_URL + "/account/security";

    @Test
    public void testNavigateToSecurityPage() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔐 Testing Navigation to Security Page");
            System.out.println("📍 Starting URL: " + driver.getCurrentUrl());
            
            // Step 1: Login first (using your existing login flow)
            performLogin(wait);
            
            // Step 2: Navigate directly to security page
            driver.get(SECURITY_URL);
            Thread.sleep(3000);
            
            // Step 3: Verify we're on the security page
            String currentUrl = driver.getCurrentUrl();
            System.out.println("📍 Current URL: " + currentUrl);
            
            assertTrue(currentUrl.contains("/account/security"), 
                "Should be on the security page");
            
            // Step 4: Check page title and content
            String pageTitle = driver.getTitle();
            System.out.println("📄 Page title: " + pageTitle);
            
            // Look for security-related content
            List<WebElement> securityElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'Security') or contains(text(), 'Two-Factor') or contains(text(), '2FA')]"
            ));
            
            assertTrue(securityElements.size() > 0, 
                "Security page should contain security-related content");
            
            System.out.println("✅ Successfully navigated to security page");
            
        } catch (Exception e) {
            System.out.println("❌ Error navigating to security page: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testTwoFactorAuthSectionPresence() throws InterruptedException, Exception {
        driver.get(SECURITY_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔍 Testing 2FA Section Presence");
            
            // Login if needed
            performLogin(wait);
            
            // Navigate to security page
            driver.get(SECURITY_URL);
            Thread.sleep(3000);
            
            // Look for 2FA section with various possible labels
            List<WebElement> twoFAElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'Two-Factor Authentication') or contains(text(), '2FA')] | " +
                "//*[contains(text(), 'Authenticator') or contains(text(), 'Google Authenticator')] | " +
                "//*[contains(text(), 'Two Factor') or contains(text(), 'Two-factor')] | " +
                "//*[contains(text(), 'TOTP') or contains(text(), 'Time-based')]"
            ));
            
            if (twoFAElements.size() > 0) {
                System.out.println("✅ 2FA section found");
                for (WebElement element : twoFAElements) {
                    System.out.println("  - " + element.getTagName() + ": " + element.getText());
                }
            } else {
                System.out.println("❌ 2FA section not found");
            }
            
            // Look for Enable/Disable buttons
            List<WebElement> actionButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Enable') or contains(text(), 'Disable')] | " +
                "//button[contains(text(), 'Setup') or contains(text(), 'Configure')] | " +
                "//button[contains(text(), 'Turn on') or contains(text(), 'Turn off')]"
            ));
            
            if (actionButtons.size() > 0) {
                System.out.println("✅ 2FA action buttons found:");
                for (WebElement button : actionButtons) {
                    System.out.println("  - Button: " + button.getText());
                    System.out.println("    Enabled: " + button.isEnabled());
                }
            }
            
            assertTrue(twoFAElements.size() > 0 || actionButtons.size() > 0, 
                "2FA functionality should be present on security page");
            
        } catch (Exception e) {
            System.out.println("❌ Error testing 2FA section presence: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testEnableTwoFactorAuth() throws InterruptedException, Exception {
        driver.get(SECURITY_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        
        try {
            System.out.println("🔐 Testing Enable Two-Factor Authentication");
            
            // Login and navigate to security page
            performLogin(wait);
            driver.get(SECURITY_URL);
            Thread.sleep(3000);
            
            // Look for Enable 2FA button
            WebElement enableButton = null;
            List<WebElement> enableButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Enable') and (contains(text(), '2FA') or contains(text(), 'Two-Factor'))] | " +
                "//button[contains(text(), 'Setup') and contains(text(), 'Authenticator')] | " +
                "//button[contains(text(), 'Turn on') and contains(text(), '2FA')] | " +
                "//button[contains(text(), 'Enable Two-Factor Authentication')] | " +
                "//a[contains(text(), 'Enable') and contains(text(), '2FA')]"
            ));
            
            if (enableButtons.size() > 0) {
                enableButton = enableButtons.get(0);
                System.out.println("✅ Found Enable 2FA button: " + enableButton.getText());
                
                // Check if 2FA is already enabled
                String buttonText = enableButton.getText().toLowerCase();
                if (buttonText.contains("disable") || buttonText.contains("turn off")) {
                    System.out.println("⚠️ 2FA appears to be already enabled");
                    return;
                }
                
                // Click Enable button
                enableButton.click();
                Thread.sleep(3000);
                
                // Test QR Code Setup Flow
                testQRCodeSetupFlow(wait);
                
            } else {
                System.out.println("❌ Enable 2FA button not found");
                
                // Look for any 2FA related content
                List<WebElement> allButtons = driver.findElements(By.xpath("//button"));
                System.out.println("🔍 All buttons on page:");
                for (WebElement btn : allButtons) {
                    String text = btn.getText();
                    if (!text.trim().isEmpty()) {
                        System.out.println("  - " + text);
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error testing enable 2FA: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testTwoFactorAuthQRCodeSetup() throws InterruptedException, Exception {
        driver.get(SECURITY_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        
        try {
            System.out.println("📱 Testing 2FA QR Code Setup");
            
            // Login and navigate
            performLogin(wait);
            driver.get(SECURITY_URL);
            Thread.sleep(3000);
            
            // Enable 2FA to trigger QR code display
            triggerTwoFactorSetup(wait);
            
            // Wait for QR code to appear
            boolean qrCodeFound = wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@src, 'qr') or contains(@alt, 'QR')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//canvas[contains(@class, 'qr')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id, 'qr')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//svg[contains(@class, 'qr')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'QR')]"))
            )) != null;
            
            if (qrCodeFound) {
                System.out.println("✅ QR code setup page displayed");
                
                // Test QR code properties
                testQRCodeProperties(wait);
                
                // Test backup codes
                testBackupCodesGeneration(wait);
                
                // Test manual setup option
                testManualSetupOption(wait);
                
            } else {
                System.out.println("❌ QR code not found in setup flow");
                
                // Debug: Print page content
                String pageSource = driver.getPageSource();
                if (pageSource.toLowerCase().contains("qr") || 
                    pageSource.toLowerCase().contains("authenticator")) {
                    System.out.println("🔍 QR/Authenticator content found in page source");
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error in QR code setup test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testTwoFactorAuthVerificationFlow() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔑 Testing 2FA Verification Flow");
            
            // Perform login that should trigger 2FA
            performLoginForTwoFactorTest(wait);
            
            // Look for 2FA verification prompt
            boolean twoFAPromptFound = wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Enter verification code')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Authentication Code')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), '6-digit code')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder*='code' or @placeholder*='verification']")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Authenticator app')]"))
            )) != null;
            
            if (twoFAPromptFound) {
                System.out.println("✅ 2FA verification prompt detected");
                
                // Test verification input field
                testVerificationInput(wait);
                
                // Test invalid code handling
                testInvalidCodeHandling(wait);
                
                // Test backup code option
                testBackupCodeVerification(wait);
                
            } else {
                System.out.println("⚠️ 2FA verification not triggered");
                System.out.println("📍 Current URL: " + driver.getCurrentUrl());
                
                // Check if we're already logged in or 2FA is not enabled
                if (driver.getCurrentUrl().contains("/account") || 
                    driver.getCurrentUrl().contains("/dashboard")) {
                    System.out.println("ℹ️ User may already be logged in or 2FA not enabled");
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error in 2FA verification test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testDisableTwoFactorAuth() throws InterruptedException, Exception {
        driver.get(SECURITY_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔐 Testing Disable Two-Factor Authentication");
            
            // Login and navigate
            performLogin(wait);
            driver.get(SECURITY_URL);
            Thread.sleep(3000);
            
            // Look for Disable 2FA button
            List<WebElement> disableButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Disable') and (contains(text(), '2FA') or contains(text(), 'Two-Factor'))] | " +
                "//button[contains(text(), 'Turn off') and contains(text(), '2FA')] | " +
                "//button[contains(text(), 'Remove') and contains(text(), 'Authenticator')] | " +
                "//button[contains(text(), 'Disable Two-Factor Authentication')]"
            ));
            
            if (disableButtons.size() > 0) {
                System.out.println("✅ Found Disable 2FA button: " + disableButtons.get(0).getText());
                
                // Click disable button
                disableButtons.get(0).click();
                Thread.sleep(2000);
                
                // Test confirmation flow
                testDisableConfirmation(wait);
                
            } else {
                System.out.println("⚠️ Disable 2FA button not found - 2FA may not be enabled");
                
                // Look for enable button instead
                List<WebElement> enableButtons = driver.findElements(By.xpath(
                    "//button[contains(text(), 'Enable') and contains(text(), '2FA')]"
                ));
                
                if (enableButtons.size() > 0) {
                    System.out.println("ℹ️ Found Enable 2FA button - 2FA is currently disabled");
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error testing disable 2FA: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Helper Methods

    private void performLogin(WebDriverWait wait) throws InterruptedException {
        try {
            System.out.println("🔐 Performing login...");
            
            // Check if already logged in
            if (driver.getCurrentUrl().contains("/account") || 
                driver.getPageSource().contains("logout") || 
                driver.getPageSource().contains("profile")) {
                System.out.println("ℹ️ Already logged in");
                return;
            }
            
            // Navigate to base URL if not there
            if (!driver.getCurrentUrl().startsWith(BASE_URL)) {
                driver.get(BASE_URL);
                Thread.sleep(2000);
            }
            
            // Look for login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'Sign in') or contains(text(), 'LOG IN')] | " +
                        "//a[contains(text(), 'Login') or contains(text(), 'Sign in')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Use Google login (as per your existing setup)
            WebElement googleLoginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class=\"h-12 w-12 flex items-center justify-center\"]")
            ));
            googleLoginButton.click();
            Thread.sleep(3000);
            
            // Handle Google authentication
            handleGoogleAuth(wait);
            
        } catch (Exception e) {
            System.out.println("⚠️ Login flow error: " + e.getMessage());
        }
    }

    private void handleGoogleAuth(WebDriverWait wait) throws InterruptedException {
        try {
            // Check if we're redirected to Google
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("google.com") || currentUrl.contains("accounts.google")) {
                System.out.println("🔄 Handling Google authentication");
                
                // Enter email
                WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='email']")
                ));
                emailInput.sendKeys("hainam11993@gmail.com");
                
                WebElement nextButton = driver.findElement(
                    By.xpath("//span[text()='Next']")
                );
                nextButton.click();
                Thread.sleep(2000);
                
                // Enter password
                WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='password']")
                ));
                passwordInput.sendKeys("Hainam12@");
                
                nextButton.click();
                Thread.sleep(5000);
                
                System.out.println("✅ Google authentication completed");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Google auth handling error: " + e.getMessage());
        }
    }

    private void performLoginForTwoFactorTest(WebDriverWait wait) throws InterruptedException {
        // This method specifically tests login flow that should trigger 2FA
        // Use credentials that have 2FA enabled
        performLogin(wait);
    }

    private void triggerTwoFactorSetup(WebDriverWait wait) throws InterruptedException {
        try {
            // Look for various 2FA setup triggers
            List<WebElement> setupTriggers = driver.findElements(By.xpath(
                "//button[contains(text(), 'Enable') and contains(text(), '2FA')] | " +
                "//button[contains(text(), 'Setup') and contains(text(), 'Authenticator')] | " +
                "//button[contains(text(), 'Add') and contains(text(), '2FA')] | " +
                "//a[contains(text(), 'Enable') and contains(text(), '2FA')]"
            ));
            
            if (setupTriggers.size() > 0) {
                setupTriggers.get(0).click();
                Thread.sleep(3000);
                System.out.println("✅ Triggered 2FA setup");
            } else {
                System.out.println("⚠️ 2FA setup trigger not found");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error triggering 2FA setup: " + e.getMessage());
        }
    }

    private void testQRCodeSetupFlow(WebDriverWait wait) {
        try {
            System.out.println("📱 Testing QR code setup flow...");
            
            // Wait for setup page to load
            Thread.sleep(3000);
            
            // Look for QR code
            List<WebElement> qrElements = driver.findElements(By.xpath(
                "//img[contains(@src, 'qr') or contains(@alt, 'QR')] | " +
                "//canvas[contains(@class, 'qr')] | " +
                "//*[contains(@id, 'qr')] | " +
                "//svg[contains(@class, 'qr')] | " +
                "//*[contains(text(), 'QR code')]"
            ));
            
            if (qrElements.size() > 0) {
                System.out.println("✅ QR code found in setup flow");
                testQRCodeProperties(wait);
            } else {
                System.out.println("⚠️ QR code not found in setup flow");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ QR code setup flow error: " + e.getMessage());
        }
    }

    private void testQRCodeProperties(WebDriverWait wait) {
        try {
            System.out.println("🔍 Testing QR code properties...");
            
            WebElement qrElement = driver.findElement(By.xpath(
                "//img[contains(@src, 'qr')] | //canvas[contains(@class, 'qr')] | //*[contains(@id, 'qr')]"
            ));
            
            // Test visibility
            assertTrue(qrElement.isDisplayed(), "QR code should be visible");
            
            // Test dimensions
            int width = qrElement.getSize().getWidth();
            int height = qrElement.getSize().getHeight();
            System.out.println("📐 QR code dimensions: " + width + "x" + height);
            
            assertTrue(width >= 100 && height >= 100, "QR code should be at least 100x100 pixels");
            
            System.out.println("✅ QR code properties validated");
            
        } catch (Exception e) {
            System.out.println("⚠️ QR code properties test error: " + e.getMessage());
        }
    }

    private void testBackupCodesGeneration(WebDriverWait wait) {
        try {
            System.out.println("🔑 Testing backup codes generation...");
            
            List<WebElement> backupElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'backup') and contains(text(), 'code')] | " +
                "//*[contains(text(), 'recovery code')] | " +
                "//*[contains(text(), 'emergency code')] | " +
                "//code | " +
                "//*[contains(@class, 'backup-code')]"
            ));
            
            if (backupElements.size() > 0) {
                System.out.println("✅ Backup codes section found");
                
                // Look for download or copy options
                List<WebElement> downloadButtons = driver.findElements(By.xpath(
                    "//button[contains(text(), 'Download') or contains(text(), 'Copy')] | " +
                    "//a[contains(text(), 'Download')]"
                ));
                
                if (downloadButtons.size() > 0) {
                    System.out.println("✅ Backup codes download/copy option available");
                }
                
            } else {
                System.out.println("⚠️ Backup codes not found");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Backup codes test error: " + e.getMessage());
        }
    }

    private void testManualSetupOption(WebDriverWait wait) {
        try {
            System.out.println("⌨️ Testing manual setup option...");
            
            List<WebElement> manualElements = driver.findElements(By.xpath(
                "//button[contains(text(), 'manual') or contains(text(), 'Manual')] | " +
                "//a[contains(text(), 'manual setup')] | " +
                "//*[contains(text(), 'secret key')] | " +
                "//*[contains(text(), 'setup key')]"
            ));
            
            if (manualElements.size() > 0) {
                System.out.println("✅ Manual setup option found");
                
                // Try clicking manual setup
                manualElements.get(0).click();
                Thread.sleep(2000);
                
                // Look for secret key display
                List<WebElement> secretKeyElements = driver.findElements(By.xpath(
                    "//input[string-length(@value) > 20] | " +
                    "//code[string-length(text()) > 20] | " +
                    "//*[contains(@class, 'secret')]"
                ));
                
                if (secretKeyElements.size() > 0) {
                    System.out.println("✅ Secret key found for manual setup");
                }
                
            } else {
                System.out.println("⚠️ Manual setup option not found");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Manual setup test error: " + e.getMessage());
        }
    }

    private void testVerificationInput(WebDriverWait wait) {
        try {
            System.out.println("🔢 Testing verification input...");
            
            WebElement codeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='text' or @type='number'][contains(@placeholder, 'code') or contains(@name, 'verification') or contains(@name, '2fa')]")
            ));
            
            assertTrue(codeInput.isDisplayed(), "Verification code input should be visible");
            assertTrue(codeInput.isEnabled(), "Verification code input should be enabled");
            
            System.out.println("✅ Verification input field validated");
            
        } catch (Exception e) {
            System.out.println("⚠️ Verification input test error: " + e.getMessage());
        }
    }

    private void testInvalidCodeHandling(WebDriverWait wait) {
        try {
            System.out.println("❌ Testing invalid code handling...");
            
            WebElement codeInput = driver.findElement(
                By.xpath("//input[contains(@placeholder, 'code') or contains(@name, 'verification')]")
            );
            
            // Enter invalid code
            codeInput.clear();
            codeInput.sendKeys("000000");
            
            // Submit
            WebElement submitButton = driver.findElement(
                By.xpath("//button[@type='submit' or contains(text(), 'Verify') or contains(text(), 'Submit')]")
            );
            submitButton.click();
            Thread.sleep(3000);
            
            // Look for error message
            List<WebElement> errorElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'error') or contains(@class, 'invalid')] | " +
                "//*[contains(text(), 'Invalid') or contains(text(), 'incorrect')]"
            ));
            
            if (errorElements.size() > 0) {
                System.out.println("✅ Error message displayed for invalid code");
            } else {
                System.out.println("⚠️ No error message found for invalid code");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Invalid code handling test error: " + e.getMessage());
        }
    }

    private void testBackupCodeVerification(WebDriverWait wait) {
        try {
            System.out.println("🔑 Testing backup code verification option...");
            
            List<WebElement> backupLinks = driver.findElements(By.xpath(
                "//a[contains(text(), 'backup') or contains(text(), 'recovery')] | " +
                "//button[contains(text(), 'Use backup code')] | " +
                "//*[contains(text(), 'lost your device')]"
            ));
            
            if (backupLinks.size() > 0) {
                System.out.println("✅ Backup code verification option found");
                
                backupLinks.get(0).click();
                Thread.sleep(2000);
                
                // Look for backup code input
                List<WebElement> backupInputs = driver.findElements(By.xpath(
                    "//input[contains(@placeholder, 'backup') or contains(@placeholder, 'recovery')]"
                ));
                
                if (backupInputs.size() > 0) {
                    System.out.println("✅ Backup code input field available");
                }
                
            } else {
                System.out.println("⚠️ Backup code verification option not found");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Backup code verification test error: " + e.getMessage());
        }
    }

    private void testDisableConfirmation(WebDriverWait wait) {
        try {
            System.out.println("⚠️ Testing disable confirmation flow...");
            
            // Look for confirmation dialog or page
            List<WebElement> confirmElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'confirm') or contains(text(), 'Confirm')] | " +
                "//*[contains(text(), 'sure') or contains(text(), 'certain')] | " +
                "//button[contains(text(), 'Yes') or contains(text(), 'Continue')] | " +
                "//button[contains(text(), 'Disable')]"
            ));
            
            if (confirmElements.size() > 0) {
                System.out.println("✅ Disable confirmation found");
                
                // Look for final disable button
                WebElement finalDisableButton = driver.findElement(
                    By.xpath("//button[contains(text(), 'Disable') or contains(text(), 'Yes') or contains(text(), 'Confirm')]")
                );
                
                if (finalDisableButton.isEnabled()) {
                    System.out.println("✅ Final disable button is available");
                    // Note: Don't actually click it to avoid disabling 2FA
                }
                
            } else {
                System.out.println("⚠️ Disable confirmation not found");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Disable confirmation test error: " + e.getMessage());
        }
    }
}
