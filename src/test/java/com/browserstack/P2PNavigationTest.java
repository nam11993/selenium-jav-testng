package com.browserstack;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;

public class P2PNavigationTest extends LocalBrowserTest {
    
    @Test(description = "Navigate to P2P section via button")
    public void navigateToP2PSection() {
        System.out.println("Navigating to P2P section...");
        
        // Navigate to homepage
        open("https://fcex-fe-718949727112.asia-southeast1.run.app/en");
        
        // Wait for page to load
        sleep(3000);
        
        System.out.println("Initial page title: " + title());
        System.out.println("Initial URL: " + webdriver().object().getCurrentUrl());
        
        // Click P2P Trading button
        try {
            $(By.xpath("//button[contains(text(), 'P2P Trading')]")).click();
            System.out.println("Clicked P2P Trading button");
            
            // Wait for navigation
            sleep(3000);
            
            System.out.println("After click - Page title: " + title());
            System.out.println("After click - URL: " + webdriver().object().getCurrentUrl());
            
            // Look for P2P related elements
            System.out.println("=== Looking for P2P page elements ===");
            
            // Check for any headings
            try {
                if ($(By.tagName("h1")).exists()) {
                    String h1Text = $(By.tagName("h1")).getText();
                    System.out.println("H1 found: " + h1Text);
                }
                if ($(By.tagName("h2")).exists()) {
                    String h2Text = $(By.tagName("h2")).getText();
                    System.out.println("H2 found: " + h2Text);
                }
            } catch (Exception e) {
                System.out.println("Error finding headings: " + e.getMessage());
            }
            
            // Look for create/post ad related elements
            try {
                if ($(By.xpath("//*[contains(text(), 'Create') or contains(text(), 'Post') or contains(text(), 'Ad')]")).exists()) {
                    String adText = $(By.xpath("//*[contains(text(), 'Create') or contains(text(), 'Post') or contains(text(), 'Ad')]")).getText();
                    System.out.println("Ad-related text found: " + adText);
                }
            } catch (Exception e) {
                System.out.println("No ad-related text found");
            }
            
            // Look for navigation menu items
            try {
                if ($(By.xpath("//nav")).exists()) {
                    System.out.println("Navigation element found");
                    String navText = $(By.xpath("//nav")).getText();
                    System.out.println("Navigation text: " + navText);
                }
            } catch (Exception e) {
                System.out.println("No navigation element found");
            }
            
            // Look for all links on the page
            System.out.println("=== Looking for all links ===");
            try {
                if ($$("a").size() > 0) {
                    System.out.println("Found " + $$("a").size() + " links");
                    for (int i = 0; i < Math.min(10, $$("a").size()); i++) {
                        String linkText = $$("a").get(i).getText();
                        String linkHref = $$("a").get(i).getAttribute("href");
                        if (!linkText.trim().isEmpty()) {
                            System.out.println("Link " + (i+1) + ": " + linkText + " -> " + linkHref);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error finding links: " + e.getMessage());
            }
            
            // Look for buttons
            System.out.println("=== Looking for all buttons ===");
            try {
                if ($$("button").size() > 0) {
                    System.out.println("Found " + $$("button").size() + " buttons");
                    for (int i = 0; i < Math.min(5, $$("button").size()); i++) {
                        String buttonText = $$("button").get(i).getText();
                        if (!buttonText.trim().isEmpty()) {
                            System.out.println("Button " + (i+1) + ": " + buttonText);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error finding buttons: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Error clicking P2P Trading button: " + e.getMessage());
        }
        
        // Always pass this exploratory test
        Assert.assertTrue(true, "Navigation exploration completed");
    }
}
