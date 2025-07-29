package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class PhoneLoginTest extends LocalBrowserTest {

    private final String BASE_URL = "https://fcex-fe-718949727112.asia-southeast1.run.app/en";

    @Test
    public void testValidPhoneNumberLogin() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("📱 Testing Valid Phone Number Login");
            
            // Navigate to login page
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Look for phone tab/option
            List<WebElement> phoneTabElements = driver.findElements(By.xpath(
                "//button[contains(text(), 'Phone') or contains(text(), 'Mobile')] | " +
                "//tab[contains(text(), 'Phone')] | " +
                "//*[contains(@class, 'phone-tab')]"
            ));
            
            if (phoneTabElements.size() > 0) {
                System.out.println("✅ Found phone login option");
                phoneTabElements.get(0).click();
                Thread.sleep(1000);
                
                // Enter phone number
                WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='tel' or contains(@placeholder, 'phone') or contains(@name, 'phone')]")
                ));
                phoneInput.clear();
                phoneInput.sendKeys("+84383792093");
                System.out.println("✅ Entered phone number");
                
                // Enter password (if required for phone login)
                List<WebElement> passwordInputs = driver.findElements(
                    By.xpath("//input[@type='password']")
                );
                
                if (passwordInputs.size() > 0) {
                    passwordInputs.get(0).clear();
                    passwordInputs.get(0).sendKeys("ValidPassword123@");
                    System.out.println("✅ Entered password for phone login");
                }
                
                // Click login button
                WebElement loginSubmitButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or @type='submit']")
                ));
                loginSubmitButton.click();
                Thread.sleep(5000);
                
                // Verify login or OTP step
                String currentUrl = driver.getCurrentUrl();
                boolean loginProcessed = currentUrl.contains("/dashboard") || 
                                       currentUrl.contains("/account") ||
                                       currentUrl.contains("/verify") ||
                                       driver.getPageSource().toLowerCase().contains("verification") ||
                                       driver.getPageSource().toLowerCase().contains("otp");
                
                assertTrue(loginProcessed, "Phone login should proceed to next step");
                System.out.println("✅ Phone login processed successfully");
                
            } else {
                System.out.println("⚠️ Phone login option not found - may use unified login");
                
                // Try entering phone number in main login field
                List<WebElement> mainInputs = driver.findElements(By.xpath(
                    "//input[@type='email' or @type='tel' or @type='text']"
                ));
                
                if (mainInputs.size() > 0) {
                    mainInputs.get(0).clear();
                    mainInputs.get(0).sendKeys("+84383792093");
                    System.out.println("✅ Entered phone number in main login field");
                    
                    // Continue with password if available
                    List<WebElement> passwordFields = driver.findElements(By.xpath("//input[@type='password']"));
                    if (passwordFields.size() > 0) {
                        passwordFields.get(0).sendKeys("ValidPassword123@");
                        
                        WebElement submitBtn = driver.findElement(
                            By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login')]")
                        );
                        submitBtn.click();
                        Thread.sleep(3000);
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during phone number login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testInvalidPhoneNumberLogin() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("❌ Testing Invalid Phone Number Login");
            
            // Navigate to login page
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Look for phone input or tab
            List<WebElement> phoneTabElements = driver.findElements(By.xpath(
                "//button[contains(text(), 'Phone')]"
            ));
            
            if (phoneTabElements.size() > 0) {
                phoneTabElements.get(0).click();
                Thread.sleep(1000);
            }
            
            // Enter invalid phone number
            List<WebElement> phoneInputs = driver.findElements(By.xpath(
                "//input[@type='tel' or contains(@placeholder, 'phone')]"
            ));
            
            if (phoneInputs.size() > 0) {
                phoneInputs.get(0).clear();
                phoneInputs.get(0).sendKeys("123456"); // Invalid short number
                System.out.println("✅ Entered invalid phone number");
                
                // Try to submit
                List<WebElement> submitButtons = driver.findElements(By.xpath(
                    "//button[contains(text(), 'Continue') or contains(text(), 'Login')]"
                ));
                
                if (submitButtons.size() > 0) {
                    submitButtons.get(0).click();
                    Thread.sleep(2000);
                    
                    // Verify error message
                    List<WebElement> errorElements = driver.findElements(By.xpath(
                        "//*[contains(@class, 'error')] | " +
                        "//*[contains(text(), 'Invalid') or contains(text(), 'invalid')] | " +
                        "//*[contains(text(), 'Please enter a valid phone')]"
                    ));
                    
                    assertTrue(errorElements.size() > 0, "Error message should be displayed for invalid phone");
                    System.out.println("✅ Error message displayed for invalid phone number");
                }
            } else {
                System.out.println("⚠️ Phone input field not found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during invalid phone login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testPhoneNumberOTPFlow() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            System.out.println("📱 Testing Phone Number OTP Flow");
            
            // Navigate to login and enter phone
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login')]")
            ));
            loginButton.click();
            Thread.sleep(2000);
            
            // Look for phone login option
            List<WebElement> phoneOptions = driver.findElements(By.xpath(
                "//button[contains(text(), 'Phone')] | " +
                "//*[contains(text(), 'SMS') or contains(text(), 'OTP')]"
            ));
            
            if (phoneOptions.size() > 0) {
                phoneOptions.get(0).click();
                Thread.sleep(1000);
                
                // Enter phone number
                WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='tel' or contains(@placeholder, 'phone')]")
                ));
                phoneInput.clear();
                phoneInput.sendKeys("+84383792093");
                
                // Click send OTP or continue
                WebElement sendOTPButton = driver.findElement(By.xpath(
                    "//button[contains(text(), 'Send') or contains(text(), 'Continue') or contains(text(), 'Get OTP')]"
                ));
                sendOTPButton.click();
                Thread.sleep(3000);
                
                // Look for OTP input fields
                List<WebElement> otpInputs = driver.findElements(By.xpath(
                    "//input[contains(@placeholder, 'OTP') or contains(@placeholder, 'code')] | " +
                    "//*[contains(@class, 'otp')] | " +
                    "//input[@maxlength='1' or @maxlength='6']"
                ));
                
                if (otpInputs.size() > 0) {
                    System.out.println("✅ OTP input fields found: " + otpInputs.size());
                    
                    // Test entering OTP (use dummy OTP for testing)
                    if (otpInputs.size() == 1) {
                        // Single OTP input field
                        otpInputs.get(0).sendKeys("123456");
                    } else if (otpInputs.size() > 1) {
                        // Multiple OTP input fields (one digit each)
                        String otpCode = "123456";
                        for (int i = 0; i < Math.min(otpInputs.size(), otpCode.length()); i++) {
                            otpInputs.get(i).sendKeys(String.valueOf(otpCode.charAt(i)));
                        }
                    }
                    
                    System.out.println("✅ Entered OTP code");
                    
                    // Look for verify button
                    List<WebElement> verifyButtons = driver.findElements(By.xpath(
                        "//button[contains(text(), 'Verify') or contains(text(), 'Submit') or contains(text(), 'Confirm')]"
                    ));
                    
                    if (verifyButtons.size() > 0) {
                        verifyButtons.get(0).click();
                        Thread.sleep(3000);
                        System.out.println("✅ Clicked verify OTP button");
                        
                        // Note: This will likely fail with invalid OTP, but we're testing the flow
                        List<WebElement> otpErrors = driver.findElements(By.xpath(
                            "//*[contains(text(), 'Invalid') or contains(text(), 'incorrect')] | " +
                            "//*[contains(@class, 'error')]"
                        ));
                        
                        if (otpErrors.size() > 0) {
                            System.out.println("⚠️ Invalid OTP error displayed (expected for test OTP)");
                        }
                    }
                    
                    // Test resend OTP option
                    List<WebElement> resendButtons = driver.findElements(By.xpath(
                        "//button[contains(text(), 'Resend') or contains(text(), 'Send again')] | " +
                        "//a[contains(text(), 'Resend')]"
                    ));
                    
                    if (resendButtons.size() > 0) {
                        System.out.println("✅ Resend OTP option available");
                    }
                    
                } else {
                    System.out.println("⚠️ OTP input fields not found");
                }
                
            } else {
                System.out.println("⚠️ Phone/OTP login option not found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during phone OTP flow test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
