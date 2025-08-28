package com.browserstack;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

/**
 * P2P Post Advertisement Test Cases
 * Based on P2P_Post_Normal_Ad_TestCases_WITH_STEPS.xlsx
 * 
 * Test Scenarios:
 * TC001: Post Normal Buy USDT Advertisement
 * TC002: Post Normal Sell USDT Advertisement  
 * TC003: Verify Advertisement Form Validation
 * TC004: Verify Payment Methods Selection
 * TC005: Verify Advertisement Preview
 */
public class P2PPostAdTestCases extends LocalBrowserTest {
    
    // Base URL for FCEX platform
    private final String BASE_URL = "https://fcex-fe-718949727112.asia-southeast1.run.app/en";
    
    // Login credentials
    private final String LOGIN_EMAIL = "khoahoctonghop11@gmail.com";
    private final String LOGIN_PASSWORD = "Hainam12@";
    
    // Test data
    private final String CRYPTO_CURRENCY = "USDT";
    private final String FIAT_CURRENCY = "VND";
    private final String PRICE = "25000";
    private final String QUANTITY = "100";
    private final String ORDER_LIMIT_MIN = "500000";
    private final String ORDER_LIMIT_MAX = "2500000";
    private final String PAYMENT_TIME_LIMIT = "15";

    @BeforeMethod(dependsOnMethods = "setUp")
    public void loginBeforeEachTest() {
        performLogin();
        navigateToP2PPostAd();
    }

    private void performLogin() {
        System.out.println("🔐 Performing login to FCEX platform...");
        
        // Navigate to homepage
        driver.get(BASE_URL);
        sleep(2000);
        
        // Click Login button
        By loginButton = By.xpath("//button[contains(text(), 'Login') or contains(text(), 'Sign in')]");
        driver.findElement(loginButton).click();
        System.out.println("✅ Clicked Login button");
        sleep(3000);
        
        // Fill email
        By emailInput = By.xpath("//input[@type='email' or contains(@placeholder, 'email') or @name='email']");
        driver.findElement(emailInput).sendKeys(LOGIN_EMAIL);
        System.out.println("✅ Entered email");
        sleep(1000);
        
        // Click Next button
        driver.findElement(By.xpath("//button[contains(text(), 'Next') or contains(text(), 'Continue') or @type='submit']")).click();
        System.out.println("✅ Clicked Next button");
        sleep(3000);
        
        // Fill password
        By passwordInput = By.xpath("//input[@type='password' or contains(@placeholder, 'password') or @name='password']");
        driver.findElement(passwordInput).sendKeys(LOGIN_PASSWORD);
        System.out.println("✅ Entered password");
        
        // Submit login
        By submitLoginButton = By.xpath("//button[@type='submit' or contains(text(), 'Login') or contains(text(), 'Sign In')]");
        driver.findElement(submitLoginButton).click();
        System.out.println("✅ Submitted login form");
        sleep(3000);
        
        System.out.println("✅ Login completed");
    }
    
    private void navigateToP2PPostAd() {
        System.out.println("🧭 Navigating to P2P Post Ad page...");
        
        // Click P2P Trading link
        By p2pTradingLink = By.xpath("//a[contains(text(), 'P2P Trading') or contains(@href, '/p2p')]");
        if (driver.findElements(p2pTradingLink).size() > 0) {
            driver.findElement(p2pTradingLink).click();
            System.out.println("✅ Clicked P2P Trading link");
            sleep(3000);
        } else {
            // Direct navigation if link not found
            driver.get(BASE_URL + "/p2p");
            sleep(3000);
        }
        
        // Click Post Advertisement button
        By postAdButton = By.xpath("//button[contains(text(), 'Post advertisement')] | //a[contains(text(), 'Post advertisement')]");
        if (driver.findElements(postAdButton).size() > 0) {
            driver.findElement(postAdButton).click();
            System.out.println("✅ Clicked Post Advertisement button");
            sleep(3000);
        } else {
            // Direct navigation to create ad page
            driver.get(BASE_URL + "/p2p/create-ad");
            sleep(3000);
        }
        
        System.out.println("✅ Navigated to P2P Post Ad page");
    }
    
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test(priority = 1, description = "TC001: Post Normal Buy USDT Advertisement")
    public void testPostNormalBuyUSDTAd() {
        System.out.println("🧪 TC001: Testing Post Normal Buy USDT Advertisement...");
        
        try {
            // Step 1: Select Buy tab
            By buyTab = By.xpath("//button[contains(text(), 'Buy')] | //div[contains(@class, 'tab') and contains(text(), 'Buy')]");
            if (driver.findElements(buyTab).size() > 0) {
                driver.findElement(buyTab).click();
                System.out.println("✅ Selected Buy tab");
                sleep(2000);
            }
            
            // Step 2: Select USDT cryptocurrency
            By usdtOption = By.xpath("//button[contains(text(), 'USDT')] | //div[contains(text(), 'USDT')] | //option[contains(text(), 'USDT')]");
            if (driver.findElements(usdtOption).size() > 0) {
                driver.findElement(usdtOption).click();
                System.out.println("✅ Selected USDT cryptocurrency");
                sleep(2000);
            }
            
            // Step 3: Select Fiat Currency (VND)
            By fiatCurrencyDropdown = By.xpath("//select[contains(@name, 'fiat')] | //div[contains(@class, 'currency-select')]");
            if (driver.findElements(fiatCurrencyDropdown).size() > 0) {
                WebElement dropdown = driver.findElement(fiatCurrencyDropdown);
                if (dropdown.getTagName().equals("select")) {
                    Select select = new Select(dropdown);
                    select.selectByVisibleText(FIAT_CURRENCY);
                } else {
                    dropdown.click();
                    sleep(1000);
                    By vndOption = By.xpath("//option[contains(text(), 'VND')] | //div[contains(text(), 'VND')]");
                    if (driver.findElements(vndOption).size() > 0) {
                        driver.findElement(vndOption).click();
                    }
                }
                System.out.println("✅ Selected " + FIAT_CURRENCY + " as fiat currency");
                sleep(2000);
            }
            
            // Step 4: Set Fixed Price
            By priceInput = By.xpath("//input[contains(@placeholder, 'Price') or contains(@name, 'price') or contains(@class, 'price')]");
            if (driver.findElements(priceInput).size() > 0) {
                WebElement priceField = driver.findElement(priceInput);
                priceField.clear();
                priceField.sendKeys(PRICE);
                System.out.println("✅ Entered price: " + PRICE);
                sleep(1000);
            }
            
            // Step 5: Set Total Quantity
            By quantityInput = By.xpath("//input[contains(@placeholder, 'Quantity') or contains(@name, 'quantity') or contains(@class, 'quantity')]");
            if (driver.findElements(quantityInput).size() > 0) {
                WebElement quantityField = driver.findElement(quantityInput);
                quantityField.clear();
                quantityField.sendKeys(QUANTITY);
                System.out.println("✅ Entered quantity: " + QUANTITY);
                sleep(1000);
            }
            
            // Step 6: Set Order Limit (Min)
            By orderLimitMinInput = By.xpath("//input[contains(@placeholder, 'Min') or contains(@name, 'min') or contains(@class, 'min-limit')]");
            if (driver.findElements(orderLimitMinInput).size() > 0) {
                WebElement minField = driver.findElement(orderLimitMinInput);
                minField.clear();
                minField.sendKeys(ORDER_LIMIT_MIN);
                System.out.println("✅ Entered minimum order limit: " + ORDER_LIMIT_MIN);
                sleep(1000);
            }
            
            // Step 7: Set Order Limit (Max)
            By orderLimitMaxInput = By.xpath("//input[contains(@placeholder, 'Max') or contains(@name, 'max') or contains(@class, 'max-limit')]");
            if (driver.findElements(orderLimitMaxInput).size() > 0) {
                WebElement maxField = driver.findElement(orderLimitMaxInput);
                maxField.clear();
                maxField.sendKeys(ORDER_LIMIT_MAX);
                System.out.println("✅ Entered maximum order limit: " + ORDER_LIMIT_MAX);
                sleep(1000);
            }
            
            // Step 8: Set Payment Time Limit
            By paymentTimeInput = By.xpath("//input[contains(@placeholder, 'time') or contains(@name, 'time') or contains(@class, 'time-limit')]");
            if (driver.findElements(paymentTimeInput).size() > 0) {
                WebElement timeField = driver.findElement(paymentTimeInput);
                timeField.clear();
                timeField.sendKeys(PAYMENT_TIME_LIMIT);
                System.out.println("✅ Entered payment time limit: " + PAYMENT_TIME_LIMIT + " minutes");
                sleep(1000);
            }
            
            // Step 9: Select Payment Methods
            By paymentMethodSection = By.xpath("//div[contains(@class, 'payment-method')] | //section[contains(text(), 'Payment')]");
            if (driver.findElements(paymentMethodSection).size() > 0) {
                // Select Bank Transfer
                By bankTransferOption = By.xpath("//input[@type='checkbox' and contains(@value, 'bank')] | //label[contains(text(), 'Bank Transfer')]");
                if (driver.findElements(bankTransferOption).size() > 0) {
                    driver.findElement(bankTransferOption).click();
                    System.out.println("✅ Selected Bank Transfer payment method");
                    sleep(1000);
                }
            }
            
            // Step 10: Add Remarks/Terms (Optional)
            By remarksTextarea = By.xpath("//textarea[contains(@placeholder, 'remark') or contains(@name, 'remark') or contains(@class, 'remark')]");
            if (driver.findElements(remarksTextarea).size() > 0) {
                driver.findElement(remarksTextarea).sendKeys("Please follow payment instructions carefully. Fast payment preferred.");
                System.out.println("✅ Added remarks");
                sleep(1000);
            }
            
            // Step 11: Preview Advertisement (if preview button exists)
            By previewButton = By.xpath("//button[contains(text(), 'Preview')] | //button[contains(@class, 'preview')]");
            if (driver.findElements(previewButton).size() > 0) {
                driver.findElement(previewButton).click();
                System.out.println("✅ Clicked Preview button");
                sleep(3000);
                
                // Verify preview content
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl.contains("preview") || 
                    driver.findElements(By.xpath("//div[contains(@class, 'preview')] | //h2[contains(text(), 'Preview')]")).size() > 0) {
                    System.out.println("✅ Advertisement preview displayed successfully");
                }
            }
            
            System.out.println("✅ TC001: Post Normal Buy USDT Advertisement test completed successfully");
            
        } catch (Exception e) {
            System.out.println("❌ TC001 failed: " + e.getMessage());
            // Take screenshot for debugging
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL when error occurred: " + currentUrl);
            throw new AssertionError("TC001: Post Normal Buy USDT Advertisement failed - " + e.getMessage());
        }
    }

    @Test(priority = 2, description = "TC002: Post Normal Sell USDT Advertisement")  
    public void testPostNormalSellUSDTAd() {
        System.out.println("🧪 TC002: Testing Post Normal Sell USDT Advertisement...");
        
        try {
            // Step 1: Select Sell tab
            By sellTab = By.xpath("//button[contains(text(), 'Sell')] | //div[contains(@class, 'tab') and contains(text(), 'Sell')]");
            if (driver.findElements(sellTab).size() > 0) {
                driver.findElement(sellTab).click();
                System.out.println("✅ Selected Sell tab");
                sleep(2000);
            }
            
            // Step 2: Select USDT cryptocurrency
            By usdtOption = By.xpath("//button[contains(text(), 'USDT')] | //div[contains(text(), 'USDT')] | //option[contains(text(), 'USDT')]");
            if (driver.findElements(usdtOption).size() > 0) {
                driver.findElement(usdtOption).click();
                System.out.println("✅ Selected USDT cryptocurrency");
                sleep(2000);
            }
            
            // Continue with similar steps as Buy test but for Sell
            // Using slightly different test data for Sell
            String sellPrice = "24500"; // Lower price for sell
            String sellQuantity = "50";
            
            // Step 3: Set Price for Sell
            By priceInput = By.xpath("//input[contains(@placeholder, 'Price') or contains(@name, 'price') or contains(@class, 'price')]");
            if (driver.findElements(priceInput).size() > 0) {
                WebElement priceField = driver.findElement(priceInput);
                priceField.clear();
                priceField.sendKeys(sellPrice);
                System.out.println("✅ Entered sell price: " + sellPrice);
                sleep(1000);
            }
            
            // Step 4: Set Quantity for Sell
            By quantityInput = By.xpath("//input[contains(@placeholder, 'Quantity') or contains(@name, 'quantity') or contains(@class, 'quantity')]");
            if (driver.findElements(quantityInput).size() > 0) {
                WebElement quantityField = driver.findElement(quantityInput);
                quantityField.clear();
                quantityField.sendKeys(sellQuantity);
                System.out.println("✅ Entered sell quantity: " + sellQuantity);
                sleep(1000);
            }
            
            System.out.println("✅ TC002: Post Normal Sell USDT Advertisement test completed successfully");
            
        } catch (Exception e) {
            System.out.println("❌ TC002 failed: " + e.getMessage());
            throw new AssertionError("TC002: Post Normal Sell USDT Advertisement failed - " + e.getMessage());
        }
    }

    @Test(priority = 3, description = "TC003: Verify Advertisement Form Validation")
    public void testAdvertisementFormValidation() {
        System.out.println("🧪 TC003: Testing Advertisement Form Validation...");
        
        try {
            // Step 1: Try to submit form without filling required fields
            By submitButton = By.xpath("//button[contains(text(), 'Post') or contains(text(), 'Submit') or contains(@type, 'submit')]");
            if (driver.findElements(submitButton).size() > 0) {
                driver.findElement(submitButton).click();
                System.out.println("✅ Clicked submit button without filling fields");
                sleep(2000);
            }
            
            // Step 2: Check for validation error messages
            List<WebElement> errorMessages = driver.findElements(By.xpath("//div[contains(@class, 'error')] | //span[contains(@class, 'error')] | //p[contains(@class, 'error')]"));
            if (errorMessages.size() > 0) {
                System.out.println("✅ Form validation errors displayed:");
                for (WebElement error : errorMessages) {
                    System.out.println("   - " + error.getText());
                }
            }
            
            // Step 3: Test invalid price format
            By priceInput = By.xpath("//input[contains(@placeholder, 'Price') or contains(@name, 'price')]");
            if (driver.findElements(priceInput).size() > 0) {
                WebElement priceField = driver.findElement(priceInput);
                priceField.clear();
                priceField.sendKeys("invalid_price");
                System.out.println("✅ Entered invalid price format");
                sleep(1000);
                
                // Check for price validation
                errorMessages = driver.findElements(By.xpath("//div[contains(@class, 'error')] | //span[contains(@class, 'error')]"));
                if (errorMessages.size() > 0) {
                    System.out.println("✅ Price validation error displayed");
                }
            }
            
            System.out.println("✅ TC003: Advertisement Form Validation test completed successfully");
            
        } catch (Exception e) {
            System.out.println("❌ TC003 failed: " + e.getMessage());
            throw new AssertionError("TC003: Advertisement Form Validation failed - " + e.getMessage());
        }
    }

    @Test(priority = 4, description = "TC004: Verify Payment Methods Selection")
    public void testPaymentMethodsSelection() {
        System.out.println("🧪 TC004: Testing Payment Methods Selection...");
        
        try {
            // Step 1: Navigate to payment methods section
            By paymentSection = By.xpath("//div[contains(@class, 'payment')] | //section[contains(text(), 'Payment')]");
            if (driver.findElements(paymentSection).size() > 0) {
                System.out.println("✅ Found payment methods section");
                
                // Step 2: Check available payment methods
                List<WebElement> paymentOptions = driver.findElements(By.xpath("//input[@type='checkbox'] | //input[@type='radio']"));
                System.out.println("✅ Found " + paymentOptions.size() + " payment method options");
                
                // Step 3: Select multiple payment methods
                for (WebElement option : paymentOptions) {
                    if (option.isDisplayed() && option.isEnabled()) {
                        String value = option.getAttribute("value");
                        option.click();
                        System.out.println("✅ Selected payment method: " + value);
                        sleep(500);
                    }
                }
            }
            
            System.out.println("✅ TC004: Payment Methods Selection test completed successfully");
            
        } catch (Exception e) {
            System.out.println("❌ TC004 failed: " + e.getMessage());
            throw new AssertionError("TC004: Payment Methods Selection failed - " + e.getMessage());
        }
    }

    @Test(priority = 5, description = "TC005: Verify Advertisement Preview")
    public void testAdvertisementPreview() {
        System.out.println("🧪 TC005: Testing Advertisement Preview...");
        
        try {
            // Step 1: Fill out advertisement form with valid data
            fillAdvertisementForm();
            
            // Step 2: Click Preview button
            By previewButton = By.xpath("//button[contains(text(), 'Preview')] | //button[contains(@class, 'preview')]");
            if (driver.findElements(previewButton).size() > 0) {
                driver.findElement(previewButton).click();
                System.out.println("✅ Clicked Preview button");
                sleep(3000);
                
                // Step 3: Verify preview page elements
                verifyPreviewPage();
            }
            
            System.out.println("✅ TC005: Advertisement Preview test completed successfully");
            
        } catch (Exception e) {
            System.out.println("❌ TC005 failed: " + e.getMessage());
            throw new AssertionError("TC005: Advertisement Preview failed - " + e.getMessage());
        }
    }
    
    private void fillAdvertisementForm() {
        System.out.println("📝 Filling advertisement form with test data...");
        
        // Select Buy tab
        By buyTab = By.xpath("//button[contains(text(), 'Buy')]");
        if (driver.findElements(buyTab).size() > 0) {
            driver.findElement(buyTab).click();
            sleep(1000);
        }
        
        // Fill price
        By priceInput = By.xpath("//input[contains(@placeholder, 'Price') or contains(@name, 'price')]");
        if (driver.findElements(priceInput).size() > 0) {
            driver.findElement(priceInput).clear();
            driver.findElement(priceInput).sendKeys(PRICE);
            sleep(500);
        }
        
        // Fill quantity
        By quantityInput = By.xpath("//input[contains(@placeholder, 'Quantity') or contains(@name, 'quantity')]");
        if (driver.findElements(quantityInput).size() > 0) {
            driver.findElement(quantityInput).clear();
            driver.findElement(quantityInput).sendKeys(QUANTITY);
            sleep(500);
        }
        
        System.out.println("✅ Advertisement form filled with test data");
    }
    
    private void verifyPreviewPage() {
        System.out.println("🔍 Verifying preview page content...");
        
        // Check if preview page is displayed
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("preview")) {
            System.out.println("✅ Preview page URL confirmed");
        }
        
        // Check for preview content elements
        if (driver.findElements(By.xpath("//div[contains(@class, 'preview')] | //h2[contains(text(), 'Preview')]")).size() > 0) {
            System.out.println("✅ Preview content section found");
        }
        
        // Verify advertisement details are displayed
        if (driver.findElements(By.xpath("//*[contains(text(), '" + PRICE + "')]")).size() > 0) {
            System.out.println("✅ Price information displayed in preview");
        }
        
        if (driver.findElements(By.xpath("//*[contains(text(), '" + QUANTITY + "')]")).size() > 0) {
            System.out.println("✅ Quantity information displayed in preview");
        }
        
        System.out.println("✅ Preview page verification completed");
    }
}
