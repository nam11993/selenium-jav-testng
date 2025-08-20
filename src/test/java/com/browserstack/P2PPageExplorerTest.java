package com.browserstack;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;

public class P2PPageExplorerTest extends LocalBrowserTest {
    
    @Test(description = "Explore P2P Create Ad page structure")
    public void exploreP2PCreateAdPageStructure() {
        System.out.println("Exploring P2P Create Ad page structure...");
        
        // Navigate to P2P Create Ad page
        open("https://fcex-fe-718949727112.asia-southeast1.run.app/en/p2p/create-ad");
        
        // Wait for page to load
        sleep(3000);
        
        // Print page title
        String pageTitle = title();
        System.out.println("Page Title: " + pageTitle);
        
        // Print current URL
        String currentUrl = webdriver().object().getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        
        // Try to find any heading elements
        System.out.println("=== Looking for heading elements ===");
        try {
            if ($(By.tagName("h1")).exists()) {
                String h1Text = $(By.tagName("h1")).getText();
                System.out.println("H1 found: " + h1Text);
            } else {
                System.out.println("No H1 element found");
            }
        } catch (Exception e) {
            System.out.println("Error finding H1: " + e.getMessage());
        }
        
        try {
            if ($(By.tagName("h2")).exists()) {
                String h2Text = $(By.tagName("h2")).getText();
                System.out.println("H2 found: " + h2Text);
            } else {
                System.out.println("No H2 element found");
            }
        } catch (Exception e) {
            System.out.println("Error finding H2: " + e.getMessage());
        }
        
        // Look for any text containing "ad", "post", "create"
        System.out.println("=== Looking for key text elements ===");
        try {
            if ($(By.xpath("//*[contains(text(), 'Ad') or contains(text(), 'ad')]")).exists()) {
                String adText = $(By.xpath("//*[contains(text(), 'Ad') or contains(text(), 'ad')]")).getText();
                System.out.println("Text containing 'Ad': " + adText);
            }
        } catch (Exception e) {
            System.out.println("No text containing 'Ad' found");
        }
        
        try {
            if ($(By.xpath("//*[contains(text(), 'Post') or contains(text(), 'post')]")).exists()) {
                String postText = $(By.xpath("//*[contains(text(), 'Post') or contains(text(), 'post')]")).getText();
                System.out.println("Text containing 'Post': " + postText);
            }
        } catch (Exception e) {
            System.out.println("No text containing 'Post' found");
        }
        
        try {
            if ($(By.xpath("//*[contains(text(), 'Create') or contains(text(), 'create')]")).exists()) {
                String createText = $(By.xpath("//*[contains(text(), 'Create') or contains(text(), 'create')]")).getText();
                System.out.println("Text containing 'Create': " + createText);
            }
        } catch (Exception e) {
            System.out.println("No text containing 'Create' found");
        }
        
        // Look for Buy/Sell elements
        System.out.println("=== Looking for Buy/Sell elements ===");
        try {
            if ($(By.xpath("//*[contains(text(), 'Buy') or contains(text(), 'buy')]")).exists()) {
                String buyText = $(By.xpath("//*[contains(text(), 'Buy') or contains(text(), 'buy')]")).getText();
                System.out.println("Text containing 'Buy': " + buyText);
            }
        } catch (Exception e) {
            System.out.println("No text containing 'Buy' found");
        }
        
        try {
            if ($(By.xpath("//*[contains(text(), 'Sell') or contains(text(), 'sell')]")).exists()) {
                String sellText = $(By.xpath("//*[contains(text(), 'Sell') or contains(text(), 'sell')]")).getText();
                System.out.println("Text containing 'Sell': " + sellText);
            }
        } catch (Exception e) {
            System.out.println("No text containing 'Sell' found");
        }
        
        // Look for form elements
        System.out.println("=== Looking for form elements ===");
        try {
            if ($(By.tagName("form")).exists()) {
                System.out.println("Form element found");
            } else {
                System.out.println("No form element found");
            }
        } catch (Exception e) {
            System.out.println("Error finding form: " + e.getMessage());
        }
        
        // Look for input elements
        try {
            if ($(By.tagName("input")).exists()) {
                System.out.println("Input element found");
                String inputType = $(By.tagName("input")).getAttribute("type");
                String inputPlaceholder = $(By.tagName("input")).getAttribute("placeholder");
                System.out.println("Input type: " + inputType + ", placeholder: " + inputPlaceholder);
            } else {
                System.out.println("No input element found");
            }
        } catch (Exception e) {
            System.out.println("Error finding input: " + e.getMessage());
        }
        
        // Look for button elements
        try {
            if ($(By.tagName("button")).exists()) {
                System.out.println("Button element found");
                String buttonText = $(By.tagName("button")).getText();
                System.out.println("Button text: " + buttonText);
            } else {
                System.out.println("No button element found");
            }
        } catch (Exception e) {
            System.out.println("Error finding button: " + e.getMessage());
        }
        
        // Print page source snippet for debugging
        System.out.println("=== Page source snippet (first 500 chars) ===");
        String pageSource = webdriver().object().getPageSource();
        if (pageSource.length() > 500) {
            System.out.println(pageSource.substring(0, 500) + "...");
        } else {
            System.out.println(pageSource);
        }
        
        // Always pass this exploratory test
        Assert.assertTrue(true, "Exploration test completed");
    }
}
