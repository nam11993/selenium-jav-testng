package com.browserstack;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class LocalSelenideTest {
    public WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(String browser) {
        driver = createDriver(browser != null ? browser : "chrome");
        
        // Configure Selenide
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
        Configuration.headless = false; // Set to true for headless mode
        Configuration.screenshots = true;
        Configuration.savePageSource = false;
        
        // Set the driver for Selenide
        WebDriverRunner.setWebDriver(driver);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            WebDriverRunner.closeWebDriver();
        }
        if (driver != null) {
            driver.quit();
        }
    }

    private WebDriver createDriver(String browserName) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--remote-allow-origins=*");
                // chromeOptions.addArguments("--headless"); // Uncomment for headless mode
                return new ChromeDriver(chromeOptions);
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                // firefoxOptions.addArguments("--headless"); // Uncomment for headless mode
                return new FirefoxDriver(firefoxOptions);
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browserName);
        }
    }
}
