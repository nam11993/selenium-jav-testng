package com.browserstack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CryptoDepositPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Page elements using @FindBy annotations
    @FindBy(xpath = "//h1[contains(text(), 'Deposit Crypto')] | //div[contains(text(), 'Deposit Crypto')]")
    private WebElement depositCryptoTitle;

    @FindBy(xpath = "//select[contains(@placeholder, 'Select cryptocurrency')] | //div[contains(text(), 'Select cryptocurrency')]")
    private WebElement cryptoDropdown;

    @FindBy(xpath = "//select[contains(@placeholder, 'Select network')] | //div[contains(text(), 'Select network')]")
    private WebElement networkDropdown;

    @FindBy(xpath = "//button[contains(text(), 'Get Deposit Address')]")
    private WebElement getDepositAddressButton;

    @FindBy(xpath = "//div[contains(@class, 'deposit-address')] | //input[contains(@class, 'address')] | //code | //span[contains(@class, 'address')]")
    private WebElement depositAddressField;

    @FindBy(xpath = "//button[contains(@class, 'copy')] | //i[contains(@class, 'copy')] | //button[contains(text(), 'Copy')]")
    private WebElement copyButton;

    @FindBy(xpath = "//div[contains(text(), 'Copied') or contains(text(), 'Success')] | //div[contains(@class, 'toast')]")
    private WebElement copySuccessMessage;

    @FindBy(xpath = "//div[contains(@class, 'error')] | //div[contains(text(), 'Please select')] | //div[contains(@class, 'alert')]")
    private WebElement errorMessage;

    // Constructor
    public CryptoDepositPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Page methods
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(depositCryptoTitle));
            return depositCryptoTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectCryptocurrency(String crypto) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(cryptoDropdown));
        
        if (cryptoDropdown.getTagName().equals("select")) {
            Select select = new Select(cryptoDropdown);
            select.selectByVisibleText(crypto);
        } else {
            cryptoDropdown.click();
            Thread.sleep(1000);
            
            WebElement cryptoOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), '" + crypto + "')] | //div[contains(text(), '" + crypto + "')] | //option[contains(text(), '" + crypto + "')]")
            ));
            cryptoOption.click();
        }
        Thread.sleep(2000); // Wait for network options to load
    }

    public void selectNetwork(int networkIndex) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(networkDropdown));
        Thread.sleep(2000); // Wait for network options to be available
        
        if (networkDropdown.getTagName().equals("select")) {
            Select select = new Select(networkDropdown);
            List<WebElement> options = select.getOptions();
            if (options.size() > networkIndex) {
                select.selectByIndex(networkIndex);
            }
        } else {
            networkDropdown.click();
            Thread.sleep(1000);
            
            WebElement networkOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//li[contains(@class, 'network-option')] | //div[contains(@class, 'network-item')])[" + (networkIndex + 1) + "]")
            ));
            networkOption.click();
        }
        Thread.sleep(1000);
    }

    public void selectFirstAvailableNetwork() throws InterruptedException {
        selectNetwork(1); // Skip first option which is usually placeholder
    }

    public boolean isGetDepositAddressButtonEnabled() {
        try {
            wait.until(ExpectedConditions.visibilityOf(getDepositAddressButton));
            return getDepositAddressButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickGetDepositAddress() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(getDepositAddressButton));
        getDepositAddressButton.click();
        Thread.sleep(5000); // Wait for address generation
    }

    public String getDepositAddress() {
        try {
            wait.until(ExpectedConditions.visibilityOf(depositAddressField));
            return depositAddressField.getText();
        } catch (Exception e) {
            // Try to get value from input field if text is empty
            try {
                return depositAddressField.getAttribute("value");
            } catch (Exception ex) {
                return "";
            }
        }
    }

    public boolean isDepositAddressDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(depositAddressField));
            return depositAddressField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCopyButton() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(copyButton));
        copyButton.click();
        Thread.sleep(1000);
    }

    public boolean isCopyButtonDisplayed() {
        try {
            return copyButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCopySuccessMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(copySuccessMessage));
            return copySuccessMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return errorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void refreshPage() {
        driver.navigate().refresh();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Utility method to validate address format
    public boolean isValidDepositAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        
        // Basic validation - address should be longer than 10 characters
        // More specific validation can be added based on crypto type
        return address.length() > 10 && !address.contains("null") && !address.contains("undefined");
    }

    // Method to get all available cryptocurrencies
    public List<WebElement> getAvailableCryptocurrencies() {
        if (cryptoDropdown.getTagName().equals("select")) {
            Select select = new Select(cryptoDropdown);
            return select.getOptions();
        } else {
            cryptoDropdown.click();
            return driver.findElements(By.xpath("//li[contains(@class, 'crypto-option')] | //div[contains(@class, 'crypto-item')]"));
        }
    }

    // Method to get all available networks for selected crypto
    public List<WebElement> getAvailableNetworks() {
        if (networkDropdown.getTagName().equals("select")) {
            Select select = new Select(networkDropdown);
            return select.getOptions();
        } else {
            networkDropdown.click();
            return driver.findElements(By.xpath("//li[contains(@class, 'network-option')] | //div[contains(@class, 'network-item')]"));
        }
    }
}
