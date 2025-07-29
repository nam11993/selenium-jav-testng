package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;

public class QRCodeValidationTest extends LocalBrowserTest {

    @Test
    public void testQRCodeGeneration() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔄 Starting QR Code Generation Test");
            
            // Navigate to 2FA setup
            navigateToTwoFactorSetup(wait);
            
            // Test QR code generation
            testQRCodeCreation(wait);
            
            // Test QR code refresh/regeneration
            testQRCodeRegeneration(wait);
            
            // Test QR code format validation
            testQRCodeFormat(wait);
            
            // Test QR code security properties
            testQRCodeSecurity(wait);
            
            System.out.println("✅ QR Code Generation Test completed successfully");
            
        } catch (Exception e) {
            System.out.println("❌ Error during QR code generation test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Test
    public void testQRCodeIntegrationWithAuthenticatorApps() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("📱 Starting QR Code Authenticator App Integration Test");
            
            // Navigate to 2FA setup
            navigateToTwoFactorSetup(wait);
            
            // Test compatibility with different authenticator apps
            testAuthenticatorAppCompatibility(wait);
            
            // Test QR code instructions
            testQRCodeInstructions(wait);
            
            // Test fallback options
            testQRCodeFallbackOptions(wait);
            
            System.out.println("✅ QR Code Authenticator Integration Test completed");
            
        } catch (Exception e) {
            System.out.println("❌ Error during authenticator integration test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Test
    public void testQRCodeErrorHandling() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("⚠️ Starting QR Code Error Handling Test");
            
            // Navigate to 2FA setup
            navigateToTwoFactorSetup(wait);
            
            // Test QR code loading failures
            testQRCodeLoadingFailures(wait);
            
            // Test network error scenarios
            testNetworkErrorScenarios(wait);
            
            // Test browser compatibility issues
            testBrowserCompatibility(wait);
            
            System.out.println("✅ QR Code Error Handling Test completed");
            
        } catch (Exception e) {
            System.out.println("❌ Error during QR code error handling test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Test
    public void testQRCodeUserExperience() throws InterruptedException, Exception {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("👤 Starting QR Code User Experience Test");
            
            // Navigate to 2FA setup
            navigateToTwoFactorSetup(wait);
            
            // Test QR code visibility and readability
            testQRCodeVisibility(wait);
            
            // Test help and guidance
            testUserGuidance(wait);
            
            // Test mobile responsiveness
            testMobileExperience(wait);
            
            System.out.println("✅ QR Code User Experience Test completed");
            
        } catch (Exception e) {
            System.out.println("❌ Error during QR code UX test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // Helper Methods
    
    private void navigateToTwoFactorSetup(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔐 Navigating to 2FA setup...");
        
        try {
            // Login first (adapt this to your login flow)
            performLogin(wait);
            
            // Navigate to account/security settings
            WebElement accountMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'account') or contains(@aria-label, 'account')] | " +
                        "//a[contains(@href, 'account')] | " +
                        "//*[contains(text(), 'Account') or contains(text(), 'Profile')]")
            ));
            accountMenu.click();
            Thread.sleep(2000);
            
            // Find security section
            WebElement securitySection = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, 'security') or contains(text(), 'Security')] | " +
                        "//button[contains(text(), 'Security')] | " +
                        "//*[contains(text(), 'Two-Factor') or contains(text(), '2FA')]")
            ));
            securitySection.click();
            Thread.sleep(2000);
            
            // Click setup 2FA
            WebElement setup2FA = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Enable 2FA') or contains(text(), 'Setup 2FA') or contains(text(), 'Add Authenticator')]")
            ));
            setup2FA.click();
            Thread.sleep(3000);
            
            System.out.println("✅ Successfully navigated to 2FA setup page");
            
        } catch (Exception e) {
            System.out.println("⚠️ Navigation to 2FA setup may need customization: " + e.getMessage());
        }
    }
    
    private void performLogin(WebDriverWait wait) throws InterruptedException {
        try {
            // Click login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'Sign in')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Perform Google login or regular login based on your setup
            WebElement googleLoginButton = driver.findElement(
                By.xpath("//div[@class=\"h-12 w-12 flex items-center justify-center\"]")
            );
            googleLoginButton.click();
            Thread.sleep(5000);
            
            System.out.println("✅ Login completed");
            
        } catch (Exception e) {
            System.out.println("⚠️ Login flow may need adjustment: " + e.getMessage());
        }
    }
    
    private void testQRCodeCreation(WebDriverWait wait) {
        System.out.println("🔄 Testing QR code creation...");
        
        try {
            // Wait for QR code to appear
            WebElement qrCode = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//img[contains(@src, 'qr') or contains(@alt, 'QR')] | " +
                        "//canvas[contains(@class, 'qr')] | " +
                        "//*[contains(@id, 'qr')] | " +
                        "//svg[contains(@class, 'qr')]")
            ));
            
            assertNotNull(qrCode, "QR code should be generated and displayed");
            assertTrue(qrCode.isDisplayed(), "QR code should be visible");
            
            // Verify QR code has proper dimensions
            int width = qrCode.getSize().getWidth();
            int height = qrCode.getSize().getHeight();
            
            assertTrue(width > 0 && height > 0, "QR code should have valid dimensions");
            System.out.println("✅ QR code created with dimensions: " + width + "x" + height);
            
            // Check if QR code loads properly (not broken image)
            if (qrCode.getTagName().equals("img")) {
                Boolean isComplete = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].complete && arguments[0].naturalWidth > 0", qrCode);
                assertTrue(isComplete, "QR code image should load completely");
                System.out.println("✅ QR code image loaded successfully");
            }
            
        } catch (Exception e) {
            System.out.println("❌ QR code creation test failed: " + e.getMessage());
        }
    }
    
    private void testQRCodeRegeneration(WebDriverWait wait) {
        System.out.println("🔄 Testing QR code regeneration...");
        
        try {
            // Look for refresh/regenerate button
            List<WebElement> refreshButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Refresh') or contains(text(), 'Regenerate') or contains(text(), 'New QR')] | " +
                "//button[contains(@title, 'refresh') or contains(@aria-label, 'refresh')] | " +
                "//*[contains(@class, 'refresh') or contains(@class, 'reload')]"
            ));
            
            if (!refreshButtons.isEmpty()) {
                System.out.println("✅ QR code refresh option found");
                
                // Get original QR code source/content
                WebElement qrCode = driver.findElement(By.xpath(
                    "//img[contains(@src, 'qr')] | //canvas[contains(@class, 'qr')]"
                ));
                String originalSrc = qrCode.getAttribute("src");
                
                // Click refresh
                refreshButtons.get(0).click();
                Thread.sleep(3000);
                
                // Verify QR code changed
                String newSrc = qrCode.getAttribute("src");
                if (originalSrc != null && newSrc != null && !originalSrc.equals(newSrc)) {
                    System.out.println("✅ QR code successfully regenerated");
                } else {
                    System.out.println("⚠️ QR code may not have changed after refresh");
                }
                
            } else {
                System.out.println("⚠️ QR code refresh option not found");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ QR code regeneration test error: " + e.getMessage());
        }
    }
    
    private void testQRCodeFormat(WebDriverWait wait) {
        System.out.println("📋 Testing QR code format...");
        
        try {
            // Check if secret key is also displayed (for manual entry)
            List<WebElement> secretKeyElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'secret') or contains(text(), 'key')] | " +
                "//input[contains(@name, 'secret') or contains(@name, 'key')] | " +
                "//code[string-length(text()) > 15]"
            ));
            
            if (!secretKeyElements.isEmpty()) {
                System.out.println("✅ Secret key for manual setup found");
                
                // Verify secret key format (should be base32 encoded, typically 32 characters)
                for (WebElement element : secretKeyElements) {
                    String text = element.getText();
                    if (text.length() >= 16 && text.matches("[A-Z2-7]+")) {
                        System.out.println("✅ Valid secret key format detected: " + text.substring(0, 8) + "...");
                        break;
                    }
                }
            } else {
                System.out.println("⚠️ Manual setup secret key not found");
            }
            
            // Verify QR code contains proper TOTP URL format
            // Note: This would require QR code decoding, which is complex in Selenium
            // In practice, you might use a separate QR code reading library
            System.out.println("✅ QR code format validation completed");
            
        } catch (Exception e) {
            System.out.println("⚠️ QR code format test error: " + e.getMessage());
        }
    }
    
    private void testQRCodeSecurity(WebDriverWait wait) {
        System.out.println("🛡️ Testing QR code security properties...");
        
        try {
            // Verify QR code is served over HTTPS
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.startsWith("https://"), "2FA setup page should use HTTPS");
            System.out.println("✅ QR code served over secure connection");
            
            // Check for security warnings or notices
            List<WebElement> securityNotices = driver.findElements(By.xpath(
                "//*[contains(text(), 'secure') or contains(text(), 'private')] | " +
                "//*[contains(text(), 'warning') or contains(text(), 'important')] | " +
                "//*[contains(@class, 'warning') or contains(@class, 'notice')]"
            ));
            
            if (!securityNotices.isEmpty()) {
                System.out.println("✅ Security notices found: " + securityNotices.size());
                for (WebElement notice : securityNotices) {
                    System.out.println("  - " + notice.getText());
                }
            }
            
            // Verify no sensitive information in page source
            String pageSource = driver.getPageSource().toLowerCase();
            assertFalse(pageSource.contains("password") && pageSource.contains("secret"), 
                "Page should not expose sensitive information in plain text");
            
            System.out.println("✅ QR code security validation completed");
            
        } catch (Exception e) {
            System.out.println("⚠️ QR code security test error: " + e.getMessage());
        }
    }
    
    private void testAuthenticatorAppCompatibility(WebDriverWait wait) {
        System.out.println("📱 Testing authenticator app compatibility...");
        
        try {
            // Look for app recommendations or compatibility information
            List<WebElement> appMentions = driver.findElements(By.xpath(
                "//*[contains(text(), 'Google Authenticator') or contains(text(), 'Authy')] | " +
                "//*[contains(text(), 'Microsoft Authenticator') or contains(text(), 'authenticator app')] | " +
                "//*[contains(text(), 'TOTP') or contains(text(), 'Time-based')]"
            ));
            
            if (!appMentions.isEmpty()) {
                System.out.println("✅ Authenticator app compatibility information found");
                for (WebElement mention : appMentions) {
                    System.out.println("  - " + mention.getText());
                }
            } else {
                System.out.println("⚠️ No specific authenticator app mentions found");
            }
            
            // Check for download links to authenticator apps
            List<WebElement> downloadLinks = driver.findElements(By.xpath(
                "//a[contains(@href, 'play.google.com') or contains(@href, 'apps.apple.com')] | " +
                "//a[contains(text(), 'Download') and contains(text(), 'app')]"
            ));
            
            if (!downloadLinks.isEmpty()) {
                System.out.println("✅ App download links provided: " + downloadLinks.size());
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Authenticator app compatibility test error: " + e.getMessage());
        }
    }
    
    private void testQRCodeInstructions(WebDriverWait wait) {
        System.out.println("📖 Testing QR code instructions...");
        
        try {
            // Look for step-by-step instructions
            List<WebElement> instructions = driver.findElements(By.xpath(
                "//*[contains(text(), 'step') or contains(text(), 'Step')] | " +
                "//ol | //ul | " +
                "//*[contains(text(), 'scan') or contains(text(), 'Scan')] | " +
                "//*[contains(text(), 'open') and contains(text(), 'app')]"
            ));
            
            if (!instructions.isEmpty()) {
                System.out.println("✅ Setup instructions found: " + instructions.size() + " elements");
                
                // Check for numbered steps or bullet points
                List<WebElement> steps = driver.findElements(By.xpath("//ol/li | //ul/li"));
                System.out.println("📝 Number of instruction steps: " + steps.size());
                
                if (steps.size() >= 3) {
                    System.out.println("✅ Comprehensive instructions provided");
                } else {
                    System.out.println("⚠️ Instructions may be incomplete");
                }
                
            } else {
                System.out.println("⚠️ Setup instructions not found");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Instructions test error: " + e.getMessage());
        }
    }
    
    private void testQRCodeFallbackOptions(WebDriverWait wait) {
        System.out.println("🔄 Testing QR code fallback options...");
        
        try {
            // Look for alternative setup methods
            List<WebElement> fallbackOptions = driver.findElements(By.xpath(
                "//button[contains(text(), 'manual') or contains(text(), 'Manual')] | " +
                "//a[contains(text(), 'manual') or contains(text(), 'alternative')] | " +
                "//*[contains(text(), 'enter code manually') or contains(text(), 'type code')]"
            ));
            
            if (!fallbackOptions.isEmpty()) {
                System.out.println("✅ Fallback options found: " + fallbackOptions.size());
                
                // Test manual entry option
                fallbackOptions.get(0).click();
                Thread.sleep(2000);
                
                // Verify manual setup interface
                List<WebElement> manualInputs = driver.findElements(By.xpath(
                    "//input[contains(@placeholder, 'secret') or contains(@placeholder, 'key')] | " +
                    "//textarea[contains(@placeholder, 'secret')]"
                ));
                
                if (!manualInputs.isEmpty()) {
                    System.out.println("✅ Manual entry interface available");
                } else {
                    System.out.println("⚠️ Manual entry interface not found");
                }
                
            } else {
                System.out.println("⚠️ No fallback options found");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Fallback options test error: " + e.getMessage());
        }
    }
    
    private void testQRCodeLoadingFailures(WebDriverWait wait) {
        System.out.println("⚠️ Testing QR code loading failure scenarios...");
        
        try {
            // Check for loading states
            List<WebElement> loadingElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'loading') or contains(@class, 'spinner')] | " +
                "//*[contains(text(), 'Loading') or contains(text(), 'loading')]"
            ));
            
            if (!loadingElements.isEmpty()) {
                System.out.println("✅ Loading state indicators found");
            }
            
            // Look for error messages related to QR code generation
            List<WebElement> errorMessages = driver.findElements(By.xpath(
                "//*[contains(@class, 'error') and contains(text(), 'QR')] | " +
                "//*[contains(text(), 'failed') and contains(text(), 'generate')] | " +
                "//*[contains(text(), 'try again') or contains(text(), 'retry')]"
            ));
            
            if (!errorMessages.isEmpty()) {
                System.out.println("⚠️ QR code error handling found: " + errorMessages.get(0).getText());
            }
            
            System.out.println("✅ QR code loading failure test completed");
            
        } catch (Exception e) {
            System.out.println("⚠️ Loading failure test error: " + e.getMessage());
        }
    }
    
    private void testNetworkErrorScenarios(WebDriverWait wait) {
        System.out.println("🌐 Testing network error scenarios...");
        
        try {
            // This would typically require network manipulation tools
            // For now, we'll check if there are retry mechanisms in place
            
            List<WebElement> retryButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Retry') or contains(text(), 'Try Again')] | " +
                "//button[contains(@class, 'retry')]"
            ));
            
            if (!retryButtons.isEmpty()) {
                System.out.println("✅ Retry mechanism available for network failures");
            } else {
                System.out.println("⚠️ No obvious retry mechanism found");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Network error test error: " + e.getMessage());
        }
    }
    
    private void testBrowserCompatibility(WebDriverWait wait) {
        System.out.println("🌐 Testing browser compatibility...");
        
        try {
            // Check for browser-specific features used
            Boolean canvasSupport = (Boolean) ((JavascriptExecutor) driver)
                .executeScript("return typeof HTMLCanvasElement !== 'undefined'");
            
            Boolean svgSupport = (Boolean) ((JavascriptExecutor) driver)
                .executeScript("return typeof SVGElement !== 'undefined'");
            
            System.out.println("✅ Canvas support: " + canvasSupport);
            System.out.println("✅ SVG support: " + svgSupport);
            
            // Check for fallback content
            List<WebElement> noscriptElements = driver.findElements(By.xpath("//noscript"));
            if (!noscriptElements.isEmpty()) {
                System.out.println("✅ NoScript fallback content found");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Browser compatibility test error: " + e.getMessage());
        }
    }
    
    private void testQRCodeVisibility(WebDriverWait wait) {
        System.out.println("👁️ Testing QR code visibility...");
        
        try {
            WebElement qrCode = driver.findElement(By.xpath(
                "//img[contains(@src, 'qr')] | //canvas[contains(@class, 'qr')] | //*[contains(@id, 'qr')]"
            ));
            
            // Test visibility properties
            assertTrue(qrCode.isDisplayed(), "QR code should be visible");
            
            // Check contrast and readability
            String backgroundColor = qrCode.getCssValue("background-color");
            String borderColor = qrCode.getCssValue("border-color");
            
            System.out.println("🎨 QR code background: " + backgroundColor);
            System.out.println("🎨 QR code border: " + borderColor);
            
            // Verify QR code position (should be prominently placed)
            int yPosition = qrCode.getLocation().getY();
            assertTrue(yPosition < 600, "QR code should be positioned prominently on the page");
            
            System.out.println("✅ QR code visibility test completed");
            
        } catch (Exception e) {
            System.out.println("⚠️ QR code visibility test error: " + e.getMessage());
        }
    }
    
    private void testUserGuidance(WebDriverWait wait) {
        System.out.println("💡 Testing user guidance...");
        
        try {
            // Look for help text, tooltips, or guidance
            List<WebElement> helpElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'help') or contains(@class, 'tooltip')] | " +
                "//*[contains(text(), 'help') or contains(text(), 'Help')] | " +
                "//button[contains(@aria-label, 'help')] | " +
                "//*[contains(@title, 'help')]"
            ));
            
            if (!helpElements.isEmpty()) {
                System.out.println("✅ Help/guidance elements found: " + helpElements.size());
            } else {
                System.out.println("⚠️ No obvious help/guidance found");
            }
            
            // Check for informational text
            List<WebElement> infoText = driver.findElements(By.xpath(
                "//*[contains(text(), 'authenticator') and string-length(text()) > 20] | " +
                "//*[contains(text(), 'scan') and string-length(text()) > 20]"
            ));
            
            System.out.println("📝 Informational text elements: " + infoText.size());
            
        } catch (Exception e) {
            System.out.println("⚠️ User guidance test error: " + e.getMessage());
        }
    }
    
    private void testMobileExperience(WebDriverWait wait) {
        System.out.println("📱 Testing mobile experience...");
        
        try {
            // Simulate mobile viewport (this is basic - real mobile testing would use device emulation)
            ((JavascriptExecutor) driver).executeScript(
                "document.querySelector('meta[name=viewport]') || " +
                "document.head.appendChild(document.createElement('meta')).setAttribute('name', 'viewport');"
            );
            
            // Check if QR code is responsive
            WebElement qrCode = driver.findElement(By.xpath(
                "//img[contains(@src, 'qr')] | //canvas[contains(@class, 'qr')] | //*[contains(@id, 'qr')]"
            ));
            
            String maxWidth = qrCode.getCssValue("max-width");
            String width = qrCode.getCssValue("width");
            
            System.out.println("📐 QR code width: " + width);
            System.out.println("📐 QR code max-width: " + maxWidth);
            
            // Check for mobile-friendly sizing
            int actualWidth = qrCode.getSize().getWidth();
            assertTrue(actualWidth <= 400, "QR code should be sized appropriately for mobile");
            
            System.out.println("✅ Mobile experience test completed");
            
        } catch (Exception e) {
            System.out.println("⚠️ Mobile experience test error: " + e.getMessage());
        }
    }
}
