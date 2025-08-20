package com.browserstack;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.Configuration;

import java.time.Duration;

public class LocalBrowserTest {
    public WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        driver = createDriver(browser != null ? browser : "chrome");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        
        // Configure Selenide to use our WebDriver instance
        WebDriverRunner.setWebDriver(driver);
        Configuration.timeout = 4000; // 4 seconds timeout
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        // Clear Selenide WebDriver reference
        WebDriverRunner.closeWebDriver();
    }

    private WebDriver createDriver(String browserName) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                try {
                    WebDriverManager.chromedriver().clearDriverCache().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                    chromeOptions.addArguments("--disable-extensions");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    chromeOptions.addArguments("--disable-web-security");
                    chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                    // chromeOptions.addArguments("--headless"); // Uncomment for headless mode
                    System.out.println("Creating Chrome driver...");
                    return new ChromeDriver(chromeOptions);
                } catch (Exception e) {
                    System.err.println("Error creating Chrome driver: " + e.getMessage());
                    throw e;
                }
                
            case "firefox":
                try {
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    // firefoxOptions.addArguments("--headless"); // Uncomment for headless mode
                    System.out.println("Creating Firefox driver...");
                    return new FirefoxDriver(firefoxOptions);
                } catch (Exception e) {
                    System.err.println("Error creating Firefox driver: " + e.getMessage());
                    throw e;
                }
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browserName);
        }
    }
}
