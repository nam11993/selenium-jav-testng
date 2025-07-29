package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class LoginTest extends LocalBrowserTest {

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
            
            // Step 6: Handle 2FA Setup after successful login
            WebElement otpUriElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class=\"flex-1 overflow-x-auto\"]")
            ));
            String otpUri = otpUriElement.getAttribute("href");
            if (otpUri == null || otpUri.isEmpty()) {
                otpUri = otpUriElement.getAttribute("value");
            }
            if (otpUri == null || otpUri.isEmpty()) {
                otpUri = otpUriElement.getText();
            }

            // 1. Extract the secret from otpauth:// URI
            String secret = extractSecretFromOtpUri(otpUri);
            System.out.println("✅ Extracted TOTP secret: " + (secret != null ? "***" + secret.substring(Math.max(0, secret.length()-4)) : "null"));

            if (secret != null) {
                // 2. Generate TOTP code
                GoogleAuthenticator gAuth = new GoogleAuthenticator();
                int otpCode = gAuth.getTotpPassword(secret);
                System.out.println("✅ Generated OTP Code: " + otpCode);

                // 3. Input code
                WebElement codeInput = driver.findElement(By.xpath("//input[@name='code']"));
                codeInput.sendKeys(String.valueOf(otpCode));
                codeInput.sendKeys(Keys.RETURN);
                System.out.println("✅ Submitted OTP Code");
                Thread.sleep(5000);
            } else {
                System.out.println("⚠️ Could not extract TOTP secret, skipping 2FA setup");
            }
 

        } catch (Exception e) {
            System.out.println("❌ Error during valid email login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Helper method to extract secret from OTP URI
    private String extractSecretFromOtpUri(String otpUri) {
        if (otpUri != null && otpUri.contains("secret=")) {
            try {
                String[] parts = otpUri.split("secret=");
                if (parts.length > 1) {
                    String secretPart = parts[1];
                    int ampIndex = secretPart.indexOf("&");
                    if (ampIndex > 0) {
                        secretPart = secretPart.substring(0, ampIndex);
                    }
                    return secretPart;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error extracting secret: " + e.getMessage());
            }
        }
        return null;
    }

    @Test
    public void testGoogleLogin() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔐 Testing Google Login");
            
            // Step 1: Click Login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Step 2: Click Google login button
            WebElement googleLoginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class=\"h-12 w-12 flex items-center justify-center\"] | " +
                        "//button[contains(text(), 'Google')] | " +
                        "//button[contains(@class, 'google')] | " +
                        "//*[contains(text(), 'Continue with Google')] | " +
                        "//*[contains(text(), 'Sign in with Google')]")
            ));
            System.out.println("✅ Found Google login button");
            googleLoginButton.click();
            Thread.sleep(3000);
            
            // Step 3: Handle Google authentication
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("google.com") || currentUrl.contains("accounts.google")) {
                System.out.println("🔄 Redirected to Google authentication");
                
                // Enter Google email
                WebElement googleEmailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='email']")
                ));
                googleEmailInput.sendKeys("hainam11993@gmail.com");
                Thread.sleep(2000);
                
                WebElement nextButton = driver.findElement(By.xpath("//button[text()='Next']"));
                nextButton.click();
                Thread.sleep(2000);
                
                // Enter Google password
                WebElement googlePasswordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='password']")
                ));
                googlePasswordInput.sendKeys("Hainam12@");
                Thread.sleep(2000);
                
                WebElement continueButton = driver.findElement(By.xpath("//button[text()='Continue']"));
                continueButton.click();
                Thread.sleep(5000);
                
                System.out.println("✅ Google authentication completed");
            }
            
            // Step 4: Verify successful login
            Thread.sleep(3000);
            currentUrl = driver.getCurrentUrl();
            System.out.println("📍 Final URL: " + currentUrl);
            
            boolean loginSuccessful = currentUrl.contains(BASE_URL) && 
                                    (currentUrl.contains("/dashboard") || 
                                     currentUrl.contains("/account") ||
                                     driver.getPageSource().toLowerCase().contains("logout"));
            
            assertTrue(loginSuccessful, "Google login should be successful");
            System.out.println("✅ Google login test completed");
            
        } catch (Exception e) {
            System.out.println("❌ Error during Google login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testInvalidEmailLogin() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("❌ Testing Invalid Email Login");
            
            // Navigate to login page
            navigateToLoginPage(wait);
            
            // Enter invalid email
            WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='email' or contains(@placeholder, 'email')]")
            ));
            emailInput.clear();
            emailInput.sendKeys("invalid@nonexistent.com");
            
            // Enter password
            WebElement passwordInput = driver.findElement(
                By.xpath("//input[@type='password']")
            );
            passwordInput.clear();
            passwordInput.sendKeys("SomePassword123@");
            
            // Click login
            WebElement loginSubmitButton = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or @type='submit']")
            );
            loginSubmitButton.click();
            Thread.sleep(3000);
            
            // Verify error message
            List<WebElement> errorElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'error') or contains(@class, 'invalid')] | " +
                "//*[contains(text(), 'Invalid') or contains(text(), 'incorrect')] | " +
                "//*[contains(text(), 'wrong') or contains(text(), 'failed')] | " +
                "//*[contains(text(), 'not found') or contains(text(), 'does not exist')]"
            ));
            
            assertTrue(errorElements.size() > 0, "Error message should be displayed for invalid email");
            System.out.println("✅ Error message displayed for invalid email");
            
        } catch (Exception e) {
            System.out.println("❌ Error during invalid email login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testInvalidPasswordLogin() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("❌ Testing Invalid Password Login");
            
            // Navigate to login page
            navigateToLoginPage(wait);
            
            // Enter valid email but wrong password
            WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='email']")
            ));
            emailInput.clear();
            emailInput.sendKeys("hainam11993@gmail.com");
            
            // Enter wrong password
            WebElement passwordInput = driver.findElement(
                By.xpath("//input[@type='password']")
            );
            passwordInput.clear();
            passwordInput.sendKeys("WrongPassword123@");
            
            // Click login
            WebElement loginSubmitButton = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or @type='submit']")
            );
            loginSubmitButton.click();
            Thread.sleep(3000);
            
            // Verify error message
            List<WebElement> errorElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'error')] | " +
                "//*[contains(text(), 'Invalid') or contains(text(), 'incorrect')] | " +
                "//*[contains(text(), 'wrong password') or contains(text(), 'authentication failed')]"
            ));
            
            assertTrue(errorElements.size() > 0, "Error message should be displayed for wrong password");
            System.out.println("✅ Error message displayed for invalid password");
            
        } catch (Exception e) {
            System.out.println("❌ Error during invalid password login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testEmptyFieldsLogin() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("❌ Testing Empty Fields Login");
            
            // Navigate to login page
            navigateToLoginPage(wait);
            
            // Leave fields empty and try to login
            WebElement loginSubmitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or @type='submit']")
            ));
            loginSubmitButton.click();
            Thread.sleep(2000);
            
            // Verify validation messages
            List<WebElement> validationElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'required') or contains(@class, 'error')] | " +
                "//*[contains(text(), 'required') or contains(text(), 'Required')] | " +
                "//*[contains(text(), 'Please enter') or contains(text(), 'This field')]"
            ));
            
            assertTrue(validationElements.size() > 0, "Validation messages should be displayed for empty fields");
            System.out.println("✅ Validation messages displayed for empty fields");
            
        } catch (Exception e) {
            System.out.println("❌ Error during empty fields login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testInvalidEmailFormatLogin() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("❌ Testing Invalid Email Format Login");
            
            // Navigate to login page
            navigateToLoginPage(wait);
            
            // Enter invalid email format
            WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='email']")
            ));
            emailInput.clear();
            emailInput.sendKeys("invalid-email-format");
            
            // Enter password
            WebElement passwordInput = driver.findElement(
                By.xpath("//input[@type='password']")
            );
            passwordInput.clear();
            passwordInput.sendKeys("SomePassword123@");
            
            // Click login
            WebElement loginSubmitButton = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or @type='submit']")
            );
            loginSubmitButton.click();
            Thread.sleep(2000);
            
            // Verify email format validation
            List<WebElement> formatErrorElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'Invalid email') or contains(text(), 'valid email')] | " +
                "//*[contains(text(), 'email format') or contains(text(), 'Please enter a valid')]"
            ));
            
            assertTrue(formatErrorElements.size() > 0, "Email format validation should be displayed");
            System.out.println("✅ Email format validation displayed");
            
        } catch (Exception e) {
            System.out.println("❌ Error during invalid email format test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testLoginPageElements() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🔍 Testing Login Page Elements");
            
            // Navigate to login page
            navigateToLoginPage(wait);
            
            // Verify email input field
            WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='email']")
            ));
            assertTrue(emailInput.isDisplayed(), "Email input field should be visible");
            assertTrue(emailInput.isEnabled(), "Email input field should be enabled");
            System.out.println("✅ Email input field verified");
            
            // Verify password input field
            WebElement passwordInput = driver.findElement(
                By.xpath("//input[@type='password']")
            );
            assertTrue(passwordInput.isDisplayed(), "Password input field should be visible");
            assertTrue(passwordInput.isEnabled(), "Password input field should be enabled");
            System.out.println("✅ Password input field verified");
            
            // Verify login button
            WebElement loginButton = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or @type='submit']")
            );
            assertTrue(loginButton.isDisplayed(), "Login button should be visible");
            assertTrue(loginButton.isEnabled(), "Login button should be enabled");
            System.out.println("✅ Login button verified");
            
            // Check for "Remember me" checkbox (if exists)
            List<WebElement> rememberMeElements = driver.findElements(By.xpath(
                "//input[@type='checkbox'] | //*[contains(text(), 'Remember') or contains(text(), 'Keep me')]"
            ));
            if (rememberMeElements.size() > 0) {
                System.out.println("✅ Remember me option found");
            }
            
            // Check for "Forgot password" link
            List<WebElement> forgotPasswordElements = driver.findElements(By.xpath(
                "//a[contains(text(), 'Forgot') or contains(text(), 'Reset')] | " +
                "//*[contains(text(), 'Forgot password') or contains(text(), 'Reset password')]"
            ));
            if (forgotPasswordElements.size() > 0) {
                System.out.println("✅ Forgot password link found");
            }
            
            // Check for social login options
            List<WebElement> socialLoginElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'Google') or contains(text(), 'Facebook')] | " +
                "//*[contains(@class, 'google') or contains(@class, 'facebook')] | " +
                "//div[@class=\"h-12 w-12 flex items-center justify-center\"]"
            ));
            if (socialLoginElements.size() > 0) {
                System.out.println("✅ Social login options found: " + socialLoginElements.size());
            }
            
            System.out.println("✅ Login page elements verification completed");
            
        } catch (Exception e) {
            System.out.println("❌ Error during login page elements test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testForgotPasswordFlow() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🔐 Testing Forgot Password Flow");
            
            // Navigate to login page
            navigateToLoginPage(wait);
            
            // Look for forgot password link
            List<WebElement> forgotPasswordLinks = driver.findElements(By.xpath(
                "//a[contains(text(), 'Forgot') or contains(text(), 'Reset')] | " +
                "//button[contains(text(), 'Forgot') or contains(text(), 'Reset')] | " +
                "//*[contains(text(), 'Forgot password') or contains(text(), 'Reset password')]"
            ));
            
            if (forgotPasswordLinks.size() > 0) {
                System.out.println("✅ Found forgot password link");
                forgotPasswordLinks.get(0).click();
                Thread.sleep(2000);
                
                // Enter email for password reset
                List<WebElement> resetEmailInputs = driver.findElements(By.xpath(
                    "//input[@type='email']"
                ));
                
                if (resetEmailInputs.size() > 0) {
                    resetEmailInputs.get(0).sendKeys("hainam11993@gmail.com");
                    System.out.println("✅ Entered email for password reset");
                    
                    // Click reset button
                    List<WebElement> resetButtons = driver.findElements(By.xpath(
                        "//button[contains(text(), 'Reset') or contains(text(), 'Send') or contains(text(), 'Submit')]"
                    ));
                    
                    if (resetButtons.size() > 0) {
                        resetButtons.get(0).click();
                        Thread.sleep(2000);
                        System.out.println("✅ Clicked reset button");
                        
                        // Verify success message
                        List<WebElement> successMessages = driver.findElements(By.xpath(
                            "//*[contains(text(), 'sent') or contains(text(), 'check your email')] | " +
                            "//*[contains(@class, 'success') or contains(@class, 'confirmation')]"
                        ));
                        
                        if (successMessages.size() > 0) {
                            System.out.println("✅ Password reset confirmation displayed");
                        }
                    }
                }
            } else {
                System.out.println("⚠️ Forgot password link not found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during forgot password test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testLoginLogout() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🔄 Testing Login and Logout Flow");
            
            // Perform login
            performValidLogin(wait);
            
            // Verify login successful
            String currentUrl = driver.getCurrentUrl();
            System.out.println("📍 URL after login: " + currentUrl);
            
            // Look for logout button/option
            List<WebElement> logoutElements = driver.findElements(By.xpath(
                "//button[contains(text(), 'Logout') or contains(text(), 'Log out')] | " +
                "//a[contains(text(), 'Logout') or contains(text(), 'Log out')] | " +
                "//*[contains(text(), 'Sign out')] | " +
                "//button[contains(@class, 'logout')] | " +
                "//*[contains(@data-testid, 'logout')]"
            ));
            
            if (logoutElements.size() > 0) {
                System.out.println("✅ Found logout option");
                logoutElements.get(0).click();
                Thread.sleep(3000);
                
                // Verify logout successful
                String urlAfterLogout = driver.getCurrentUrl();
                boolean logoutSuccessful = !urlAfterLogout.contains("/dashboard") && 
                                         !urlAfterLogout.contains("/account") &&
                                         (urlAfterLogout.contains("/login") || 
                                          urlAfterLogout.equals(BASE_URL) ||
                                          driver.getPageSource().toLowerCase().contains("login"));
                
                assertTrue(logoutSuccessful, "Should be logged out successfully");
                System.out.println("✅ Logout successful");
                
            } else {
                System.out.println("⚠️ Logout option not found - may need to check user menu");
                
                // Look for user profile menu that might contain logout
                List<WebElement> userMenus = driver.findElements(By.xpath(
                    "//*[contains(@class, 'user') or contains(@class, 'profile')] | " +
                    "//button[contains(@aria-label, 'user') or contains(@aria-label, 'profile')] | " +
                    "//*[contains(@class, 'avatar')] | " +
                    "//*[contains(@class, 'dropdown')]"
                ));
                
                if (userMenus.size() > 0) {
                    System.out.println("✅ Found user menu, checking for logout option");
                    userMenus.get(0).click();
                    Thread.sleep(1000);
                    
                    // Look for logout in dropdown
                    List<WebElement> dropdownLogout = driver.findElements(By.xpath(
                        "//a[contains(text(), 'Logout')] | //button[contains(text(), 'Logout')]"
                    ));
                    
                    if (dropdownLogout.size() > 0) {
                        dropdownLogout.get(0).click();
                        System.out.println("✅ Clicked logout from dropdown");
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during login/logout test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testTwoFactorSetupFlow() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🔐 Testing 2FA Setup Flow After Login");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Check for 2FA setup dialog
            List<WebElement> twoFactorElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'Set up 2-Factor') or contains(text(), '2-Factor')] | " +
                "//*[contains(text(), 'Authenticator App')] | " +
                "//*[contains(text(), 'Universal Second Factor')]"
            ));
            
            if (twoFactorElements.size() > 0) {
                System.out.println("✅ 2FA setup dialog found");
                
                // Test Authenticator App option
                WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[contains(text(), 'Authenticator App')]")
                ));
                assertTrue(authenticatorOption.isDisplayed(), "Authenticator App option should be visible");
                System.out.println("✅ Authenticator App option verified");
                
                // Test Universal Second Factor option
                List<WebElement> usfElements = driver.findElements(By.xpath(
                    "//*[contains(text(), 'Universal Second Factor')]"
                ));
                if (usfElements.size() > 0) {
                    assertTrue(usfElements.get(0).isDisplayed(), "Universal Second Factor option should be visible");
                    System.out.println("✅ Universal Second Factor option verified");
                }
                
                // Test Skip option
                List<WebElement> skipElements = driver.findElements(By.xpath(
                    "//*[contains(text(), 'Skip') or contains(@class, 'skip')]"
                ));
                if (skipElements.size() > 0) {
                    assertTrue(skipElements.get(0).isDisplayed(), "Skip option should be visible");
                    System.out.println("✅ Skip option verified");
                }
                
                // Test Back button
                List<WebElement> backElements = driver.findElements(By.xpath(
                    "//button[contains(text(), 'Back')] | //*[contains(@class, 'back')]"
                ));
                if (backElements.size() > 0) {
                    assertTrue(backElements.get(0).isDisplayed(), "Back button should be visible");
                    System.out.println("✅ Back button verified");
                }
                
            } else {
                System.out.println("⚠️ 2FA setup dialog not found - may not be required");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during 2FA setup test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testAuthenticatorAppSetup() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("📱 Testing Authenticator App Setup");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Look for 2FA setup and click Authenticator App
            List<WebElement> authenticatorElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'Authenticator App')]"
            ));
            
            if (authenticatorElements.size() > 0) {
                authenticatorElements.get(0).click();
                Thread.sleep(2000);
                System.out.println("✅ Clicked Authenticator App option");
                
                // Check for QR code display
                List<WebElement> qrCodeElements = driver.findElements(By.xpath(
                    "//img[contains(@alt, 'QR') or contains(@class, 'qr')] | " +
                    "//*[contains(@class, 'qr-code')] | " +
                    "//*[contains(text(), 'QR code') or contains(text(), 'Scan')] | " +
                    "//canvas | //svg"
                ));
                
                if (qrCodeElements.size() > 0) {
                    System.out.println("✅ QR code found for authenticator setup");
                    assertTrue(qrCodeElements.get(0).isDisplayed(), "QR code should be visible");
                }
                
                // Check for manual entry code/secret key
                List<WebElement> manualCodeElements = driver.findElements(By.xpath(
                    "//*[contains(text(), 'otpauth://') or contains(text(), 'hainam38493@gmail.com')] | " +
                    "//input[@type='text'] | " +
                    "//*[contains(text(), 'secret') or contains(text(), 'manually')] | " +
                    "//*[contains(text(), 'SHA1') or contains(text(), 'TOTP')]"
                ));
                
                if (manualCodeElements.size() > 0) {
                    System.out.println("✅ Manual entry code/secret found");
                }
                
                // Test entering verification code
                testVerificationCodeEntry(wait);
                
            } else {
                System.out.println("⚠️ Authenticator App option not found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during authenticator app setup test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testUniversalSecondFactorSetup() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🔑 Testing Universal Second Factor Setup");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Look for 2FA setup and click Universal Second Factor
            List<WebElement> usfElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'Universal Second Factor')]"
            ));
            
            if (usfElements.size() > 0) {
                usfElements.get(0).click();
                Thread.sleep(2000);
                System.out.println("✅ Clicked Universal Second Factor option");
                
                // Check for USB key instructions
                List<WebElement> usbInstructions = driver.findElements(By.xpath(
                    "//*[contains(text(), 'USB') or contains(text(), 'security key')] | " +
                    "//*[contains(text(), 'hardware') or contains(text(), 'device')] | " +
                    "//*[contains(text(), 'Insert') or contains(text(), 'Connect')]"
                ));
                
                if (usbInstructions.size() > 0) {
                    System.out.println("✅ USB/Hardware key instructions found");
                }
                
                // Look for setup button
                List<WebElement> setupButtons = driver.findElements(By.xpath(
                    "//button[contains(text(), 'Setup') or contains(text(), 'Register')] | " +
                    "//button[contains(text(), 'Add') or contains(text(), 'Connect')]"
                ));
                
                if (setupButtons.size() > 0) {
                    assertTrue(setupButtons.get(0).isDisplayed(), "Setup button should be visible");
                    System.out.println("✅ Setup button verified");
                }
                
            } else {
                System.out.println("⚠️ Universal Second Factor option not found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during USF setup test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testSkip2FASetup() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("⏭️ Testing Skip 2FA Setup");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Look for Skip option in 2FA setup
            List<WebElement> skipElements = driver.findElements(By.xpath(
                "//button[contains(text(), 'Skip')] | " +
                "//a[contains(text(), 'Skip')] | " +
                "//*[contains(@class, 'skip')]"
            ));
            
            if (skipElements.size() > 0) {
                skipElements.get(0).click();
                Thread.sleep(3000);
                System.out.println("✅ Clicked Skip button");
                
                // Verify navigation to main application
                String currentUrl = driver.getCurrentUrl();
                boolean skippedSuccessfully = currentUrl.contains("/dashboard") || 
                                           currentUrl.contains("/account") ||
                                           currentUrl.contains("/main") ||
                                           !driver.getPageSource().contains("Set up 2-Factor");
                
                assertTrue(skippedSuccessfully, "Should navigate to main app after skipping 2FA");
                System.out.println("✅ Successfully skipped 2FA setup");
                
            } else {
                System.out.println("⚠️ Skip option not found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during skip 2FA test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testPostLoginNavigation() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🧭 Testing Post-Login Navigation");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Skip 2FA if present
            skipTwoFactorSetupIfPresent();
            
            // Test navigation elements
            List<WebElement> navElements = driver.findElements(By.xpath(
                "//nav | //header | //*[contains(@class, 'navigation')] | " +
                "//*[contains(@class, 'menu')] | //*[contains(@class, 'sidebar')]"
            ));
            
            if (navElements.size() > 0) {
                System.out.println("✅ Navigation elements found: " + navElements.size());
            }
            
            // Look for common navigation items
            String[] commonNavItems = {"Dashboard", "Account", "Profile", "Settings", "Security", "Wallet", "Trading", "History"};
            
            for (String navItem : commonNavItems) {
                List<WebElement> navItemElements = driver.findElements(By.xpath(
                    "//*[contains(text(), '" + navItem + "')] | " +
                    "//a[contains(@href, '" + navItem.toLowerCase() + "')] | " +
                    "//button[contains(text(), '" + navItem + "')]"
                ));
                
                if (navItemElements.size() > 0) {
                    System.out.println("✅ Found navigation item: " + navItem);
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during post-login navigation test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testUserAccountInfo() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("👤 Testing User Account Information Display");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Skip 2FA if present
            skipTwoFactorSetupIfPresent();
            
            // Look for user email display
            List<WebElement> emailElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'hainam') or contains(text(), '@gmail.com')] | " +
                "//*[contains(@class, 'email')] | " +
                "//input[@value='hainam11993@gmail.com']"
            ));
            
            if (emailElements.size() > 0) {
                System.out.println("✅ User email found in interface");
            }
            
            // Look for user profile/avatar
            List<WebElement> profileElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'avatar')] | " +
                "//*[contains(@class, 'profile')] | " +
                "//img[contains(@alt, 'profile') or contains(@alt, 'user')] | " +
                "//*[contains(@class, 'user-info')]"
            ));
            
            if (profileElements.size() > 0) {
                System.out.println("✅ User profile element found");
            }
            
            // Look for account status indicators
            List<WebElement> statusElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'status')] | " +
                "//*[contains(text(), 'Verified') or contains(text(), 'Active')] | " +
                "//*[contains(@class, 'badge')] | " +
                "//*[contains(@class, 'indicator')]"
            ));
            
            if (statusElements.size() > 0) {
                System.out.println("✅ Account status indicators found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during user account info test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testComplete2FASetupWithValidCode() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("🔐 Testing Complete 2FA Setup with Valid Code");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Click Authenticator App option
            WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), 'Authenticator App')]")
            ));
            authenticatorOption.click();
            Thread.sleep(2000);
            System.out.println("✅ Selected Authenticator App");
            
            // Verify QR code and manual entry are displayed
            verifyQRCodeAndManualEntry();
            
            // Enter a test verification code (6 digits)
            WebElement codeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='text' or @type='number'] | " +
                        "//input[contains(@placeholder, 'code') or contains(@placeholder, 'Code')] | " +
                        "//input[contains(@name, 'code') or contains(@id, 'code')]")
            ));
            codeInput.clear();
            codeInput.sendKeys("123456"); // Test code
            System.out.println("✅ Entered verification code");
            
            // Click Continue button
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Continue') or contains(text(), 'Verify')] | " +
                        "//button[contains(text(), 'Submit') or contains(text(), 'Confirm')]")
            ));
            continueButton.click();
            System.out.println("✅ Clicked Continue button");
            Thread.sleep(3000);
            
            // Verify completion or error handling
            verifySetupCompletion();
            
        } catch (Exception e) {
            System.out.println("❌ Error during complete 2FA setup test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testComplete2FASetupWithInvalidCode() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("❌ Testing 2FA Setup with Invalid Code");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Click Authenticator App option
            WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), 'Authenticator App')]")
            ));
            authenticatorOption.click();
            Thread.sleep(2000);
            
            // Enter invalid verification code
            WebElement codeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='text' or @type='number'] | " +
                        "//input[contains(@placeholder, 'code')]")
            ));
            codeInput.clear();
            codeInput.sendKeys("000000"); // Invalid code
            System.out.println("✅ Entered invalid verification code");
            
            // Click Continue button
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Continue') or contains(text(), 'Verify')]")
            ));
            continueButton.click();
            Thread.sleep(2000);
            
            // Verify error message is displayed
            List<WebElement> errorElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'error') or contains(@class, 'invalid')] | " +
                "//*[contains(text(), 'Invalid') or contains(text(), 'incorrect')] | " +
                "//*[contains(text(), 'wrong') or contains(text(), 'failed')] | " +
                "//*[contains(text(), 'try again')]"
            ));
            
            if (errorElements.size() > 0) {
                System.out.println("✅ Error message displayed for invalid code");
                assertTrue(errorElements.get(0).isDisplayed(), "Error message should be visible");
            } else {
                System.out.println("⚠️ No error message found - may need to check validation");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during invalid code test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testComplete2FASetupWithEmptyCode() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("❌ Testing 2FA Setup with Empty Code");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Click Authenticator App option
            WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), 'Authenticator App')]")
            ));
            authenticatorOption.click();
            Thread.sleep(2000);
            
            // Leave code field empty and try to continue
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Continue') or contains(text(), 'Verify')]")
            ));
            continueButton.click();
            Thread.sleep(2000);
            
            // Verify validation message
            List<WebElement> validationElements = driver.findElements(By.xpath(
                "//*[contains(@class, 'required') or contains(@class, 'error')] | " +
                "//*[contains(text(), 'required') or contains(text(), 'Required')] | " +
                "//*[contains(text(), 'Please enter') or contains(text(), 'This field')] | " +
                "//*[contains(text(), 'cannot be empty')]"
            ));
            
            if (validationElements.size() > 0) {
                System.out.println("✅ Validation message displayed for empty code");
                assertTrue(validationElements.get(0).isDisplayed(), "Validation message should be visible");
            } else {
                System.out.println("⚠️ No validation message found for empty code");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during empty code test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testQRCodeAccessibility() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("♿ Testing QR Code Accessibility Features");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Click Authenticator App option
            WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), 'Authenticator App')]")
            ));
            authenticatorOption.click();
            Thread.sleep(2000);
            
            // Check for QR code accessibility
            List<WebElement> qrCodeElements = driver.findElements(By.xpath(
                "//img[contains(@alt, 'QR')] | " +
                "//canvas | //svg | " +
                "//*[contains(@aria-label, 'QR') or contains(@aria-label, 'code')]"
            ));
            
            if (qrCodeElements.size() > 0) {
                WebElement qrElement = qrCodeElements.get(0);
                String altText = qrElement.getAttribute("alt");
                String ariaLabel = qrElement.getAttribute("aria-label");
                
                if (altText != null && !altText.isEmpty()) {
                    System.out.println("✅ QR code has alt text: " + altText);
                }
                if (ariaLabel != null && !ariaLabel.isEmpty()) {
                    System.out.println("✅ QR code has aria-label: " + ariaLabel);
                }
            }
            
            // Check for manual entry option (accessibility alternative)
            List<WebElement> manualElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'manually') or contains(text(), 'copy')] | " +
                "//*[contains(text(), 'otpauth://') or contains(text(), 'secret')] | " +
                "//input[@type='text' and @readonly]"
            ));
            
            if (manualElements.size() > 0) {
                System.out.println("✅ Manual entry option available for accessibility");
            }
            
            // Test copy functionality if available
            List<WebElement> copyButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Copy') or contains(@title, 'Copy')] | " +
                "//*[contains(@class, 'copy')]"
            ));
            
            if (copyButtons.size() > 0) {
                copyButtons.get(0).click();
                System.out.println("✅ Copy button functionality tested");
                Thread.sleep(1000);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during QR code accessibility test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testManualCodeEntry() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("📝 Testing Manual Code Entry Option");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Click Authenticator App option
            WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), 'Authenticator App')]")
            ));
            authenticatorOption.click();
            Thread.sleep(2000);
            
            // Look for manual entry code display
            List<WebElement> manualCodeElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'otpauth://totp/ZITADEL:hainam38493@gmail.com')] | " +
                "//*[contains(text(), 'hainam38493@gmail.com')] | " +
                "//input[@type='text' and @readonly] | " +
                "//*[contains(text(), 'SHA1') and contains(text(), 'digits=6')]"
            ));
            
            if (manualCodeElements.size() > 0) {
                WebElement codeElement = manualCodeElements.get(0);
                String codeText = codeElement.getText();
                
                if (codeText != null && !codeText.isEmpty()) {
                    System.out.println("✅ Manual code found: " + codeText.substring(0, Math.min(50, codeText.length())) + "...");
                    
                    // Verify code contains expected elements
                    assertTrue(codeText.toLowerCase().contains("hainam38493@gmail.com"), 
                              "Code should contain user email");
                    System.out.println("✅ Manual code contains user email");
                }
            } else {
                System.out.println("⚠️ Manual code entry not found");
            }
            
            // Test if manual code can be selected/copied
            if (manualCodeElements.size() > 0) {
                WebElement codeElement = manualCodeElements.get(0);
                codeElement.click();
                System.out.println("✅ Manual code element clicked");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during manual code entry test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testBackButtonFunctionality() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("⬅️ Testing Back Button Functionality");
            
            // Perform login first
            performValidLogin(wait);
            Thread.sleep(3000);
            
            // Click Authenticator App option
            WebElement authenticatorOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), 'Authenticator App')]")
            ));
            authenticatorOption.click();
            Thread.sleep(2000);
            System.out.println("✅ Navigated to Authenticator App setup");
            
            // Verify we're on the QR code screen
            List<WebElement> qrElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'Scan the QR Code')] | " +
                "//canvas | //img[contains(@alt, 'QR')] | //svg"
            ));
            assertTrue(qrElements.size() > 0, "Should be on QR code screen");
            
            // Click Back button
            List<WebElement> backButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Back')] | " +
                "//*[contains(@class, 'back')] | " +
                "//button[contains(@aria-label, 'back')]"
            ));
            
            if (backButtons.size() > 0) {
                backButtons.get(0).click();
                Thread.sleep(2000);
                System.out.println("✅ Clicked Back button");
                
                // Verify we're back to the 2FA options screen
                List<WebElement> optionsElements = driver.findElements(By.xpath(
                    "//*[contains(text(), 'Set up 2-Factor')] | " +
                    "//*[contains(text(), 'Authenticator App')] | " +
                    "//*[contains(text(), 'Universal Second Factor')]"
                ));
                
                assertTrue(optionsElements.size() > 0, "Should be back to 2FA options screen");
                System.out.println("✅ Successfully returned to 2FA options screen");
                
                // Test Universal Second Factor option now
                List<WebElement> usfElements = driver.findElements(By.xpath(
                    "//*[contains(text(), 'Universal Second Factor')]"
                ));
                
                if (usfElements.size() > 0) {
                    usfElements.get(0).click();
                    Thread.sleep(2000);
                    System.out.println("✅ Tested Universal Second Factor option after back navigation");
                }
                
            } else {
                System.out.println("⚠️ Back button not found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during back button test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Helper Methods

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
                System.out.println("✅ 2FA setup dialog detected after login");
                
                // Verify 2FA setup options are available
                List<WebElement> authenticatorOption = driver.findElements(By.xpath(
                    "//*[contains(text(), 'Authenticator App')]"
                ));
                
                List<WebElement> usfOption = driver.findElements(By.xpath(
                    "//*[contains(text(), 'Universal Second Factor')]"
                ));
                
                if (authenticatorOption.size() > 0) {
                    System.out.println("✅ Authenticator App option found");
                    
                    // Click Authenticator App option
                    authenticatorOption.get(0).click();
                    Thread.sleep(2000);
                    System.out.println("✅ Selected Authenticator App");
                    
                    // Complete the 2FA setup process
                    complete2FASetupProcess(wait);
                    
                } else if (usfOption.size() > 0) {
                    System.out.println("✅ Universal Second Factor option found");
                    // Handle USF setup if needed
                    handleUSFSetup(wait);
                } else {
                    System.out.println("⚠️ No 2FA options found, checking for skip option");
                    skipOrComplete2FASetup(wait);
                }
                
            } else {
                System.out.println("ℹ️ No 2FA setup dialog found - login may be complete");
                // Verify final login state
                verifyFinalLoginState();
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Error during 2FA setup handling: " + e.getMessage());
            // Try to skip or continue anyway
            skipOrComplete2FASetup(wait);
        }
    }
    
    private void complete2FASetupProcess(WebDriverWait wait) throws InterruptedException {
        System.out.println("📱 Completing 2FA setup process...");
        
        try {
            // Wait for QR code and setup interface to load
            Thread.sleep(2000);
            
            // Verify QR code or manual entry is displayed
            List<WebElement> qrCodeElements = driver.findElements(By.xpath(
                "//canvas | //img[contains(@alt, 'QR')] | //svg | " +
                "//*[contains(text(), 'Scan the QR Code')]"
            ));
            
            if (qrCodeElements.size() > 0) {
                System.out.println("✅ QR code displayed for 2FA setup");
            }
            
            // Check for manual entry code
            List<WebElement> manualCodeElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'otpauth://') or contains(text(), 'hainam38493@gmail.com')] | " +
                "//input[@type='text' and @readonly] | " +
                "//*[contains(text(), 'manually')]"
            ));
            
            if (manualCodeElements.size() > 0) {
                System.out.println("✅ Manual entry code available");
            }
            
            // Look for verification code input field
            List<WebElement> codeInputs = driver.findElements(By.xpath(
                "//input[@type='text' or @type='number'] | " +
                "//input[contains(@placeholder, 'code') or contains(@placeholder, 'Code')] | " +
                "//input[contains(@name, 'code') or contains(@id, 'code')]"
            ));
            
            if (codeInputs.size() > 0) {
                System.out.println("✅ Verification code input field found");
                
                // Enter a test verification code
                WebElement codeInput = codeInputs.get(0);
                codeInput.clear();
                codeInput.sendKeys("123456"); // Test code
                System.out.println("✅ Entered test verification code: 123456");
                
                // Look for Continue/Submit button
                List<WebElement> continueButtons = driver.findElements(By.xpath(
                    "//button[contains(text(), 'Continue')] | " +
                    "//button[contains(text(), 'Verify')] | " +
                    "//button[contains(text(), 'Submit')] | " +
                    "//button[contains(text(), 'Confirm')]"
                ));
                
                if (continueButtons.size() > 0) {
                    continueButtons.get(0).click();
                    System.out.println("✅ Clicked Continue button");
                    Thread.sleep(3000);
                    
                    // Check for setup completion or error
                    verifySetupResult();
                } else {
                    System.out.println("⚠️ Continue button not found");
                }
            } else {
                System.out.println("⚠️ Verification code input not found, trying to skip");
                skipOrComplete2FASetup(wait);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during 2FA setup completion: " + e.getMessage());
            skipOrComplete2FASetup(wait);
        }
    }
    
    private void handleUSFSetup(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔑 Handling Universal Second Factor setup...");
        
        List<WebElement> usfElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'Universal Second Factor')]"
        ));
        
        if (usfElements.size() > 0) {
            usfElements.get(0).click();
            Thread.sleep(2000);
            System.out.println("✅ Selected Universal Second Factor");
            
            // Check for setup instructions
            List<WebElement> setupInstructions = driver.findElements(By.xpath(
                "//*[contains(text(), 'USB') or contains(text(), 'security key')] | " +
                "//*[contains(text(), 'hardware') or contains(text(), 'device')]"
            ));
            
            if (setupInstructions.size() > 0) {
                System.out.println("✅ USF setup instructions found");
            }
            
            // For testing purposes, go back and try skip
            List<WebElement> backButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Back')]"
            ));
            
            if (backButtons.size() > 0) {
                backButtons.get(0).click();
                Thread.sleep(1000);
                System.out.println("✅ Went back from USF setup");
            }
        }
        
        // Try to skip after checking USF
        skipOrComplete2FASetup(wait);
    }
    
    private void skipOrComplete2FASetup(WebDriverWait wait) throws InterruptedException {
        System.out.println("⏭️ Attempting to skip or complete 2FA setup...");
        
        // Look for Skip button
        List<WebElement> skipButtons = driver.findElements(By.xpath(
            "//button[contains(text(), 'Skip')] | " +
            "//a[contains(text(), 'Skip')] | " +
            "//*[contains(@class, 'skip')] | " +
            "//button[contains(text(), 'Later')] | " +
            "//button[contains(text(), 'Not now')]"
        ));
        
        if (skipButtons.size() > 0) {
            skipButtons.get(0).click();
            Thread.sleep(3000);
            System.out.println("✅ Successfully skipped 2FA setup");
            verifyFinalLoginState();
            return;
        }
        
        // Look for close button or overlay close
        List<WebElement> closeButtons = driver.findElements(By.xpath(
            "//button[contains(@class, 'close')] | " +
            "//*[contains(@aria-label, 'close')] | " +
            "//button[text()='×'] | " +
            "//button[contains(@class, 'modal-close')]"
        ));
        
        if (closeButtons.size() > 0) {
            closeButtons.get(0).click();
            Thread.sleep(2000);
            System.out.println("✅ Closed 2FA setup dialog");
            verifyFinalLoginState();
            return;
        }
        
        // If no skip/close options, try pressing Escape key
        try {
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
            Thread.sleep(2000);
            System.out.println("✅ Pressed Escape to close dialog");
        } catch (Exception e) {
            System.out.println("⚠️ Could not press Escape key");
        }
        
        verifyFinalLoginState();
    }
    
    private void verifySetupResult() {
        System.out.println("🔍 Verifying 2FA setup result...");
        
        // Check for success messages
        List<WebElement> successElements = driver.findElements(By.xpath(
            "//*[contains(@class, 'success')] | " +
            "//*[contains(text(), 'successfully')] | " +
            "//*[contains(text(), 'completed')] | " +
            "//*[contains(text(), 'enabled')]"
        ));
        
        if (successElements.size() > 0) {
            System.out.println("✅ 2FA setup appears to be successful");
            return;
        }
        
        // Check for error messages
        List<WebElement> errorElements = driver.findElements(By.xpath(
            "//*[contains(@class, 'error')] | " +
            "//*[contains(text(), 'Invalid')] | " +
            "//*[contains(text(), 'incorrect')] | " +
            "//*[contains(text(), 'failed')]"
        ));
        
        if (errorElements.size() > 0) {
            System.out.println("⚠️ Error detected during 2FA setup: " + errorElements.get(0).getText());
            
            // Try to skip after error
            try {
                skipOrComplete2FASetup(new WebDriverWait(driver, Duration.ofSeconds(10)));
            } catch (Exception e) {
                System.out.println("⚠️ Could not handle error recovery");
            }
        } else {
            System.out.println("ℹ️ No clear success or error message found");
        }
    }
    
    private void verifyFinalLoginState() {
        System.out.println("🔍 Verifying final login state...");
        
        String currentUrl = driver.getCurrentUrl();
        System.out.println("📍 Final URL: " + currentUrl);
        
        // Check if we're successfully logged in and past 2FA setup
        boolean finalLoginSuccessful = currentUrl.contains("/dashboard") || 
                                     currentUrl.contains("/account") || 
                                     currentUrl.contains("/main") ||
                                     currentUrl.contains("/home") ||
                                     (!currentUrl.contains("/login") && 
                                      !driver.getPageSource().contains("Set up 2-Factor"));
        
        if (finalLoginSuccessful) {
            System.out.println("✅ Final login state verified - user successfully logged in");
            
            // Look for user-specific elements
            List<WebElement> userElements = driver.findElements(By.xpath(
                "//*[contains(text(), 'hainam') or contains(text(), '@gmail.com')] | " +
                "//*[contains(@class, 'user')] | " +
                "//*[contains(text(), 'logout') or contains(text(), 'Logout')]"
            ));
            
            if (userElements.size() > 0) {
                System.out.println("✅ User-specific elements found in the interface");
            }
            
        } else {
            System.out.println("⚠️ Final login state unclear - may need manual verification");
        }
    }

    private void testVerificationCodeEntry(WebDriverWait wait) throws InterruptedException {
        System.out.println("🔢 Testing verification code entry...");
        
        // Look for code input field
        List<WebElement> codeInputs = driver.findElements(By.xpath(
            "//input[@type='text' or @type='number'] | " +
            "//input[contains(@placeholder, 'code') or contains(@placeholder, 'Code')] | " +
            "//input[contains(@name, 'code')]"
        ));
        
        if (codeInputs.size() > 0) {
            WebElement codeInput = codeInputs.get(0);
            assertTrue(codeInput.isDisplayed(), "Code input field should be visible");
            assertTrue(codeInput.isEnabled(), "Code input field should be enabled");
            System.out.println("✅ Code input field verified");
            
            // Test entering a code
            codeInput.sendKeys("123456");
            System.out.println("✅ Test code entered");
            
            // Look for Continue button
            List<WebElement> continueButtons = driver.findElements(By.xpath(
                "//button[contains(text(), 'Continue') or contains(text(), 'Verify')]"
            ));
            
            if (continueButtons.size() > 0) {
                assertTrue(continueButtons.get(0).isDisplayed(), "Continue button should be visible");
                System.out.println("✅ Continue button verified");
            }
        }
    }

    private void verifyQRCodeAndManualEntry() {
        System.out.println("🔍 Verifying QR code and manual entry display...");
        
        // Check QR code display
        List<WebElement> qrElements = driver.findElements(By.xpath(
            "//canvas | //img[contains(@alt, 'QR')] | //svg | " +
            "//*[contains(text(), 'Scan the QR Code')]"
        ));
        
        if (qrElements.size() > 0) {
            assertTrue(qrElements.get(0).isDisplayed(), "QR code should be visible");
            System.out.println("✅ QR code is displayed");
        }
        
        // Check manual entry text
        List<WebElement> manualElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'otpauth://') or contains(text(), 'hainam38493@gmail.com')] | " +
            "//*[contains(text(), 'manually')] | " +
            "//input[@type='text' and @readonly]"
        ));
        
        if (manualElements.size() > 0) {
            System.out.println("✅ Manual entry option is displayed");
        }
    }

    private void verifySetupCompletion() {
        System.out.println("🔍 Verifying setup completion...");
        
        // Check for success indicators
        List<WebElement> successElements = driver.findElements(By.xpath(
            "//*[contains(@class, 'success')] | " +
            "//*[contains(text(), 'completed') or contains(text(), 'success')] | " +
            "//*[contains(text(), 'enabled') or contains(text(), 'active')]"
        ));
        
        if (successElements.size() > 0) {
            System.out.println("✅ Setup completion indicators found");
        }
        
        // Check for navigation to next step or dashboard
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("/dashboard") || currentUrl.contains("/account") || 
            currentUrl.contains("/security")) {
            System.out.println("✅ Successfully navigated after 2FA setup");
        }
        
        // Check for error messages
        List<WebElement> errorElements = driver.findElements(By.xpath(
            "//*[contains(@class, 'error')] | " +
            "//*[contains(text(), 'Invalid') or contains(text(), 'failed')]"
        ));
        
        if (errorElements.size() > 0) {
            System.out.println("⚠️ Error message detected: " + errorElements.get(0).getText());
        } else {
            System.out.println("✅ No error messages found");
        }
    }

    private void skipTwoFactorSetupIfPresent() throws InterruptedException {
        // Skip 2FA setup if the dialog is present
        List<WebElement> skipElements = driver.findElements(By.xpath(
            "//button[contains(text(), 'Skip')] | //a[contains(text(), 'Skip')]"
        ));
        
        if (skipElements.size() > 0) {
            skipElements.get(0).click();
            Thread.sleep(2000);
            System.out.println("✅ Skipped 2FA setup");
        }
    }

    private void navigateToLoginPage(WebDriverWait wait) throws InterruptedException {
        // Click Login button to open login form
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
        ));
        loginButton.click();
        Thread.sleep(2000);
    }

    private void performValidLogin(WebDriverWait wait) throws InterruptedException {
        // Navigate to login page
        navigateToLoginPage(wait);
        
        // Enter valid credentials
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='email']")
        ));
        emailInput.clear();
        emailInput.sendKeys("hainam11993@gmail.com");
        
        WebElement passwordInput = driver.findElement(
            By.xpath("//input[@type='password']")
        );
        passwordInput.clear();
        passwordInput.sendKeys("ValidPassword123@");
        
        // Click login
        WebElement loginSubmitButton = driver.findElement(
            By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or @type='submit']")
        );
        loginSubmitButton.click();
        Thread.sleep(5000);
    }
}
