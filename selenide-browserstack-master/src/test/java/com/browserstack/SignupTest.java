package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SignupTest extends BrowserStackTest {

    @Test
    public void testValidPhoneNumberSignup() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000); // Wait for page to fully load
        
        // Wait for page to load and click Login first
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Try to find and click the Login button with case-sensitive text
        try {
            // Try multiple possible button texts
            WebElement loginButton = driver.findElement(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
            );
            System.out.println("Found button with text: " + loginButton.getText());
            loginButton.click();
        } catch (Exception e) {
            System.out.println("Error finding login button: " + e.getMessage());
            // Take screenshot for debugging
            // ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        }
        
        try {
            // Click Register account link
            WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Register') or contains(text(), 'Sign up')]")
            ));
            System.out.println("Found register button with text: " + registerButton.getText());
            registerButton.click();
            
            // Wait and switch to Phone tab
            WebElement phoneTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Phone')]")
            ));
            System.out.println("Found phone tab with text: " + phoneTab.getText());
            phoneTab.click();
            Thread.sleep(1000); // Wait for tab switch animation
            
            // Now enter phone number
            WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='tel' or @placeholder='Phone number']")
            ));
            phoneInput.clear(); // Clear any existing text
            phoneInput.sendKeys("+84383792093");
            System.out.println("Successfully entered phone number");
        } catch (Exception e) {
            System.out.println("Error during registration process: " + e.getMessage());
            throw e; // Re-throw to fail the test
        }
        
        // Click Continue
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Continue']"))).click();
        
        // Verify the next step is shown (this will need to be adjusted based on actual UI response)
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'verification-code')]")));
    }

    @Test
    public void testValidEmailSignup() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000); // Wait for page to fully load
        
        // Wait for page to load and click Login first
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
            emailInput.sendKeys("hainam38493@gmail.com");
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

            // Wait for verification code field and verify it's present
            WebElement verificationCodeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'verification-code') or contains(@class, 'otp-input')]")
            ));
            assertTrue(verificationCodeInput.isDisplayed(), "Verification code input should be displayed");
            System.out.println("Successfully reached verification code step");

        } catch (Exception e) {
            System.out.println("Error during email signup process: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to fail the test
        }
    }

    @Test
    public void testInvalidEmail() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(2000);
        
        // Click on Sign up button
        driver.findElement(By.xpath("//button[text()='Sign-up']")).click();
        Thread.sleep(1000);

        // Enter invalid email
        driver.findElement(By.xpath("//input[@type='email']")).sendKeys("abc@@gmail");
        Thread.sleep(1000);
        
        // Click Sign up
        driver.findElement(By.xpath("//button[text()='Sign-up']")).click();
        Thread.sleep(1000);

        // Verify error message
        String errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Invalid email address')]")).getText();
        assertEquals(errorMessage, "Invalid email address");
    }

    @Test
    public void testInvalidPhoneNumber() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(2000);
        
        // Click on Sign up button
        driver.findElement(By.xpath("//button[text()='Sign-up']")).click();
        Thread.sleep(1000);

        // Enter invalid phone number
        driver.findElement(By.xpath("//input[@type='tel']")).sendKeys("123");
        Thread.sleep(1000);
        
        // Click Sign up
        driver.findElement(By.xpath("//button[text()='Sign-up']")).click();
        Thread.sleep(1000);

        // Verify error message
        String errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Invalid phone number')]")).getText();
        assertEquals(errorMessage, "Invalid phone number");
    }

    @Test
    public void testExistingEmail() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(2000);
        
        // Click on Sign up button
        driver.findElement(By.xpath("//button[text()='Sign-up']")).click();
        Thread.sleep(1000);

        // Enter existing email
        driver.findElement(By.xpath("//input[@type='email']")).sendKeys("namnh@gmail.com");
        Thread.sleep(1000);
        
        // Click Sign up
        driver.findElement(By.xpath("//button[text()='Sign-up']")).click();
        Thread.sleep(1000);

        // Verify error message
        String errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Email already exists')]")).getText();
        assertEquals(errorMessage, "Email already exists");
    }

    @Test
    public void testExistingPhoneNumber() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(2000);
        
        // Click on Sign up button
        driver.findElement(By.xpath("//button[text()='Sign-up']")).click();
        Thread.sleep(1000);

        // Enter existing phone number
        driver.findElement(By.xpath("//input[@type='tel']")).sendKeys("0987654321");
        Thread.sleep(1000);
        
        // Click Sign up
        driver.findElement(By.xpath("//button[text()='Sign-up']")).click();
        Thread.sleep(1000);

        // Verify error message
        String errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Phone number already exists')]")).getText();
        assertEquals(errorMessage, "Phone number already exists");
    }

    @Test
    public void testPasswordTooShort() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        navigateToPasswordScreen("test@example.com");
        
        // Enter short password
        WebElement passwordInput = driver.findElement(
            By.xpath("//input[@type='password'][1]")  // First password field
        );
        passwordInput.sendKeys("Abc@1");  // Only 5 characters
        
        // Verify error message
        WebElement errorMessage = driver.findElement(
            By.xpath("//*[contains(@class, 'error') and contains(text(), 'Password length')]")
        );
        assertTrue(errorMessage.isDisplayed());
    }

    @Test
    public void testPasswordWithoutSymbol() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        navigateToPasswordScreen("test@example.com");
        
        // Enter password without symbol
        WebElement passwordInput = driver.findElement(
            By.xpath("//input[@type='password' and (contains(@placeholder, 'Password') and not(contains(@placeholder, 'Confirm')))]")
        );
        passwordInput.sendKeys("Password123");  // No symbol
        
        // Verify error message
        WebElement errorMessage = driver.findElement(
            By.xpath("//*[contains(@class, 'error') and contains(text(), 'Symbol')]")
        );
        assertTrue(errorMessage.isDisplayed());
    }

    @Test
    public void testPasswordWithoutNumber() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        navigateToPasswordScreen("test@example.com");
        
        // Enter password without number
        WebElement passwordInput = driver.findElement(
            By.xpath("//input[@type='password' and (contains(@placeholder, 'Password') and not(contains(@placeholder, 'Confirm')))]")
        );
        passwordInput.sendKeys("Password@");  // No number
        
        // Verify error message
        WebElement errorMessage = driver.findElement(
            By.xpath("//*[contains(@class, 'error') and contains(text(), 'Number')]")
        );
        assertTrue(errorMessage.isDisplayed());
    }

    @Test
    public void testPasswordWithoutUppercase() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        navigateToPasswordScreen("test@example.com");
        
        // Enter password without uppercase
        WebElement passwordInput = driver.findElement(
            By.xpath("//input[@type='password' and (contains(@placeholder, 'Password') and not(contains(@placeholder, 'Confirm')))]")
        );
        passwordInput.sendKeys("password@123");  // No uppercase
        
        // Verify error message
        WebElement errorMessage = driver.findElement(
            By.xpath("//*[contains(@class, 'error') and contains(text(), 'uppercase')]")
        );
        assertTrue(errorMessage.isDisplayed());
    }

    @Test
    public void testPasswordWithoutLowercase() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        navigateToPasswordScreen("test@example.com");
        
        // Enter password without lowercase
        WebElement passwordInput = driver.findElement(
            By.xpath("//input[@type='password' and (contains(@placeholder, 'Password') and not(contains(@placeholder, 'Confirm')))]")
        );
        passwordInput.sendKeys("PASSWORD@123");  // No lowercase
        
        // Verify error message
        WebElement errorMessage = driver.findElement(
            By.xpath("//*[contains(@class, 'error') and contains(text(), 'lowercase')]")
        );
        assertTrue(errorMessage.isDisplayed());
    }

    @Test
    public void testPasswordsDontMatch() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        navigateToPasswordScreen("test@example.com");
        
        // Enter password
        WebElement passwordInput = driver.findElement(
            By.xpath("//input[@type='password' and (contains(@placeholder, 'Password') and not(contains(@placeholder, 'Confirm')))]")
        );
        passwordInput.sendKeys("Password@123");
        
        // Enter different confirm password
        WebElement confirmPasswordInput = driver.findElement(
            By.xpath("//input[@type='password' and contains(@placeholder, 'Confirm')]")
        );
        confirmPasswordInput.sendKeys("Password@124");  // Different password
        
        // Verify error message
        WebElement errorMessage = driver.findElement(
            By.xpath("//*[contains(@class, 'error') and contains(text(), 'Passwords do not match')]")
        );
        assertTrue(errorMessage.isDisplayed());
    }

    // Helper method to navigate to password screen
    private void navigateToPasswordScreen(String email) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Click Login button
        WebElement loginButton = driver.findElement(
            By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
        );
        loginButton.click();
        
        // Click Register button
        WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Register') or contains(text(), 'Sign up')]")
        ));
        registerButton.click();
        
        // Enter email
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='email']")
        ));
        emailInput.sendKeys(email);
        
        // Click Continue
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[text()='Continue']")
        )).click();
        
        // Enter name details
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[contains(@placeholder, 'first name')]")
        )).sendKeys("Test");
        
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[contains(@placeholder, 'last name')]")
        )).sendKeys("User");
        
        // Click Continue again
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[text()='Continue']")
        )).click();
    }
}
