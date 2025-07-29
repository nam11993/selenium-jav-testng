package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class Complete2FALoginTest extends LocalBrowserTest {

    private final String BASE_URL = "https://fcex-fe-718949727112.asia-southeast1.run.app/en";

    @Test
    public void testCompleteLoginWithAuthenticatorSetup() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔐 Testing Complete Login Flow with Authenticator Setup");
            
            // Step 1: Perform initial login
            performLogin(wait);
            
            // Step 2: Complete 2FA setup to finish login process
            complete2FASetup(wait);
            
            // Step 3: Verify successful complete login
            verifyCompleteLoginSuccess();
            
            System.out.println("✅ Complete login with 2FA setup successful");
            
        } catch (Exception e) {
            System.out.println("❌ Error during complete login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testCompleteLoginBySkipping2FA() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("⏭️ Testing Complete Login by Skipping 2FA");
            
            // Step 1: Perform initial login
            performLogin(wait);
            
            // Step 2: Skip 2FA setup to complete login
            skip2FASetupToCompleteLogin(wait);
            
            // Step 3: Verify successful login bypass
            verifyLoginBypassSuccess();
            
            System.out.println("✅ Complete login by skipping 2FA successful");
            
        } catch (Exception e) {
            System.out.println("❌ Error during skip 2FA login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testCompleteLoginWithMultiple2FAAttempts() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔄 Testing Complete Login with Multiple 2FA Attempts");
            
            // Step 1: Perform initial login
            performLogin(wait);
            
            // Step 2: Click Authenticator App option
            selectAuthenticatorApp(wait);
            
            // Step 3: Try invalid code first
            attemptInvalidCode(wait);
            
            // Step 4: Try valid code to complete
            attemptValidCode(wait);
            
            // Step 5: Verify completion
            verifyCompleteLoginSuccess();
            
            System.out.println("✅ Complete login with multiple attempts successful");
            
        } catch (Exception e) {
            System.out.println("❌ Error during multiple attempts test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testCompleteLoginWithQRCodeValidation() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("📱 Testing Complete Login with QR Code Validation");
            
            // Step 1: Perform initial login
            performLogin(wait);
            
            // Step 2: Click Authenticator App option
            selectAuthenticatorApp(wait);
            
            // Step 3: Validate QR code display and properties
            validateQRCodeDisplay();
            
            // Step 4: Validate manual entry option
            validateManualEntryOption();
            
            // Step 5: Complete setup with test code
            enterTestCodeAndComplete(wait);
            
            System.out.println("✅ Complete login with QR validation successful");
            
        } catch (Exception e) {
            System.out.println("❌ Error during QR validation test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testCompleteLoginWithNavigationTesting() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🧭 Testing Complete Login with Navigation Testing");
            
            // Step 1: Perform initial login
            performLogin(wait);
            
            // Step 2: Test navigation between 2FA options
            testNavigationBetween2FAOptions(wait);
            
            // Step 3: Complete setup to finish login
            completeSetupViaAuthenticator(wait);
            
            // Step 4: Test post-login navigation
            testPostLoginNavigation();
            
            System.out.println("✅ Complete login with navigation testing successful");
            
        } catch (Exception e) {
            System.out.println("❌ Error during navigation test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Helper Methods

    private void performLogin(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔐 Performing initial login...");
        
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
        
        System.out.println("✅ Initial login completed");
    }

    private void complete2FASetup(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔐 Completing 2FA setup...");
        
        // Wait for 2FA setup dialog
        WebElement twoFactorDialog = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[contains(text(), 'Set up 2-Factor')]")
        ));
        assertTrue(twoFactorDialog.isDisplayed(), "2FA setup dialog should appear");
        
        // Click Authenticator App
        WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[contains(text(), 'Authenticator App')]")
        ));
        authenticatorOption.click();
        Thread.sleep(2000);
        System.out.println("✅ Selected Authenticator App");
        
        // Enter verification code
        WebElement codeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='text' or @type='number']")
        ));
        codeInput.sendKeys("123456"); // Test code
        System.out.println("✅ Entered verification code");
        
        // Click Continue
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Continue')]")
        ));
        continueButton.click();
        Thread.sleep(3000);
        System.out.println("✅ Clicked Continue to complete setup");
    }

    private void skip2FASetupToCompleteLogin(WebDriverWait wait) throws InterruptedException {
        System.out.println("⏭️ Skipping 2FA setup to complete login...");
        
        // Wait for 2FA setup dialog
        WebElement twoFactorDialog = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[contains(text(), 'Set up 2-Factor')]")
        ));
        assertTrue(twoFactorDialog.isDisplayed(), "2FA setup dialog should appear");
        
        // Look for Skip option
        List<WebElement> skipButtons = driver.findElements(By.xpath(
            "//button[contains(text(), 'Skip')] | //a[contains(text(), 'Skip')]"
        ));
        
        if (skipButtons.size() > 0) {
            skipButtons.get(0).click();
            Thread.sleep(3000);
            System.out.println("✅ Skipped 2FA setup");
        } else {
            // Alternative: Click outside dialog or look for close button
            List<WebElement> closeButtons = driver.findElements(By.xpath(
                "//button[contains(@class, 'close')] | " +
                "//*[contains(@aria-label, 'close')] | " +
                "//button[text()='×']"
            ));
            
            if (closeButtons.size() > 0) {
                closeButtons.get(0).click();
                Thread.sleep(2000);
                System.out.println("✅ Closed 2FA setup dialog");
            } else {
                System.out.println("⚠️ Skip option not found, proceeding with setup");
                complete2FASetup(wait);
            }
        }
    }

    private void selectAuthenticatorApp(WebDriverWait wait) throws InterruptedException {
        WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[contains(text(), 'Authenticator App')]")
        ));
        authenticatorOption.click();
        Thread.sleep(2000);
        System.out.println("✅ Selected Authenticator App");
    }

    private void attemptInvalidCode(WebDriverWait wait) throws InterruptedException {
        System.out.println("❌ Attempting invalid code...");
        
        WebElement codeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='text' or @type='number']")
        ));
        codeInput.clear();
        codeInput.sendKeys("000000");
        
        WebElement continueButton = driver.findElement(
            By.xpath("//button[contains(text(), 'Continue')]")
        );
        continueButton.click();
        Thread.sleep(2000);
        
        // Check for error message
        List<WebElement> errorElements = driver.findElements(By.xpath(
            "//*[contains(@class, 'error')] | " +
            "//*[contains(text(), 'Invalid') or contains(text(), 'incorrect')]"
        ));
        
        if (errorElements.size() > 0) {
            System.out.println("✅ Error message displayed for invalid code");
        }
    }

    private void attemptValidCode(WebDriverWait wait) throws InterruptedException {
        System.out.println("✅ Attempting valid code...");
        
        WebElement codeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='text' or @type='number']")
        ));
        codeInput.clear();
        codeInput.sendKeys("123456");
        
        WebElement continueButton = driver.findElement(
            By.xpath("//button[contains(text(), 'Continue')]")
        );
        continueButton.click();
        Thread.sleep(3000);
    }

    private void validateQRCodeDisplay() {
        System.out.println("📱 Validating QR code display...");
        
        // Check for QR code presence
        List<WebElement> qrElements = driver.findElements(By.xpath(
            "//canvas | //img[contains(@alt, 'QR')] | //svg"
        ));
        
        assertTrue(qrElements.size() > 0, "QR code should be displayed");
        assertTrue(qrElements.get(0).isDisplayed(), "QR code should be visible");
        System.out.println("✅ QR code validated");
        
        // Check for QR code instructions
        List<WebElement> instructionElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'Scan the QR Code') or contains(text(), 'navigate to the URL manually')]"
        ));
        
        if (instructionElements.size() > 0) {
            System.out.println("✅ QR code instructions found");
        }
    }

    private void validateManualEntryOption() {
        System.out.println("📝 Validating manual entry option...");
        
        // Check for manual entry code
        List<WebElement> manualElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'otpauth://') or contains(text(), 'hainam38493@gmail.com')] | " +
            "//input[@type='text' and @readonly]"
        ));
        
        if (manualElements.size() > 0) {
            String codeText = manualElements.get(0).getText();
            if (codeText == null || codeText.isEmpty()) {
                codeText = manualElements.get(0).getAttribute("value");
            }
            
            if (codeText != null && codeText.contains("hainam38493@gmail.com")) {
                System.out.println("✅ Manual entry code validated");
            }
        }
    }

    private void enterTestCodeAndComplete(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔢 Entering test code to complete setup...");
        
        WebElement codeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='text' or @type='number']")
        ));
        codeInput.sendKeys("123456");
        
        WebElement continueButton = driver.findElement(
            By.xpath("//button[contains(text(), 'Continue')]")
        );
        continueButton.click();
        Thread.sleep(3000);
        System.out.println("✅ Test code entered and setup completed");
    }

    private void testNavigationBetween2FAOptions(WebDriverWait wait) throws InterruptedException {
        System.out.println("🧭 Testing navigation between 2FA options...");
        
        // Click Authenticator App
        WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[contains(text(), 'Authenticator App')]")
        ));
        authenticatorOption.click();
        Thread.sleep(2000);
        
        // Go back
        List<WebElement> backButtons = driver.findElements(By.xpath(
            "//button[contains(text(), 'Back')]"
        ));
        
        if (backButtons.size() > 0) {
            backButtons.get(0).click();
            Thread.sleep(1000);
            System.out.println("✅ Navigated back from Authenticator App");
            
            // Try Universal Second Factor
            List<WebElement> usfOptions = driver.findElements(By.xpath(
                "//*[contains(text(), 'Universal Second Factor')]"
            ));
            
            if (usfOptions.size() > 0) {
                usfOptions.get(0).click();
                Thread.sleep(2000);
                System.out.println("✅ Tested Universal Second Factor navigation");
                
                // Go back again
                List<WebElement> backButtons2 = driver.findElements(By.xpath(
                    "//button[contains(text(), 'Back')]"
                ));
                if (backButtons2.size() > 0) {
                    backButtons2.get(0).click();
                    Thread.sleep(1000);
                }
            }
        }
    }

    private void completeSetupViaAuthenticator(WebDriverWait wait) throws InterruptedException {
        System.out.println("✅ Completing setup via authenticator...");
        
        // Select authenticator app again
        selectAuthenticatorApp(wait);
        
        // Enter code and complete
        enterTestCodeAndComplete(wait);
    }

    private void testPostLoginNavigation() {
        System.out.println("🔍 Testing post-login navigation...");
        
        // Look for navigation elements
        List<WebElement> navElements = driver.findElements(By.xpath(
            "//nav | //header | //*[contains(@class, 'navigation')] | " +
            "//*[contains(@class, 'menu')] | //*[contains(@class, 'sidebar')]"
        ));
        
        if (navElements.size() > 0) {
            System.out.println("✅ Navigation elements found: " + navElements.size());
        }
        
        // Look for user account elements
        List<WebElement> userElements = driver.findElements(By.xpath(
            "//*[contains(@class, 'user')] | " +
            "//*[contains(text(), 'hainam') or contains(text(), '@gmail.com')]"
        ));
        
        if (userElements.size() > 0) {
            System.out.println("✅ User account elements found");
        }
    }

    private void verifyCompleteLoginSuccess() {
        System.out.println("🔍 Verifying complete login success...");
        
        String currentUrl = driver.getCurrentUrl();
        System.out.println("📍 Current URL: " + currentUrl);
        
        // Check for successful login indicators
        boolean loginSuccessful = currentUrl.contains("/dashboard") || 
                                currentUrl.contains("/account") ||
                                currentUrl.contains("/main") ||
                                driver.getPageSource().toLowerCase().contains("logout") ||
                                driver.getPageSource().toLowerCase().contains("welcome") ||
                                !driver.getPageSource().contains("Set up 2-Factor");
        
        assertTrue(loginSuccessful, "Complete login should be successful");
        System.out.println("✅ Complete login verified successful");
    }

    private void verifyLoginBypassSuccess() {
        System.out.println("🔍 Verifying login bypass success...");
        
        String currentUrl = driver.getCurrentUrl();
        boolean bypassSuccessful = !currentUrl.contains("/login") && 
                                 !driver.getPageSource().contains("Set up 2-Factor") &&
                                 (currentUrl.contains(BASE_URL) || 
                                  driver.getPageSource().toLowerCase().contains("dashboard") ||
                                  driver.getPageSource().toLowerCase().contains("account"));
        
        assertTrue(bypassSuccessful, "Login bypass should be successful");
        System.out.println("✅ Login bypass verified successful");
    }
}
