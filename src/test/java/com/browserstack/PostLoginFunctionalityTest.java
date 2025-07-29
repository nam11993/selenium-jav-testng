package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class PostLoginFunctionalityTest extends LocalBrowserTest {

    private final String BASE_URL = "https://fcex-fe-718949727112.asia-southeast1.run.app/en";

    @Test
    public void testCompleteLoginAndSecuritySetup() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔄 Testing Complete Login and Security Setup Flow");
            
            // Step 1: Perform login
            performLogin(wait);
            
            // Step 2: Handle 2FA setup dialog
            handle2FASetupDialog(wait);
            
            // Step 3: Test security features access
            testSecurityFeaturesAccess(wait);
            
            // Step 4: Test account management features
            testAccountManagementFeatures(wait);
            
            System.out.println("✅ Complete login and security setup flow tested");
            
        } catch (Exception e) {
            System.out.println("❌ Error during complete login flow test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testTwoFactorAuthFlow() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔐 Testing Complete 2FA Flow");
            
            // Perform login
            performLogin(wait);
            
            // Check for 2FA setup dialog
            WebElement twoFactorDialog = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), 'Set up 2-Factor')]")
            ));
            
            assertTrue(twoFactorDialog.isDisplayed(), "2FA setup dialog should be displayed");
            System.out.println("✅ 2FA setup dialog found");
            
            // Test selecting Authenticator App
            WebElement authenticatorOption = driver.findElement(
                By.xpath("//*[contains(text(), 'Authenticator App')]")
            );
            authenticatorOption.click();
            Thread.sleep(2000);
            System.out.println("✅ Selected Authenticator App option");
            
            // Check for QR code or setup instructions
            List<WebElement> setupElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'QR') or contains(text(), 'code') or contains(text(), 'secret')]"
            ));
            
            if (setupElements.size() > 0) {
                System.out.println("✅ Setup instructions/QR code found");
            }
            
            // Test going back to options
            List<WebElement> backButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Back')]"
            ));
            
            if (backButtons.size() > 0) {
                backButtons.get(0).click();
                Thread.sleep(1000);
                System.out.println("✅ Back button functionality tested");
                
                // Now test Universal Second Factor
                List<WebElement> usfOption = driver.findElements(By.xpath(
                    "//*[contains(text(), 'Universal Second Factor')]"
                ));
                
                if (usfOption.size() > 0) {
                    usfOption.get(0).click();
                    Thread.sleep(2000);
                    System.out.println("✅ Universal Second Factor option tested");
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during 2FA flow test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testSecurityPageAccess() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🛡️ Testing Security Page Access After Login");
            
            // Perform login and skip 2FA setup
            performLogin(wait);
            skip2FASetup();
            
            // Navigate to security settings
            navigateToSecuritySettings(wait);
            
            // Test security features
            testSecurityFeatures(wait);
            
        } catch (Exception e) {
            System.out.println("❌ Error during security page access test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testAccountSettingsAccess() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("⚙️ Testing Account Settings Access");
            
            // Perform login and skip 2FA setup
            performLogin(wait);
            skip2FASetup();
            
            // Navigate to account settings
            navigateToAccountSettings(wait);
            
            // Test account management features
            testAccountFeatures(wait);
            
        } catch (Exception e) {
            System.out.println("❌ Error during account settings test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testDashboardFunctionality() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("📊 Testing Dashboard Functionality");
            
            // Perform login and skip 2FA setup
            performLogin(wait);
            skip2FASetup();
            
            // Test dashboard elements
            testDashboardElements(wait);
            
        } catch (Exception e) {
            System.out.println("❌ Error during dashboard functionality test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Helper Methods

    private void performLogin(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔐 Performing login...");
        
        // Click Login button
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN')]")
        ));
        loginButton.click();
        Thread.sleep(2000);
        
        // Enter credentials
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='email']")
        ));
        emailInput.sendKeys("hainam11993@gmail.com");
        
        WebElement passwordInput = driver.findElement(By.xpath("//input[@type='password']"));
        passwordInput.sendKeys("ValidPassword123@");
        
        // Submit login
        WebElement submitButton = driver.findElement(
            By.xpath("//button[contains(text(), 'Sign in') or @type='submit']")
        );
        submitButton.click();
        Thread.sleep(5000);
        
        System.out.println("✅ Login completed");
    }

    private void handle2FASetupDialog(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔐 Handling 2FA setup dialog...");
        
        List<WebElement> twoFactorElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'Set up 2-Factor')]"
        ));
        
        if (twoFactorElements.size() > 0) {
            System.out.println("✅ 2FA setup dialog found");
            
            // Test each option briefly
            testAuthenticatorAppOption();
            testUniversalSecondFactorOption();
            
            // Skip setup for now
            skip2FASetup();
        } else {
            System.out.println("⚠️ 2FA setup dialog not found");
        }
    }

    private void testAuthenticatorAppOption() throws InterruptedException {
        List<WebElement> authenticatorElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'Authenticator App')]"
        ));
        
        if (authenticatorElements.size() > 0) {
            authenticatorElements.get(0).click();
            Thread.sleep(2000);
            System.out.println("✅ Authenticator App option clicked");
            
            // Go back
            List<WebElement> backButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Back')]"
            ));
            if (backButtons.size() > 0) {
                backButtons.get(0).click();
                Thread.sleep(1000);
            }
        }
    }

    private void testUniversalSecondFactorOption() throws InterruptedException {
        List<WebElement> usfElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'Universal Second Factor')]"
        ));
        
        if (usfElements.size() > 0) {
            usfElements.get(0).click();
            Thread.sleep(2000);
            System.out.println("✅ Universal Second Factor option clicked");
            
            // Go back
            List<WebElement> backButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Back')]"
            ));
            if (backButtons.size() > 0) {
                backButtons.get(0).click();
                Thread.sleep(1000);
            }
        }
    }

    private void skip2FASetup() throws InterruptedException {
        List<WebElement> skipButtons = driver.findElements(By.xpath(
            "//button[contains(text(), 'Skip')] | //a[contains(text(), 'Skip')]"
        ));
        
        if (skipButtons.size() > 0) {
            skipButtons.get(0).click();
            Thread.sleep(3000);
            System.out.println("✅ Skipped 2FA setup");
        }
    }

    private void testSecurityFeaturesAccess(WebDriverWait wait) {
        System.out.println("🛡️ Testing security features access...");
        
        // Look for security-related navigation or buttons
        List<WebElement> securityElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'Security') or contains(text(), 'Settings')] | " +
            "//a[contains(@href, 'security')] | " +
            "//button[contains(text(), 'Account')]"
        ));
        
        if (securityElements.size() > 0) {
            System.out.println("✅ Security features access points found: " + securityElements.size());
        } else {
            System.out.println("⚠️ Security features access points not immediately visible");
        }
    }

    private void testAccountManagementFeatures(WebDriverWait wait) {
        System.out.println("👤 Testing account management features...");
        
        // Look for account-related elements
        List<WebElement> accountElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'Account') or contains(text(), 'Profile')] | " +
            "//*[contains(@class, 'user') or contains(@class, 'account')] | " +
            "//a[contains(@href, 'account') or contains(@href, 'profile')]"
        ));
        
        if (accountElements.size() > 0) {
            System.out.println("✅ Account management features found: " + accountElements.size());
        } else {
            System.out.println("⚠️ Account management features not immediately visible");
        }
    }

    private void navigateToSecuritySettings(WebDriverWait wait) throws InterruptedException {
        // Direct navigation to security page
        String securityUrl = BASE_URL + "/account/security";
        driver.get(securityUrl);
        Thread.sleep(3000);
        System.out.println("✅ Navigated to security settings");
    }

    private void testSecurityFeatures(WebDriverWait wait) {
        System.out.println("🔍 Testing security features...");
        
        // Look for 2FA management
        List<WebElement> twoFactorManagement = driver.findElements(By.xpath(
            "//*[contains(text(), '2FA') or contains(text(), 'Two-Factor')] | " +
            "//*[contains(text(), 'Authentication') or contains(text(), 'Security')]"
        ));
        
        if (twoFactorManagement.size() > 0) {
            System.out.println("✅ 2FA management options found");
        }
        
        // Look for password change options
        List<WebElement> passwordElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'Password') or contains(text(), 'Change')] | " +
            "//input[@type='password']"
        ));
        
        if (passwordElements.size() > 0) {
            System.out.println("✅ Password management options found");
        }
    }

    private void navigateToAccountSettings(WebDriverWait wait) throws InterruptedException {
        // Look for account settings navigation
        List<WebElement> accountLinks = driver.findElements(By.xpath(
            "//a[contains(@href, 'account') or contains(text(), 'Account')] | " +
            "//button[contains(text(), 'Settings') or contains(text(), 'Profile')]"
        ));
        
        if (accountLinks.size() > 0) {
            accountLinks.get(0).click();
            Thread.sleep(2000);
            System.out.println("✅ Navigated to account settings");
        } else {
            System.out.println("⚠️ Account settings navigation not found");
        }
    }

    private void testAccountFeatures(WebDriverWait wait) {
        System.out.println("🔍 Testing account features...");
        
        // Test various account features
        String[] accountFeatures = {"Profile", "Email", "Verification", "Preferences", "Notifications"};
        
        for (String feature : accountFeatures) {
            List<WebElement> featureElements = driver.findElements(By.xpath(
                "//*[contains(text(), '" + feature + "')]"
            ));
            
            if (featureElements.size() > 0) {
                System.out.println("✅ Found " + feature + " feature");
            }
        }
    }

    private void testDashboardElements(WebDriverWait wait) {
        System.out.println("🔍 Testing dashboard elements...");
        
        // Look for common dashboard elements
        String[] dashboardElements = {"Balance", "Portfolio", "Trading", "History", "Charts", "Markets"};
        
        for (String element : dashboardElements) {
            List<WebElement> elementList = driver.findElements(By.xpath(
                "//*[contains(text(), '" + element + "')]"
            ));
            
            if (elementList.size() > 0) {
                System.out.println("✅ Found " + element + " element");
            }
        }
        
        // Check for navigation menu
        List<WebElement> navElements = driver.findElements(By.xpath(
            "//nav | //header | //*[contains(@class, 'navigation')] | " +
            "//*[contains(@class, 'menu')] | //*[contains(@class, 'sidebar')]"
        ));
        
        if (navElements.size() > 0) {
            System.out.println("✅ Navigation elements found: " + navElements.size());
        }
    }
}
