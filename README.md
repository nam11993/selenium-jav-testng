# Local Selenium Tests

This project contains a local version of the SignupTest.java file that can be run on your local machine without BrowserStack.

## Prerequisites

1. **Java 11 or higher** - Make sure you have Java JDK 11+ installed
2. **Maven** - Make sure Maven is installed and available in your PATH
3. **Chrome Browser** - The tests are configured to run with Chrome by default

## Project Structure

```
local-selenium-tests/
├── pom.xml                     # Maven configuration
├── src/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── browserstack/
│       │           ├── LocalBrowserTest.java   # Base test class for local execution
│       │           └── SignupTest.java         # Test cases
│       └── resources/
│           └── testng.xml      # TestNG configuration
└── README.md
```

## Key Changes from BrowserStack Version

1. **LocalBrowserTest.java** - Base class for Selenium WebDriver tests (used by SignupTest)
2. **LocalSelenideTest.java** - Base class for Selenide tests (used by other test classes)
3. **WebDriverManager** - Automatically downloads and manages browser drivers
4. **Simplified Configuration** - No need for BrowserStack credentials or YAML files
5. **Local Browser Support** - Supports Chrome and Firefox browsers
6. **Selenide Integration** - Added Selenide framework for more concise test writing

## Test Frameworks Used

### Selenium WebDriver
- **SignupTest.java** uses pure Selenium WebDriver
- More verbose but gives full control over browser interactions
- Good for complex test scenarios

### Selenide
- **BStackSampleTest.java** and **Suite Tests** use Selenide framework
- More concise and readable test code
- Built-in waits and assertions
- Easier to write and maintain tests

Both frameworks are included and can be used based on your preference.

## How to Run Tests

### Option 1: Run all tests using Maven
```bash
cd "d:\Microsoft VS Code\New folder\local-selenium-tests"
mvn clean test
```

### Option 2: Run specific test class
```bash
mvn test -Dtest=SignupTest
```

### Option 3: Run specific test method
```bash
mvn test -Dtest=SignupTest#testValidEmailSignup
```

### Option 4: Run with different browser (Firefox)
Edit the `testng.xml` file and change the browser parameter:
```xml
<parameter name="browser" value="firefox"/>
```

### Option 5: Run in headless mode
Edit `LocalBrowserTest.java` and uncomment the headless options:
```java
// chromeOptions.addArguments("--headless"); // Uncomment for headless mode
```

## Available Test Classes

### 1. SignupTest.java
Tests for signup functionality on your application:
- `testValidEmailSignup()` - Tests valid email signup flow
- `testValidPhoneNumberSignup()` - Tests valid phone number signup flow
- `testInvalidEmail()` - Tests invalid email validation
- `testInvalidPhoneNumber()` - Tests invalid phone number validation
- `testExistingEmail()` - Tests existing email error handling
- `testExistingPhoneNumber()` - Tests existing phone number error handling
- `testPasswordTooShort()` - Tests password length validation
- `testPasswordWithoutSymbol()` - Tests password symbol requirement
- `testPasswordWithoutNumber()` - Tests password number requirement
- `testPasswordWithoutUppercase()` - Tests password uppercase requirement
- `testPasswordWithoutLowercase()` - Tests password lowercase requirement
- `testPasswordsDontMatch()` - Tests password confirmation matching

### 2. BStackSampleTest.java
Sample test for BrowserStack demo application:
- `test()` - Tests adding a product to cart on bstackdemo.com

### 3. BStackLocalTest.java
Test for local testing capabilities:
- `test()` - Tests page title verification on bstackdemo.com

### 4. Suite Tests (SuiteTest01-04.java)
Multiple test classes for parallel execution testing:
- All contain the same cart functionality test
- Useful for testing parallel execution and suite configurations

## How to Run Tests

### Option 1: Run all tests using Maven
```bash
cd "d:\Microsoft VS Code\New folder\local-selenium-tests"
mvn clean test
```

### Option 2: Run specific test configurations
```bash
# Run only signup tests
mvn test -DsuiteXmlFile=src/test/resources/signup-tests.xml

# Run only suite tests (SuiteTest01-04)
mvn test -DsuiteXmlFile=src/test/resources/suite-tests.xml

# Run all test classes
mvn test -DsuiteXmlFile=src/test/resources/all-tests.xml
```

### Option 3: Run specific test class
```bash
mvn test -Dtest=SignupTest
mvn test -Dtest=BStackSampleTest
mvn test -Dtest="com.browserstack.suite.SuiteTest01"
```

### Option 4: Run specific test method
```bash
mvn test -Dtest=SignupTest#testValidEmailSignup
mvn test -Dtest=BStackSampleTest#test
```
