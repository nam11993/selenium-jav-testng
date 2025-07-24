package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Password extends LocalBrowserTest {

    @Test
    public void testPasswordTooShort() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000); 
        
        // Wait for page to load and click Login first
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
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
            emailInput.sendKeys("nhnbaohan1@gmail.com");
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
            firstNameInput.sendKeys("Bao");
            System.out.println("Entered first name");

            // Enter Last Name
            WebElement lastNameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[contains(@placeholder, 'last name')]")
            ));
            lastNameInput.sendKeys("Han");
            System.out.println("Entered last name");

            // Click Continue after entering names
            WebElement nextContinueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Continue']")
            ));
            nextContinueButton.click();
            System.out.println("Clicked continue after entering names");

            
             // Enter password that's too short (less than 8 characters)
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"password\"]")
            ));
            String shortPassword = "Test@1"; // Only 6 characters
            passwordInput.sendKeys(shortPassword);
            System.out.println("Entered short password: " + shortPassword);

            // Enter confirm password
            WebElement confirmPasswordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"confirmPassword\"]")
            ));
            confirmPasswordInput.sendKeys(shortPassword);
            System.out.println("Entered confirm password");
           
            // Wait a moment for validation to appear
            Thread.sleep(2000);

            

            // Enter password without symbol
            passwordInput.clear();
            String noSymbolPassword = "Test123456"; // No special symbol
            passwordInput.sendKeys(noSymbolPassword);
            System.out.println("🔑 Entered password without symbol: " + noSymbolPassword);

            // Enter confirm password
            confirmPasswordInput.clear();
            confirmPasswordInput.sendKeys(noSymbolPassword);
            System.out.println("🔑 Entered matching confirm password");



            // Enter password without number
            passwordInput.clear();
            String noNumberPassword = "Test@Password"; // No number
            passwordInput.sendKeys(noNumberPassword);
            System.out.println("🔑 Entered password without symbol: " + noNumberPassword);

            // Enter confirm password
            confirmPasswordInput.clear();
            confirmPasswordInput.sendKeys(noNumberPassword);
            System.out.println("🔑 Entered matching confirm password");

















            // Check for password length validation failure
            boolean passwordTooShortValidation = false;
            try {
                // Look for the "Password length 8" validation message with X mark or error state
                WebElement lengthValidation = driver.findElement(
                    By.xpath("//*[contains(text(), 'Password length') or contains(text(), 'length')]")
                );
                String validationText = lengthValidation.getText();
                System.out.println("Found length validation text: " + validationText);
                
                // Check if the validation element has error styling or X mark
                String classAttr = lengthValidation.getAttribute("class");
                System.out.println("Length validation class: " + classAttr);
                
                // Check if there's an X mark or error indicator
                WebElement parentElement = lengthValidation.findElement(By.xpath("./.."));
                String parentClass = parentElement.getAttribute("class");
                System.out.println("Parent element class: " + parentClass);
                
                // Look for error indicators (red color, X mark, etc.)
                boolean hasError = classAttr.contains("error") || classAttr.contains("invalid") || 
                                 classAttr.contains("fail") || parentClass.contains("error") ||
                                 parentClass.contains("invalid") || parentClass.contains("fail");
                
                if (hasError) {
                    passwordTooShortValidation = true;
                    System.out.println("✅ Password length validation detected - password too short");
                } else {
                    System.out.println("❌ Password length validation not showing error state");
                }
                
            } catch (Exception e) {
                System.out.println("Could not find password length validation element: " + e.getMessage());
            }
            
            // Check if Continue button is disabled
            boolean continueButtonDisabled = false;
            try {
                WebElement passwordContinueButton = driver.findElement(By.xpath("//button[text()='Continue']"));
                
                // Check if button is disabled
                String disabledAttr = passwordContinueButton.getAttribute("disabled");
                boolean isEnabled = passwordContinueButton.isEnabled();
                String classAttr = passwordContinueButton.getAttribute("class");
                
                System.out.println("Continue button disabled attribute: " + disabledAttr);
                System.out.println("Continue button isEnabled(): " + isEnabled);
                System.out.println("Continue button class: " + classAttr);
                
                // Button is disabled if disabled attribute exists or isEnabled() returns false
                continueButtonDisabled = (disabledAttr != null) || !isEnabled || 
                                       classAttr.contains("disabled") || classAttr.contains("inactive");
                
                if (continueButtonDisabled) {
                    System.out.println("✅ Continue button is properly disabled for short password");
                } else {
                    System.out.println("❌ Continue button should be disabled but appears enabled");
                }
                
            } catch (Exception e) {
                System.out.println("Could not check continue button state: " + e.getMessage());
            }
            
            // Overall validation result
            if (passwordTooShortValidation || continueButtonDisabled) {
                System.out.println("✅ PASSWORD TOO SHORT VALIDATION WORKING: Short password properly rejected");
                if (passwordTooShortValidation) {
                    System.out.println("  - Length validation shows error state");
                }
                if (continueButtonDisabled) {
                    System.out.println("  - Continue button is disabled");
                }
            } else {
                System.out.println("❌ PASSWORD TOO SHORT VALIDATION FAILED: Short password was accepted");
            }
            
            // Assert that validation is working (either validation message shows error OR button is disabled)
            assertTrue(passwordTooShortValidation || continueButtonDisabled, 
                      "Password too short validation should either show error message or disable continue button");
                      
        } catch (Exception e) {
            System.out.println("❌ Error during password too short test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testPasswordWithoutSymbol() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // Navigate to password step
            navigateToPasswordStep(wait);
            
            System.out.println("🧪 TESTING: Password Without Symbol Validation");
            // Enter password without symbol
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"password\"]")
            ));
            String noSymbolPassword = "Test123456"; // No special symbol
            passwordInput.sendKeys(noSymbolPassword);
            System.out.println("🔑 Entered password without symbol: " + noSymbolPassword);

            // Enter confirm password
            WebElement confirmPasswordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"confirmPassword\"]")
            ));
            confirmPasswordInput.sendKeys(noSymbolPassword);
            System.out.println("🔑 Entered matching confirm password");

            // Wait for validation to appear
            Thread.sleep(2000);

            // Check for symbol validation failure
            boolean symbolValidationFailed = checkValidationIndicator("Symbol", "symbol", "special");
            
            // Check if Continue button is disabled
            boolean continueButtonDisabled = isContinueButtonDisabled();

            // Overall validation result
            if (symbolValidationFailed || continueButtonDisabled) {
                System.out.println("✅ PASSWORD WITHOUT SYMBOL VALIDATION WORKING: Password without symbol properly rejected");
                if (symbolValidationFailed) {
                    System.out.println("  - Symbol validation shows error state");
                }
                if (continueButtonDisabled) {
                    System.out.println("  - Continue button is disabled");
                }
            } else {
                System.out.println("❌ PASSWORD WITHOUT SYMBOL VALIDATION FAILED: Password without symbol was accepted");
            }

            assertTrue(symbolValidationFailed || continueButtonDisabled, 
                      "Password without symbol validation should either show error message or disable continue button");

        } catch (Exception e) {
            System.out.println("❌ Error during password without symbol test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    @Test
    public void testPasswordWithoutNumber() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // Navigate to password step
            navigateToPasswordStep(wait);
            
            System.out.println("🧪 TESTING: Password Without Number Validation");
            // Enter password without number
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"password\"]")
            ));
            String noNumberPassword = "Test@Password"; // No number
            passwordInput.sendKeys(noNumberPassword);
            System.out.println("🔑 Entered password without number: " + noNumberPassword);

            // Enter confirm password
            WebElement confirmPasswordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"confirmPassword\"]")
            ));
            confirmPasswordInput.sendKeys(noNumberPassword);
            System.out.println("🔑 Entered matching confirm password");

            // Wait for validation to appear
            Thread.sleep(2000);

            // Check for number validation failure
            boolean numberValidationFailed = checkValidationIndicator("Number", "number", "digit");
            
            // Check if Continue button is disabled
            boolean continueButtonDisabled = isContinueButtonDisabled();

            // Overall validation result
            if (numberValidationFailed || continueButtonDisabled) {
                System.out.println("✅ PASSWORD WITHOUT NUMBER VALIDATION WORKING: Password without number properly rejected");
                if (numberValidationFailed) {
                    System.out.println("  - Number validation shows error state");
                }
                if (continueButtonDisabled) {
                    System.out.println("  - Continue button is disabled");
                }
            } else {
                System.out.println("❌ PASSWORD WITHOUT NUMBER VALIDATION FAILED: Password without number was accepted");
            }

            assertTrue(numberValidationFailed || continueButtonDisabled, 
                      "Password without number validation should either show error message or disable continue button");

        } catch (Exception e) {
            System.out.println("❌ Error during password without number test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testPasswordWithoutUppercase() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // Navigate to password step
            navigateToPasswordStep(wait);
            
            System.out.println("🧪 TESTING: Password Without Uppercase Validation");
            // Enter password without uppercase
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"password\"]")
            ));
            String noUppercasePassword = "test@123456"; // No uppercase letter
            passwordInput.sendKeys(noUppercasePassword);
            System.out.println("🔑 Entered password without uppercase: " + noUppercasePassword);

            // Enter confirm password
            WebElement confirmPasswordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"confirmPassword\"]")
            ));
            confirmPasswordInput.sendKeys(noUppercasePassword);
            System.out.println("🔑 Entered matching confirm password");

            // Wait for validation to appear
            Thread.sleep(2000);

            // Check for uppercase validation failure
            boolean uppercaseValidationFailed = checkValidationIndicator("uppercase", "upper", "capital");
            
            // Check if Continue button is disabled
            boolean continueButtonDisabled = isContinueButtonDisabled();

            // Overall validation result
            if (uppercaseValidationFailed || continueButtonDisabled) {
                System.out.println("✅ PASSWORD WITHOUT UPPERCASE VALIDATION WORKING: Password without uppercase properly rejected");
                if (uppercaseValidationFailed) {
                    System.out.println("  - Uppercase validation shows error state");
                }
                if (continueButtonDisabled) {
                    System.out.println("  - Continue button is disabled");
                }
            } else {
                System.out.println("❌ PASSWORD WITHOUT UPPERCASE VALIDATION FAILED: Password without uppercase was accepted");
            }

            assertTrue(uppercaseValidationFailed || continueButtonDisabled, 
                      "Password without uppercase validation should either show error message or disable continue button");

        } catch (Exception e) {
            System.out.println("❌ Error during password without uppercase test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testPasswordWithoutLowercase() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // Navigate to password step
            navigateToPasswordStep(wait);
            
            System.out.println("🧪 TESTING: Password Without Lowercase Validation");
            // Enter password without lowercase
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"password\"]")
            ));
            String noLowercasePassword = "TEST@123456"; // No lowercase letter
            passwordInput.sendKeys(noLowercasePassword);
            System.out.println("🔑 Entered password without lowercase: " + noLowercasePassword);

            // Enter confirm password
            WebElement confirmPasswordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"confirmPassword\"]")
            ));
            confirmPasswordInput.sendKeys(noLowercasePassword);
            System.out.println("🔑 Entered matching confirm password");

            // Wait for validation to appear
            Thread.sleep(2000);

            // Check for lowercase validation failure
            boolean lowercaseValidationFailed = checkValidationIndicator("lowercase", "lower", "small");
            
            // Check if Continue button is disabled
            boolean continueButtonDisabled = isContinueButtonDisabled();

            // Overall validation result
            if (lowercaseValidationFailed || continueButtonDisabled) {
                System.out.println("✅ PASSWORD WITHOUT LOWERCASE VALIDATION WORKING: Password without lowercase properly rejected");
                if (lowercaseValidationFailed) {
                    System.out.println("  - Lowercase validation shows error state");
                }
                if (continueButtonDisabled) {
                    System.out.println("  - Continue button is disabled");
                }
            } else {
                System.out.println("❌ PASSWORD WITHOUT LOWERCASE VALIDATION FAILED: Password without lowercase was accepted");
            }

            assertTrue(lowercaseValidationFailed || continueButtonDisabled, 
                      "Password without lowercase validation should either show error message or disable continue button");

        } catch (Exception e) {
            System.out.println("❌ Error during password without lowercase test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testPasswordsDontMatch() throws InterruptedException {
        driver.get("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // Navigate to password step
            navigateToPasswordStep(wait);
            
            System.out.println("🧪 TESTING: Passwords Don't Match Validation");
            // Enter valid password
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"password\"]")
            ));
            String password = "Test@123456";
            passwordInput.sendKeys(password);
            System.out.println("🔑 Entered password: " + password);

            // Enter different confirm password
            WebElement confirmPasswordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@name=\"confirmPassword\"]")
            ));
            String differentPassword = "Different@123";
            confirmPasswordInput.sendKeys(differentPassword);
            System.out.println("🔑 Entered different confirm password: " + differentPassword);

            // Wait for validation to appear
            Thread.sleep(2000);

            // Check for password match validation failure
            boolean matchValidationFailed = checkValidationIndicator("equals", "match", "same");
            
            // Check if Continue button is disabled
            boolean continueButtonDisabled = isContinueButtonDisabled();

            // Overall validation result
            if (matchValidationFailed || continueButtonDisabled) {
                System.out.println("✅ PASSWORD MISMATCH VALIDATION WORKING: Mismatched passwords properly rejected");
                if (matchValidationFailed) {
                    System.out.println("  - Password match validation shows error state");
                }
                if (continueButtonDisabled) {
                    System.out.println("  - Continue button is disabled");
                }
            } else {
                System.out.println("❌ PASSWORD MISMATCH VALIDATION FAILED: Mismatched passwords were accepted");
            }

            assertTrue(matchValidationFailed || continueButtonDisabled, 
                      "Password mismatch validation should either show error message or disable continue button");

        } catch (Exception e) {
            System.out.println("❌ Error during password mismatch test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Helper method to navigate to password step
    private void navigateToPasswordStep(WebDriverWait wait) throws InterruptedException {
        System.out.println("=== NAVIGATING TO PASSWORD STEP ===");
        
        // Click Login button
        WebElement loginButton = driver.findElement(
            By.xpath("//button[contains(text(), 'Login') or contains(text(), 'LOG IN') or contains(text(), 'Sign in')]")
        );
        System.out.println("✅ Found login button with text: " + loginButton.getText());
        loginButton.click();
        
        // Click Register account link
        WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Register') or contains(text(), 'Sign up')]")
        ));
        System.out.println("✅ Found register button with text: " + registerButton.getText());
        registerButton.click();
        
        // Enter email
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='email']")
        ));
        emailInput.clear();
        String testEmail = "test" + System.currentTimeMillis() + "@example.com";
        emailInput.sendKeys(testEmail);
        System.out.println("✅ Entered email: " + testEmail);
        
        // Click Continue for email
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[text()='Continue']")
        ));
        continueButton.click();
        System.out.println("✅ Clicked continue for email");
        
        // Enter First Name
        WebElement firstNameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[contains(@placeholder, 'first name')]")
        ));
        firstNameInput.clear();
        firstNameInput.sendKeys("Test");
        System.out.println("✅ Entered first name: Test");

        // Enter Last Name
        WebElement lastNameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[contains(@placeholder, 'last name')]")
        ));
        lastNameInput.clear();
        lastNameInput.sendKeys("User");
        System.out.println("✅ Entered last name: User");

        // Click Continue after names
        WebElement nextContinueButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[text()='Continue']")
        ));
        nextContinueButton.click();
        System.out.println("✅ Clicked continue after entering names");
        
        Thread.sleep(2000); // Wait for password page to load
        System.out.println("✅ Successfully reached password step");
    }

    // Helper method to check validation indicators
    private boolean checkValidationIndicator(String... keywords) {
        try {
            for (String keyword : keywords) {
                WebElement validationElement = driver.findElement(
                    By.xpath("//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + keyword.toLowerCase() + "')]")
                );
                String validationText = validationElement.getText();
                System.out.println("📋 Found validation text: " + validationText);
                
                // Check if the validation element has error styling
                String classAttr = validationElement.getAttribute("class");
                System.out.println("🔍 Validation class: " + classAttr);
                
                // Check parent element for error indicators
                WebElement parentElement = validationElement.findElement(By.xpath("./.."));
                String parentClass = parentElement.getAttribute("class");
                System.out.println("🔍 Parent element class: " + parentClass);
                
                // Look for error indicators (red color, X mark, error styling)
                boolean hasError = classAttr.contains("error") || classAttr.contains("invalid") || 
                                 classAttr.contains("fail") || classAttr.contains("red") ||
                                 parentClass.contains("error") || parentClass.contains("invalid") || 
                                 parentClass.contains("fail") || parentClass.contains("red");
                
                if (hasError) {
                    System.out.println("✅ " + keyword + " validation detected - showing error state");
                    return true;
                } else {
                    System.out.println("❌ " + keyword + " validation not showing error state");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not find validation element for keywords: " + String.join(", ", keywords) + " - " + e.getMessage());
        }
        return false;
    }

    // Helper method to check if Continue button is disabled
    private boolean isContinueButtonDisabled() {
        try {
            WebElement passwordContinueButton = driver.findElement(By.xpath("//button[text()='Continue']"));
            
            // Check if button is disabled
            String disabledAttr = passwordContinueButton.getAttribute("disabled");
            boolean isEnabled = passwordContinueButton.isEnabled();
            String classAttr = passwordContinueButton.getAttribute("class");
            
            System.out.println("🔘 Continue button disabled attribute: " + disabledAttr);
            System.out.println("🔘 Continue button isEnabled(): " + isEnabled);
            System.out.println("🔘 Continue button class: " + classAttr);
            
            // Button is disabled if disabled attribute exists or isEnabled() returns false
            boolean continueButtonDisabled = (disabledAttr != null) || !isEnabled || 
                                           classAttr.contains("disabled") || classAttr.contains("inactive");
            
            if (continueButtonDisabled) {
                System.out.println("✅ Continue button is properly disabled");
            } else {
                System.out.println("❌ Continue button should be disabled but appears enabled");
            }
            
            return continueButtonDisabled;
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not check continue button state: " + e.getMessage());
            return false;
        }
    }
}
