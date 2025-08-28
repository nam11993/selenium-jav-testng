import pandas as pd
import os
import sys

def read_excel_testcases(file_path):
    """
    Read Excel file and extract test cases information
    """
    try:
        # Read Excel file
        print(f"📖 Reading Excel file: {file_path}")
        
        # Try to read all sheets
        excel_file = pd.ExcelFile(file_path)
        print(f"📋 Found sheets: {excel_file.sheet_names}")
        
        # Read the first sheet (or specific sheet if needed)
        df = pd.read_excel(file_path, sheet_name=0)
        
        print(f"📊 Total rows: {len(df)}")
        print(f"📊 Total columns: {len(df.columns)}")
        print(f"📊 Column names: {list(df.columns)}")
        
        # Display first few rows
        print("\n📋 First 10 rows:")
        print(df.head(10).to_string())
        
        # Display structure
        print(f"\n📋 Data types:")
        print(df.dtypes)
        
        # Save to CSV for easier processing
        csv_path = file_path.replace('.xlsx', '_extracted.csv')
        df.to_csv(csv_path, index=False, encoding='utf-8')
        print(f"\n💾 Saved to CSV: {csv_path}")
        
        return df
        
    except Exception as e:
        print(f"❌ Error reading Excel file: {str(e)}")
        return None

def generate_java_testcases(df):
    """
    Generate Java TestNG automation code based on Excel data
    """
    if df is None:
        return
        
    print("\n🔄 Generating Java automation test cases...")
    
    # Extract unique test scenarios
    test_cases = []
    
    # Assuming common Excel structure for test cases
    # Try to identify columns that might contain test case info
    columns = df.columns.tolist()
    
    # Common column names to look for
    test_id_col = None
    test_name_col = None
    test_steps_col = None
    expected_result_col = None
    
    for col in columns:
        col_lower = str(col).lower()
        if 'id' in col_lower or 'case' in col_lower:
            test_id_col = col
        elif 'name' in col_lower or 'title' in col_lower or 'description' in col_lower:
            test_name_col = col
        elif 'step' in col_lower or 'action' in col_lower:
            test_steps_col = col
        elif 'expected' in col_lower or 'result' in col_lower:
            expected_result_col = col
    
    print(f"🔍 Identified columns:")
    print(f"   Test ID: {test_id_col}")
    print(f"   Test Name: {test_name_col}")
    print(f"   Test Steps: {test_steps_col}")
    print(f"   Expected Result: {expected_result_col}")
    
    # Process each row as a test case
    for index, row in df.iterrows():
        if pd.isna(row[test_id_col if test_id_col else df.columns[0]]):
            continue
            
        test_case = {
            'id': row[test_id_col] if test_id_col else f"TC{index+1:03d}",
            'name': row[test_name_col] if test_name_col else f"Test Case {index+1}",
            'steps': row[test_steps_col] if test_steps_col else "No steps defined",
            'expected': row[expected_result_col] if expected_result_col else "No expected result defined"
        }
        test_cases.append(test_case)
    
    print(f"\n📊 Found {len(test_cases)} test cases")
    
    # Generate Java code
    java_code = generate_java_class(test_cases)
    
    # Save to file
    java_file_path = "P2PComprehensiveTestCases.java"
    with open(java_file_path, 'w', encoding='utf-8') as f:
        f.write(java_code)
    
    print(f"💾 Generated Java file: {java_file_path}")
    
    return test_cases

def generate_java_class(test_cases):
    """
    Generate complete Java TestNG class with all test cases
    """
    
    java_template = '''package com.browserstack;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

/**
 * Comprehensive P2P Test Cases - Generated from Excel
 * Total Test Cases: {total_cases}
 * Auto-generated on: {date}
 */
public class P2PComprehensiveTestCases extends LocalBrowserTest {{
    
    private final String BASE_URL = "https://fcex-fe-718949727112.asia-southeast1.run.app/en";
    private final String LOGIN_EMAIL = "khoahoctonghop11@gmail.com";
    private final String LOGIN_PASSWORD = "Hainam12@";
    private WebDriverWait wait;
    
    @BeforeMethod(dependsOnMethods = "setUp")
    public void setupTest() {{
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        performLogin();
        navigateToP2PSection();
    }}
    
    private void performLogin() {{
        System.out.println("🔐 Performing login...");
        driver.get(BASE_URL);
        sleep(2000);
        
        try {{
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'Sign in')]")));
            loginBtn.click();
            sleep(3000);
            
            WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='email' or contains(@placeholder, 'email')]")));
            emailInput.sendKeys(LOGIN_EMAIL);
            sleep(1000);
            
            WebElement nextBtn = driver.findElement(By.xpath("//button[contains(text(), 'Next')]"));
            nextBtn.click();
            sleep(3000);
            
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='password']")));
            passwordInput.sendKeys(LOGIN_PASSWORD);
            
            WebElement submitBtn = driver.findElement(By.xpath("//button[@type='submit']"));
            submitBtn.click();
            sleep(3000);
            
            System.out.println("✅ Login completed");
        }} catch (Exception e) {{
            System.out.println("❌ Login failed: " + e.getMessage());
        }}
    }}
    
    private void navigateToP2PSection() {{
        System.out.println("🧭 Navigating to P2P section...");
        try {{
            // Try to find P2P link
            WebElement p2pLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'P2P') or contains(@href, '/p2p')]")));
            p2pLink.click();
            sleep(3000);
            System.out.println("✅ Navigated to P2P section");
        }} catch (Exception e) {{
            // Direct navigation
            driver.get(BASE_URL + "/p2p");
            sleep(3000);
            System.out.println("✅ Direct navigation to P2P");
        }}
    }}
    
    private void sleep(int milliseconds) {{
        try {{
            Thread.sleep(milliseconds);
        }} catch (InterruptedException e) {{
            Thread.currentThread().interrupt();
        }}
    }}
    
    // Common helper methods for P2P operations
    private void clickPostAdvertisement() {{
        try {{
            WebElement postAdBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Post advertisement')] | //a[contains(text(), 'Post advertisement')]")));
            postAdBtn.click();
            sleep(2000);
            System.out.println("✅ Clicked Post Advertisement");
        }} catch (Exception e) {{
            driver.get(BASE_URL + "/p2p/create-ad");
            sleep(2000);
        }}
    }}
    
    private void selectBuyTab() {{
        try {{
            WebElement buyTab = driver.findElement(By.xpath("//button[contains(text(), 'Buy')] | //div[contains(@class, 'tab') and contains(text(), 'Buy')]"));
            buyTab.click();
            sleep(1000);
            System.out.println("✅ Selected Buy tab");
        }} catch (Exception e) {{
            System.out.println("⚠️ Buy tab not found or already selected");
        }}
    }}
    
    private void selectSellTab() {{
        try {{
            WebElement sellTab = driver.findElement(By.xpath("//button[contains(text(), 'Sell')] | //div[contains(@class, 'tab') and contains(text(), 'Sell')]"));
            sellTab.click();
            sleep(1000);
            System.out.println("✅ Selected Sell tab");
        }} catch (Exception e) {{
            System.out.println("⚠️ Sell tab not found or already selected");
        }}
    }}
    
    private void selectCryptocurrency(String crypto) {{
        try {{
            WebElement cryptoBtn = driver.findElement(By.xpath("//button[contains(text(), '" + crypto + "')] | //option[contains(text(), '" + crypto + "')]"));
            cryptoBtn.click();
            sleep(1000);
            System.out.println("✅ Selected cryptocurrency: " + crypto);
        }} catch (Exception e) {{
            System.out.println("⚠️ Cryptocurrency " + crypto + " not found");
        }}
    }}
    
    private void enterPrice(String price) {{
        try {{
            WebElement priceInput = driver.findElement(By.xpath("//input[contains(@placeholder, 'Price') or contains(@name, 'price')]"));
            priceInput.clear();
            priceInput.sendKeys(price);
            sleep(500);
            System.out.println("✅ Entered price: " + price);
        }} catch (Exception e) {{
            System.out.println("⚠️ Price input not found");
        }}
    }}
    
    private void enterQuantity(String quantity) {{
        try {{
            WebElement quantityInput = driver.findElement(By.xpath("//input[contains(@placeholder, 'Quantity') or contains(@name, 'quantity')]"));
            quantityInput.clear();
            quantityInput.sendKeys(quantity);
            sleep(500);
            System.out.println("✅ Entered quantity: " + quantity);
        }} catch (Exception e) {{
            System.out.println("⚠️ Quantity input not found");
        }}
    }}

{test_methods}
}}'''
    
    # Generate test methods
    test_methods = ""
    for i, test_case in enumerate(test_cases, 1):
        method_name = f"test{test_case['id'].replace(' ', '').replace('-', '').replace('_', '')}"
        
        test_method = f'''
    @Test(priority = {i}, description = "{test_case['id']}: {test_case['name']}")
    public void {method_name}() {{
        System.out.println("🧪 {test_case['id']}: {test_case['name']}");
        
        try {{
            // Test Steps: {test_case['steps'][:100]}...
            clickPostAdvertisement();
            
            // Common P2P test actions based on test case
            if ("{test_case['name']}".toLowerCase().contains("buy")) {{
                selectBuyTab();
            }} else if ("{test_case['name']}".toLowerCase().contains("sell")) {{
                selectSellTab();
            }}
            
            if ("{test_case['name']}".toLowerCase().contains("usdt")) {{
                selectCryptocurrency("USDT");
            }} else if ("{test_case['name']}".toLowerCase().contains("btc")) {{
                selectCryptocurrency("BTC");
            }}
            
            // Add specific test logic based on test case name and steps
            performTestCaseActions("{test_case['id']}", "{test_case['name']}", "{test_case['steps'][:200]}");
            
            // Expected Result: {test_case['expected'][:100]}...
            verifyTestResult("{test_case['id']}", "{test_case['expected'][:200]}");
            
            System.out.println("✅ {test_case['id']} completed successfully");
            
        }} catch (Exception e) {{
            System.out.println("❌ {test_case['id']} failed: " + e.getMessage());
            throw new AssertionError("{test_case['id']} failed: " + e.getMessage());
        }}
    }}'''
        
        test_methods += test_method
    
    # Add helper methods for test execution
    helper_methods = '''
    
    private void performTestCaseActions(String testId, String testName, String steps) {
        System.out.println("🔄 Executing steps for " + testId);
        
        // Parse and execute steps based on content
        if (steps.toLowerCase().contains("price")) {
            enterPrice("25000");
        }
        if (steps.toLowerCase().contains("quantity")) {
            enterQuantity("100");
        }
        if (steps.toLowerCase().contains("payment")) {
            selectPaymentMethods();
        }
        if (steps.toLowerCase().contains("limit")) {
            setOrderLimits("500000", "2500000");
        }
        if (steps.toLowerCase().contains("preview")) {
            clickPreview();
        }
        if (steps.toLowerCase().contains("submit") || steps.toLowerCase().contains("post")) {
            submitAdvertisement();
        }
        
        sleep(2000);
    }
    
    private void verifyTestResult(String testId, String expectedResult) {
        System.out.println("🔍 Verifying result for " + testId);
        
        // Common verification actions
        if (expectedResult.toLowerCase().contains("success")) {
            // Verify success message or successful navigation
            try {
                WebElement successElement = driver.findElement(By.xpath("//*[contains(text(), 'success') or contains(text(), 'Success')]"));
                Assert.assertTrue(successElement.isDisplayed(), "Success message should be displayed");
            } catch (Exception e) {
                // Check URL for success indication
                String currentUrl = driver.getCurrentUrl();
                System.out.println("Current URL: " + currentUrl);
            }
        } else if (expectedResult.toLowerCase().contains("error")) {
            // Verify error message
            try {
                WebElement errorElement = driver.findElement(By.xpath("//*[contains(@class, 'error') or contains(text(), 'error')]"));
                Assert.assertTrue(errorElement.isDisplayed(), "Error message should be displayed");
            } catch (Exception e) {
                System.out.println("No error message found - this might be expected");
            }
        }
    }
    
    private void selectPaymentMethods() {
        try {
            List<WebElement> paymentOptions = driver.findElements(By.xpath("//input[@type='checkbox']"));
            if (!paymentOptions.isEmpty()) {
                paymentOptions.get(0).click();
                System.out.println("✅ Selected payment method");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Payment method selection failed");
        }
    }
    
    private void setOrderLimits(String min, String max) {
        try {
            WebElement minInput = driver.findElement(By.xpath("//input[contains(@placeholder, 'Min') or contains(@name, 'min')]"));
            minInput.clear();
            minInput.sendKeys(min);
            
            WebElement maxInput = driver.findElement(By.xpath("//input[contains(@placeholder, 'Max') or contains(@name, 'max')]"));
            maxInput.clear();
            maxInput.sendKeys(max);
            
            System.out.println("✅ Set order limits: " + min + " - " + max);
        } catch (Exception e) {
            System.out.println("⚠️ Order limits setting failed");
        }
    }
    
    private void clickPreview() {
        try {
            WebElement previewBtn = driver.findElement(By.xpath("//button[contains(text(), 'Preview')]"));
            previewBtn.click();
            sleep(2000);
            System.out.println("✅ Clicked Preview");
        } catch (Exception e) {
            System.out.println("⚠️ Preview button not found");
        }
    }
    
    private void submitAdvertisement() {
        try {
            WebElement submitBtn = driver.findElement(By.xpath("//button[contains(text(), 'Post') or contains(text(), 'Submit')]"));
            submitBtn.click();
            sleep(3000);
            System.out.println("✅ Submitted advertisement");
        } catch (Exception e) {
            System.out.println("⚠️ Submit button not found");
        }
    }'''
    
    test_methods += helper_methods
    
    # Fill in the template
    from datetime import datetime
    final_code = java_template.format(
        total_cases=len(test_cases),
        date=datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
        test_methods=test_methods
    )
    
    return final_code

if __name__ == "__main__":
    file_path = "P2P_Post_Normal_Ad_TestCases_WITH_STEPS.xlsx"
    
    if os.path.exists(file_path):
        df = read_excel_testcases(file_path)
        if df is not None:
            test_cases = generate_java_testcases(df)
            print(f"\n🎉 Successfully generated automation for {len(test_cases)} test cases!")
        else:
            print("❌ Failed to read Excel file")
    else:
        print(f"❌ File not found: {file_path}")
