package com.browserstack;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class FCEXSecurityPageExplorer extends LocalBrowserTest {

    private final String BASE_URL = "https://fcex-fe-718949727112.asia-southeast1.run.app/en";
    private final String SECURITY_URL = BASE_URL + "/account/security";

    @Test
    public void exploreSecurityPageStructure() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        
        try {
            System.out.println("🔍 FCEX Security Page Structure Explorer");
            System.out.println("======================================");
            
            // Step 1: Login
            performLogin(wait);
            
            // Step 2: Navigate to security page
            System.out.println("📍 Navigating to: " + SECURITY_URL);
            driver.get(SECURITY_URL);
            Thread.sleep(5000);
            
            System.out.println("📍 Current URL: " + driver.getCurrentUrl());
            System.out.println("📄 Page Title: " + driver.getTitle());
            
            // Step 3: Explore page elements
            explorePageElements();
            
            // Step 4: Look for 2FA specific elements
            explore2FAElements();
            
            // Step 5: Check for security settings
            exploreSecuritySettings();
            
            // Step 6: Look for forms and inputs
            exploreFormsAndInputs();
            
            // Step 7: Check for navigation elements
            exploreNavigation();
            
        } catch (Exception e) {
            System.out.println("❌ Error exploring security page: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testDirectSecurityPageAccess() throws InterruptedException, Exception {
        try {
            System.out.println("🔍 Testing Direct Security Page Access");
            System.out.println("====================================");
            
            // Test direct access without login
            System.out.println("📍 Testing direct access to security page...");
            driver.get(SECURITY_URL);
            Thread.sleep(5000);
            
            String currentUrl = driver.getCurrentUrl();
            System.out.println("📍 URL after direct access: " + currentUrl);
            
            if (currentUrl.contains("/login") || currentUrl.contains("/auth")) {
                System.out.println("✅ Security page properly redirects to login when not authenticated");
            } else if (currentUrl.contains("/account/security")) {
                System.out.println("⚠️ Direct access to security page allowed - check authentication");
            } else {
                System.out.println("🔄 Redirected to: " + currentUrl);
            }
            
            // Now login and try again
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            performLogin(wait);
            
            driver.get(SECURITY_URL);
            Thread.sleep(3000);
            
            System.out.println("📍 URL after login: " + driver.getCurrentUrl());
            
        } catch (Exception e) {
            System.out.println("❌ Error testing direct access: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Helper Methods

    private void performLogin(WebDriverWait wait) throws InterruptedException {
        try {
            System.out.println("🔐 Performing login for security page access...");
            
            // Check if already logged in
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("/account") || 
                driver.getPageSource().toLowerCase().contains("logout")) {
                System.out.println("ℹ️ Already logged in");
                return;
            }
            
            // Navigate to base URL
            driver.get(BASE_URL);
            Thread.sleep(3000);
            
            // Find and click login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'Sign in') or contains(text(), 'LOG IN')] | " +
                        "//a[contains(text(), 'Login') or contains(text(), 'Sign in')]")
            ));
            System.out.println("✅ Found login button: " + loginButton.getText());
            loginButton.click();
            Thread.sleep(3000);
            
            // Use Google login
            WebElement googleLoginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class=\"h-12 w-12 flex items-center justify-center\"]")
            ));
            googleLoginButton.click();
            Thread.sleep(5000);
            
            // Handle Google authentication
            handleGoogleLogin(wait);
            
            System.out.println("✅ Login completed");
            
        } catch (Exception e) {
            System.out.println("⚠️ Login error: " + e.getMessage());
        }
    }

    private void handleGoogleLogin(WebDriverWait wait) throws InterruptedException {
        try {
            String currentUrl = driver.getCurrentUrl();
            System.out.println("🔄 Current URL after Google login click: " + currentUrl);
            
            if (currentUrl.contains("google.com") || currentUrl.contains("accounts.google")) {
                System.out.println("🔄 Handling Google authentication...");
                
                // Enter email
                WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='email']")
                ));
                emailInput.sendKeys("hainam11993@gmail.com");
                
                WebElement nextButton = driver.findElement(By.xpath("//span[text()='Next']"));
                nextButton.click();
                Thread.sleep(3000);
                
                // Enter password
                WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='password']")
                ));
                passwordInput.sendKeys("Hainam12@");
                
                nextButton = driver.findElement(By.xpath("//span[text()='Next']"));
                nextButton.click();
                Thread.sleep(5000);
                
                System.out.println("✅ Google authentication completed");
            } else {
                System.out.println("ℹ️ No Google authentication required or already completed");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Google login error: " + e.getMessage());
        }
    }

    private void explorePageElements() {
        try {
            System.out.println("\n🔍 EXPLORING PAGE ELEMENTS");
            System.out.println("==========================");
            
            // Get all headings
            List<WebElement> headings = driver.findElements(By.xpath("//h1 | //h2 | //h3 | //h4 | //h5 | //h6"));
            System.out.println("📋 Headings found: " + headings.size());
            for (WebElement heading : headings) {
                String text = heading.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println("  - " + heading.getTagName().toUpperCase() + ": " + text);
                }
            }
            
            // Get all buttons
            List<WebElement> buttons = driver.findElements(By.xpath("//button"));
            System.out.println("🔘 Buttons found: " + buttons.size());
            for (WebElement button : buttons) {
                String text = button.getText().trim();
                String className = button.getAttribute("class");
                if (!text.isEmpty()) {
                    System.out.println("  - Button: '" + text + "' | Class: '" + className + "' | Enabled: " + button.isEnabled());
                }
            }
            
            // Get all links
            List<WebElement> links = driver.findElements(By.xpath("//a"));
            System.out.println("🔗 Links found: " + links.size());
            for (WebElement link : links) {
                String text = link.getText().trim();
                String href = link.getAttribute("href");
                if (!text.isEmpty() && href != null) {
                    System.out.println("  - Link: '" + text + "' | Href: '" + href + "'");
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error exploring page elements: " + e.getMessage());
        }
    }

    private void explore2FAElements() {
        try {
            System.out.println("\n🔐 EXPLORING 2FA ELEMENTS");
            System.out.println("=========================");
            
            // Search for 2FA related text
            String[] twoFAKeywords = {
                "Two-Factor", "2FA", "Authenticator", "Google Authenticator", 
                "TOTP", "Time-based", "Verification", "Security Code", 
                "QR Code", "Backup Code", "Recovery Code"
            };
            
            for (String keyword : twoFAKeywords) {
                List<WebElement> elements = driver.findElements(By.xpath(
                    "//*[contains(text(), '" + keyword + "')]"
                ));
                if (elements.size() > 0) {
                    System.out.println("✅ Found '" + keyword + "' in " + elements.size() + " elements:");
                    for (WebElement element : elements) {
                        String text = element.getText().trim();
                        if (text.length() > 0 && text.length() < 200) {
                            System.out.println("    - " + element.getTagName() + ": " + text);
                        }
                    }
                }
            }
            
            // Look for specific 2FA UI patterns
            List<WebElement> switches = driver.findElements(By.xpath(
                "//*[contains(@class, 'switch') or contains(@class, 'toggle')] | " +
                "//input[@type='checkbox']"
            ));
            if (switches.size() > 0) {
                System.out.println("🔘 Found " + switches.size() + " switches/toggles (potential 2FA enable/disable)");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error exploring 2FA elements: " + e.getMessage());
        }
    }

    private void exploreSecuritySettings() {
        try {
            System.out.println("\n🛡️ EXPLORING SECURITY SETTINGS");
            System.out.println("==============================");
            
            // Look for security-related sections
            String[] securityKeywords = {
                "Security", "Password", "Privacy", "Account", "Settings",
                "Authentication", "Login", "Access", "Protection"
            };
            
            for (String keyword : securityKeywords) {
                List<WebElement> elements = driver.findElements(By.xpath(
                    "//*[contains(text(), '" + keyword + "') and (self::h1 or self::h2 or self::h3 or self::h4 or self::h5 or self::h6 or self::div or self::span)]"
                ));
                if (elements.size() > 0) {
                    System.out.println("🔍 '" + keyword + "' sections found: " + elements.size());
                    for (WebElement element : elements) {
                        String text = element.getText().trim();
                        if (text.length() > 0 && text.length() < 100) {
                            System.out.println("    - " + text);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error exploring security settings: " + e.getMessage());
        }
    }

    private void exploreFormsAndInputs() {
        try {
            System.out.println("\n📝 EXPLORING FORMS AND INPUTS");
            System.out.println("=============================");
            
            // Find all forms
            List<WebElement> forms = driver.findElements(By.xpath("//form"));
            System.out.println("📋 Forms found: " + forms.size());
            
            // Find all inputs
            List<WebElement> inputs = driver.findElements(By.xpath("//input"));
            System.out.println("📝 Input fields found: " + inputs.size());
            for (WebElement input : inputs) {
                String type = input.getAttribute("type");
                String name = input.getAttribute("name");
                String placeholder = input.getAttribute("placeholder");
                String id = input.getAttribute("id");
                
                System.out.println("  - Input: type='" + type + "' name='" + name + "' placeholder='" + placeholder + "' id='" + id + "'");
            }
            
            // Find all textareas
            List<WebElement> textareas = driver.findElements(By.xpath("//textarea"));
            System.out.println("📝 Textareas found: " + textareas.size());
            
            // Find all selects
            List<WebElement> selects = driver.findElements(By.xpath("//select"));
            System.out.println("📝 Select dropdowns found: " + selects.size());
            
        } catch (Exception e) {
            System.out.println("❌ Error exploring forms and inputs: " + e.getMessage());
        }
    }

    private void exploreNavigation() {
        try {
            System.out.println("\n🧭 EXPLORING NAVIGATION");
            System.out.println("======================");
            
            // Look for navigation menus
            List<WebElement> navElements = driver.findElements(By.xpath(
                "//nav | //*[contains(@class, 'nav')] | //*[contains(@class, 'menu')]"
            ));
            System.out.println("🧭 Navigation elements found: " + navElements.size());
            
            // Look for breadcrumbs
            List<WebElement> breadcrumbs = driver.findElements(By.xpath(
                "//*[contains(@class, 'breadcrumb')] | //*[contains(@class, 'crumb')]"
            ));
            System.out.println("🍞 Breadcrumb elements found: " + breadcrumbs.size());
            
            // Look for sidebar or menu items
            List<WebElement> menuItems = driver.findElements(By.xpath(
                "//*[contains(@class, 'sidebar')] | //*[contains(@class, 'menu-item')] | " +
                "//*[contains(@class, 'nav-item')]"
            ));
            System.out.println("📋 Menu items found: " + menuItems.size());
            
            // Check current page indicators
            List<WebElement> activeItems = driver.findElements(By.xpath(
                "//*[contains(@class, 'active')] | //*[contains(@class, 'current')] | " +
                "//*[contains(@class, 'selected')]"
            ));
            System.out.println("✅ Active/current page indicators: " + activeItems.size());
            for (WebElement item : activeItems) {
                String text = item.getText().trim();
                if (!text.isEmpty() && text.length() < 50) {
                    System.out.println("    - Active: " + text);
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error exploring navigation: " + e.getMessage());
        }
    }

    @Test
    public void searchFor2FASpecificElements() throws InterruptedException, Exception {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            System.out.println("🔍 SEARCHING FOR 2FA SPECIFIC ELEMENTS");
            System.out.println("======================================");
            
            // Login and navigate
            performLogin(wait);
            driver.get(SECURITY_URL);
            Thread.sleep(5000);
            
            // Search for common 2FA UI patterns
            searchForQRCodes();
            searchForTOTPElements();
            searchForBackupCodes();
            searchForSecurityToggles();
            searchForVerificationInputs();
            
        } catch (Exception e) {
            System.out.println("❌ Error searching for 2FA elements: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void searchForQRCodes() {
        System.out.println("\n📱 SEARCHING FOR QR CODE ELEMENTS");
        System.out.println("=================================");
        
        // Multiple QR code detection strategies
        String[] qrSelectors = {
            "//img[contains(@src, 'qr')]",
            "//img[contains(@alt, 'QR')]", 
            "//canvas[contains(@class, 'qr')]",
            "//*[contains(@id, 'qr')]",
            "//svg[contains(@class, 'qr')]",
            "//*[contains(text(), 'QR code')]",
            "//*[contains(text(), 'QR Code')]",
            "//*[contains(@data-testid, 'qr')]"
        };
        
        for (String selector : qrSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(selector));
                if (elements.size() > 0) {
                    System.out.println("✅ QR elements found with selector '" + selector + "': " + elements.size());
                    for (WebElement element : elements) {
                        System.out.println("    - " + element.getTagName() + " | Class: " + element.getAttribute("class"));
                    }
                }
            } catch (Exception e) {
                // Continue with next selector
            }
        }
    }

    private void searchForTOTPElements() {
        System.out.println("\n⏰ SEARCHING FOR TOTP ELEMENTS");
        System.out.println("=============================");
        
        String[] totpKeywords = {
            "TOTP", "Time-based", "Authenticator", "6-digit", "verification code",
            "authentication code", "security code", "one-time password"
        };
        
        for (String keyword : totpKeywords) {
            List<WebElement> elements = driver.findElements(By.xpath(
                "//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + keyword.toLowerCase() + "')]"
            ));
            if (elements.size() > 0) {
                System.out.println("✅ '" + keyword + "' found in " + elements.size() + " elements");
            }
        }
    }

    private void searchForBackupCodes() {
        System.out.println("\n🔑 SEARCHING FOR BACKUP CODE ELEMENTS");
        System.out.println("====================================");
        
        String[] backupSelectors = {
            "//*[contains(text(), 'backup') and contains(text(), 'code')]",
            "//*[contains(text(), 'recovery code')]",
            "//*[contains(text(), 'emergency code')]",
            "//code",
            "//*[contains(@class, 'backup-code')]",
            "//*[contains(@class, 'recovery-code')]"
        };
        
        for (String selector : backupSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(selector));
                if (elements.size() > 0) {
                    System.out.println("✅ Backup code elements found: " + elements.size());
                }
            } catch (Exception e) {
                // Continue
            }
        }
    }

    private void searchForSecurityToggles() {
        System.out.println("\n🔘 SEARCHING FOR SECURITY TOGGLES");
        System.out.println("================================");
        
        String[] toggleSelectors = {
            "//input[@type='checkbox']",
            "//*[contains(@class, 'toggle')]",
            "//*[contains(@class, 'switch')]",
            "//*[contains(@role, 'switch')]",
            "//button[contains(@aria-pressed, 'true') or contains(@aria-pressed, 'false')]"
        };
        
        for (String selector : toggleSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(selector));
                if (elements.size() > 0) {
                    System.out.println("✅ Toggle elements found with '" + selector + "': " + elements.size());
                    for (WebElement element : elements) {
                        String label = element.getAttribute("aria-label");
                        String name = element.getAttribute("name");
                        System.out.println("    - Label: '" + label + "' | Name: '" + name + "'");
                    }
                }
            } catch (Exception e) {
                // Continue
            }
        }
    }

    private void searchForVerificationInputs() {
        System.out.println("\n🔢 SEARCHING FOR VERIFICATION INPUTS");
        System.out.println("===================================");
        
        String[] inputSelectors = {
            "//input[contains(@placeholder, 'code')]",
            "//input[contains(@placeholder, 'verification')]",
            "//input[contains(@name, '2fa')]",
            "//input[contains(@name, 'totp')]",
            "//input[contains(@name, 'verification')]",
            "//input[@maxlength='6']",
            "//input[@pattern='[0-9]{6}']"
        };
        
        for (String selector : inputSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(selector));
                if (elements.size() > 0) {
                    System.out.println("✅ Verification inputs found: " + elements.size());
                    for (WebElement element : elements) {
                        String placeholder = element.getAttribute("placeholder");
                        String type = element.getAttribute("type");
                        System.out.println("    - Type: '" + type + "' | Placeholder: '" + placeholder + "'");
                    }
                }
            } catch (Exception e) {
                // Continue
            }
        }
    }
}
