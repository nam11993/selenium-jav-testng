package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class Loginwithoutverifyemail extends LocalBrowserTest {

    private final String BASE_URL = "https://fcex-fe-718949727112.asia-southeast1.run.app/en";

    @Test
    public void testValidEmailSignup() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(3000); // Wait for page to fully load
        
        // Wait for page to load and click Login first
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            // Try multiple possible button texts for Login
            WebElement loginButton = driver.findElement(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            );
            System.out.println("Found login button with text: " + loginButton.getText());
            loginButton.click();
            
            // Click Register account link
            WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Register') or contains(text(), 'Sign up')]")
            ));
            System.out.println("Found register button with text: " + registerButton.getText());
            registerButton.click();
            
            // Wait for email input and enter email
            WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='email']")
            ));
            emailInput.clear(); // Clear any existing text
            emailInput.sendKeys("tuanna15091999@gmail.com");
            System.out.println("Successfully entered email");
            
            // Click Continue button
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Continue']")
            ));
            continueButton.click();
            System.out.println("Clicked continue button");
            
            // Enter First Name
            WebElement firstNameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[contains(@placeholder, 'first name')]")
            ));
            firstNameInput.clear();
            firstNameInput.sendKeys("Hai");
            System.out.println("Entered first name");

            // Enter Last Name
            WebElement lastNameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[contains(@placeholder, 'last name')]")
            ));
            lastNameInput.clear();
            lastNameInput.sendKeys("Nam");
            System.out.println("Entered last name");

            // Click Continue after entering names
            WebElement nextContinueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Continue']")
            ));
            nextContinueButton.click();
            System.out.println("Clicked continue after entering names");

            // Wait for password field and enter password
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"password\"]")  // First password field is always the new password
            ));
            String password = "Hainam12@"; // Meets all requirements: length 8+, symbol, number, upper/lowercase
            passwordInput.sendKeys(password);
            System.out.println("Entered password");

            // Enter confirm password
            WebElement confirmPasswordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"confirmPassword\"]")  // Second password field is always the confirm password
            ));
            confirmPasswordInput.sendKeys(password);
            System.out.println("Entered confirm password");

            // Click Continue after setting password
            WebElement passwordContinueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Continue']")
            ));
            passwordContinueButton.click();
            System.out.println("Clicked continue after setting password");

        } catch (Exception e) {
            System.out.println("❌ Error during valid email login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }    


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
            emailInput.sendKeys("tuanna15091999@gmail.com");
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
            passwordInput.sendKeys("Hainam12@");
            System.out.println("✅ Entered password");
            
            // Step 4: Click login/sign in button
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login') or contains(text(), 'Submit') or @type='submit']")
            ));
            signInButton.click();
            System.out.println("✅ Clicked sign in button");
            Thread.sleep(5000);
            // Wait for verification code field to appear first
            WebElement verificationCodeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name='code'] | //div[contains(@class, 'verification-code')] | //div[contains(@class, 'otp-input')]")
            ));
            assertTrue(verificationCodeInput.isDisplayed(), "Verification code input should be displayed");
            System.out.println("Successfully reached verification code step");

            // Step 2: Fetch OTP from Gmail using SignupTest method
            String otp = SignupTest.fetchOtpFromGmail();
            System.out.println("OTP received: " + otp);

            // Step 3: Enter OTP and verify
            WebElement codeInput = driver.findElement(By.xpath("//input[@name='code']"));
            codeInput.clear();
            codeInput.sendKeys(otp);
            System.out.println("Entered OTP code: " + otp);
            
            // Click Continue to submit OTP
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Continue' or text()='Submit' or text()='Verify']")
            ));
            submitButton.click();
            System.out.println("Clicked continue/submit button after entering OTP");
            
            // Wait a moment for verification to process
            Thread.sleep(3000);
            System.out.println("Email signup with OTP verification completed successfully");

            // click skip button 2 FA
            WebElement skipButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Skip']")
            ));
            skipButton.click();
            System.out.println("Clicked skip button 2 FA");

        } catch (Exception e) {
            System.out.println("Error during email signup process: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e); // Wrap checked exception in unchecked to avoid compile error
        }
    }
}