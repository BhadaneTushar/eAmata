# eAmata Test Automation Framework

A comprehensive, robust test automation framework for the eAmata healthcare application, built with Selenium WebDriver, TestNG, and integrated reporting systems.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Framework Architecture](#framework-architecture)
- [Framework Components](#framework-components)
- [Utilities](#utilities)
- [Best Practices](#best-practices)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Overview

This automation framework is designed to test the eAmata healthcare application, focusing on reliability, maintainability, and comprehensive reporting. The framework follows the Page Object Model design pattern and implements numerous best practices for test automation.

## Features

- **Page Object Model**: Clean separation between test logic and page interactions
- **Dual Reporting System**: Integrated Allure and Extent Reports
- **Data-Driven Testing**: Support for Excel, CSV, and property-based test data
- **Cross-Browser Testing**: Support for Chrome, Firefox, Edge, and Safari
- **Parallel Execution**: Multi-threaded test execution for faster feedback
- **Retry Mechanism**: Automatic retry for flaky tests
- **Screenshot Capture**: Automatic screenshots on test failures
- **Performance Monitoring**: Execution time tracking for performance analysis
- **Centralized Configuration**: Type-safe configuration management
- **Robust Element Handling**: Centralized element interactions with proper waits
- **Test Data Generation**: Utilities for generating random test data
- **Error Handling**: Comprehensive error handling and reporting
- **Logging**: Detailed logging throughout the framework

## Project Structure

```
eAmata/
├── src/
│   └── test/
│       ├── java/
│       │   ├── pageObject/
│       │   │   ├── BasePage.java             # Base class for all page objects
│       │   │   ├── eAmataNursePage.java      # eAmata Nurse management page
│       │   │   ├── LocationPage.java         # Location management page
│       │   │   ├── Logout.java               # Logout functionality
│       │   │   ├── ProviderGroupPage.java    # Provider Group management page
│       │   │   ├── StaffPage.java            # Staff management page
│       │   │   └── SuperAdminLogin.java      # Login page for super admin
│       │   ├── testBase/
│       │   │   └── BaseClass.java            # Base class for all test classes
│       │   ├── testCases/
│       │   │   ├── TC001_SuperAdminLogin.java # Login test cases
│       │   │   ├── TC002_AddProviderGroup.java # Provider Group test cases
│       │   │   ├── TC003_AddStaff.java       # Staff management test cases
│       │   │   ├── TC004_AddLocation.java    # Location management test cases
│       │   │   └── TC005_AddEamataNurse.java # eAmata Nurse test cases
│       │   └── utilities/
│       │       ├── Address.java              # Address generation utility
│       │       ├── AssertionUtils.java       # Centralized assertions
│       │       ├── ConfigManager.java        # Configuration management
│       │       ├── Constants.java            # Framework constants
│       │       ├── DatePicker.java           # Date picker handling
│       │       ├── ElementActions.java       # Element interaction utility
│       │       ├── EnhancedTestListener.java # Enhanced TestNG listener
│       │       ├── ErrorHandler.java         # Error handling utility
│       │       ├── ErrorMessages.java        # Centralized error messages
│       │       ├── LoggerUtils.java          # Logging utility
│       │       ├── LoginUtils.java           # Login helper utility
│       │       ├── PerformanceMonitor.java   # Performance monitoring
│       │       ├── ReportUtils.java          # Reporting utility
│       │       ├── RetryAnalyzer.java        # Test retry mechanism
│       │       ├── ScreenshotUtils.java      # Screenshot utility
│       │       ├── TestDataCleanup.java      # Test data cleanup utility
│       │       ├── TestDataGenerator.java    # Test data generation utility
│       │       ├── TestListener.java         # TestNG listener
│       │       └── WebDriverFactory.java     # WebDriver initialization
│       └── resources/
│           ├── config.properties            # Configuration properties
│           ├── extent-config.xml            # Extent Reports configuration
│           └── log4j2.xml                   # Logging configuration
├── testng-all-tests.xml                     # TestNG suite for all tests
├── testng-regression.xml                    # TestNG suite for regression tests
├── testng-smoke.xml                         # TestNG suite for smoke tests
├── run-all-tests.sh                         # Script to run all tests
├── run-regression-tests.sh                  # Script to run regression tests
├── run-smoke-tests.sh                       # Script to run smoke tests
├── run-enhanced-tests.sh                    # Script to run enhanced tests
├── open-report.sh                           # Script to open test reports
├── pom.xml                                  # Maven configuration
└── README.md                                # Project documentation
```

## Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.8.0 or higher
- **Browsers**: Chrome, Firefox, Edge, or Safari
- **Operating System**: Windows, macOS, or Linux
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code (recommended)

## Setup

1. **Clone the repository**:
   ```bash
   git clone [repository-url]
   cd eAmata
   ```

2. **Install dependencies**:
   ```bash
   mvn clean install -DskipTests
   ```

3. **Configure environment**:
   - Update `src/test/resources/config.properties` with your environment settings
   - Set browser preferences and test environment URLs

4. **WebDriver Setup**:
   - The framework uses WebDriverManager to automatically download and configure drivers
   - No manual driver download is required

## Running Tests

### Using Scripts

The project includes several shell scripts for running tests:

- **All Tests**:
  ```bash
  ./run-all-tests.sh
  ```

- **Smoke Tests**:
  ```bash
  ./run-smoke-tests.sh
  ```

- **Regression Tests**:
  ```bash
  ./run-regression-tests.sh
  ```

- **Enhanced Tests**:
  ```bash
  ./run-enhanced-tests.sh
  ```

### Using Maven

- **Run All Tests**:
  ```bash
  mvn clean test
  ```

- **Run Specific Test Class**:
  ```bash
  mvn test -Dtest=TC001_SuperAdminLogin
  ```

- **Run with Specific TestNG XML**:
  ```bash
  mvn test -Dsurefire.suiteXmlFiles=testng-smoke.xml
  ```

- **Run with Specific Browser**:
  ```bash
  mvn test -Dbrowser=firefox
  ```

- **Run in Headless Mode**:
  ```bash
  mvn test -Dheadless=true
  ```

### Test Suites

The project includes several TestNG XML files for different test suites:

- `testng-all-tests.xml`: All test cases (20 test methods)
- `testng-regression.xml`: Regression test suite
- `testng-smoke.xml`: Critical functionality smoke tests

## Test Reports

### Viewing Reports

After test execution, you can view the reports:

- **Using Script**:
  ```bash
  ./open-report.sh
  ```

- **Extent Reports**:
  - HTML reports are automatically generated in the `reports` directory
  - Open the latest HTML report in any browser

- **Allure Reports**:
  ```bash
  mvn allure:report   # Generate the report
  mvn allure:serve    # Open the report in a browser
  ```

- **TestNG Reports**:
  - Available in `target/surefire-reports` directory

### Report Features

- **Test Status**: Pass/Fail status with detailed logs
- **Screenshots**: Captured on test failures
- **Execution Time**: Test duration metrics
- **Environment Details**: Browser, OS, and environment information
- **Test Steps**: Detailed test step execution logs
- **Error Details**: Comprehensive error information for failures
- **Performance Metrics**: Execution time analysis

## Framework Architecture

### Core Architecture

The eAmata automation framework follows a layered architecture designed for maximum maintainability, scalability, and reusability:

1. **Test Layer**: Contains test classes that implement test scenarios using TestNG
2. **Page Object Layer**: Encapsulates page elements and actions
3. **Utility Layer**: Provides reusable utilities and helper methods
4. **Configuration Layer**: Manages test configuration and environment settings
5. **Reporting Layer**: Handles test reporting and logging
6. **WebDriver Layer**: Manages browser instances and interactions

### Design Patterns

The framework implements several design patterns to promote best practices:

- **Page Object Model (POM)**: Separates UI elements and interactions from test logic
  - Each page in the application has a corresponding Page Object class
  - Page Objects encapsulate page elements and provide methods for page interactions
  - Test classes use Page Objects to interact with the application

- **Factory Pattern**: Used for WebDriver initialization
  - `WebDriverFactory` creates appropriate WebDriver instances based on configuration
  - Supports multiple browsers (Chrome, Firefox, Edge, Safari)
  - Handles browser-specific configurations and capabilities

- **Singleton Pattern**: Used for ConfigManager and other utilities
  - Ensures only one instance of utility classes exists throughout the test execution
  - Provides global access to utility methods
  - Optimizes resource usage

- **Builder Pattern**: Used for complex object creation
  - Simplifies creation of complex objects with many parameters
  - Makes code more readable and maintainable

- **Strategy Pattern**: Used for different browser implementations
  - Allows for different browser-specific implementations
  - Makes adding new browsers easy without changing existing code

### Framework Flow

1. **Test Initialization**:
   - BaseClass initializes WebDriver using WebDriverFactory
   - Configuration is loaded from config.properties
   - Test listeners are registered for reporting

2. **Test Execution**:
   - Test methods use Page Objects to interact with the application
   - ElementActions utility handles all Selenium interactions
   - AssertionUtils validates test results
   - TestListener captures test events for reporting

3. **Test Completion**:
   - Screenshots are captured for failures
   - Reports are generated
   - WebDriver is closed and resources are released

## Framework Components

### Base Classes

#### BasePage

`BasePage` is the parent class for all page objects and provides:

- Common methods for page interactions
- WebDriver instance
- Wait utilities
- Page verification methods
- Logging methods

Key methods:
```java
// Wait for element to be visible
public WebElement waitForElementVisible(By locator)

// Click element with proper waits
public void clickElement(By locator)

// Enter text with validation
public void enterText(By locator, String text)

// Check if element exists
public boolean isElementPresent(By locator)

// Get page title
public String getPageTitle()
```

#### BaseClass

`BaseClass` is the parent class for all test classes and provides:

- WebDriver setup and teardown
- Test configuration
- Common test utilities
- Reporting setup
- Test data management

Key methods:
```java
// Setup method that runs before each test
@BeforeMethod
public void setUp()

// Teardown method that runs after each test
@AfterMethod
public void tearDown()

// Get WebDriver instance
protected WebDriver getDriver()

// Load test data
protected Map<String, String> loadTestData(String testName)
```

### Page Objects

All page objects extend `BasePage` and implement the Page Object Model pattern:

- **SuperAdminLogin**: Handles login functionality
- **ProviderGroupPage**: Manages provider group operations
- **StaffPage**: Handles staff management
- **LocationPage**: Manages location operations
- **eAmataNursePage**: Handles eAmata nurse management
- **Logout**: Manages logout functionality

Example Page Object:
```java
public class SuperAdminLogin extends BasePage {
    // Page elements as private By locators
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[contains(text(),'Login')]");
    
    // Constructor
    public SuperAdminLogin(WebDriver driver) {
        super(driver);
    }
    
    // Page methods
    public void enterUsername(String username) {
        enterText(usernameField, username);
    }
    
    public void enterPassword(String password) {
        enterText(passwordField, password);
    }
    
    public void clickLogin() {
        clickElement(loginButton);
    }
    
    // Business method combining multiple actions
    public void loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}
```

### Test Classes

Test classes extend `BaseClass` and implement test scenarios using TestNG:

- **TC001_SuperAdminLogin**: Tests login functionality
- **TC002_AddProviderGroup**: Tests provider group operations
- **TC003_AddStaff**: Tests staff management
- **TC004_AddLocation**: Tests location operations
- **TC005_AddEamataNurse**: Tests eAmata nurse management

Example Test Class:
```java
public class TC001_SuperAdminLogin extends BaseClass {
    private SuperAdminLogin loginPage;
    
    @BeforeMethod
    public void setupTest() {
        loginPage = new SuperAdminLogin(getDriver());
    }
    
    @Test(description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        // Get test data
        String username = ConfigManager.getInstance().getString("Username");
        String password = ConfigManager.getInstance().getString("Password");
        
        // Perform login
        loginPage.loginAs(username, password);
        
        // Assert successful login
        AssertionUtils.assertTrue(
            driver.getCurrentUrl().contains("dashboard"),
            "User should be redirected to dashboard after login"
        );
    }
    
    @Test(description = "Verify error message with invalid credentials")
    public void testInvalidLogin() {
        // Perform login with invalid credentials
        loginPage.loginAs("invalid@example.com", "wrongpassword");
        
        // Assert error message
        AssertionUtils.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid login"
        );
    }
}
```

### Test Configuration

The framework uses a centralized configuration system:

- **config.properties**: Main configuration file
- **ConfigManager**: Singleton class that provides type-safe access to configuration properties
- **Constants**: Class that defines framework constants

Example Configuration:
```properties
# Environment Configuration
url=https://qa.admin.eamata.com/auth/login
Username=superadminqa@eamata.com
Password=Eamata@123

# Browser Configuration
browser=chrome
Resolution=1920,1080
Headless=false

# Timeouts
implicit.wait.seconds=5
explicit.wait.seconds=15
page.load.timeout.seconds=20
```

## Utilities

### Element Interactions

`ElementActions` provides centralized methods for all Selenium interactions:

```java
public class ElementActions {
    // Click with proper waits and fallback to JavaScript if needed
    public static void click(WebDriver driver, By locator) {
        try {
            WebElement element = waitForClickable(driver, locator);
            element.click();
        } catch (ElementClickInterceptedException e) {
            // Fallback to JavaScript click
            WebElement element = waitForPresence(driver, locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        }
    }
    
    // Enter text with clear and validation
    public static void enterText(WebDriver driver, By locator, String text) {
        WebElement element = waitForClickable(driver, locator);
        element.clear();
        element.sendKeys(text);
        
        // Validate text was entered correctly
        String actualValue = element.getAttribute("value");
        if (!actualValue.equals(text)) {
            // Retry if text wasn't entered correctly
            element.clear();
            element.sendKeys(text);
        }
    }
    
    // Wait for element to be clickable
    public static WebElement waitForClickable(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(
            ConfigManager.getInstance().getInt("explicit.wait.seconds", 15)
        ));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    // Additional methods for select, checkbox, radio buttons, etc.
}
```

### Configuration Management

`ConfigManager` provides type-safe access to configuration properties:

```java
public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;
    
    private ConfigManager() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            LoggerUtils.error("Failed to load config.properties", e);
        }
    }
    
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    public String getString(String key) {
        return properties.getProperty(key);
    }
    
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        try {
            return (value != null) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return (value != null) ? Boolean.parseBoolean(value) : defaultValue;
    }
}
```

### Assertion Utilities

`AssertionUtils` provides enhanced assertions:

```java
public class AssertionUtils {
    // Assert with custom error message and logging
    public static void assertEquals(Object actual, Object expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
            LoggerUtils.info("Assertion passed: " + message);
        } catch (AssertionError e) {
            LoggerUtils.error("Assertion failed: " + message + 
                " - Expected: [" + expected + "] but found: [" + actual + "]");
            throw e;
        }
    }
    
    // Assert element is displayed
    public static void assertElementDisplayed(WebDriver driver, By locator, String message) {
        try {
            WebElement element = driver.findElement(locator);
            Assert.assertTrue(element.isDisplayed(), message);
            LoggerUtils.info("Element is displayed: " + message);
        } catch (Exception e) {
            LoggerUtils.error("Element not displayed: " + message);
            throw new AssertionError(message, e);
        }
    }
    
    // Assert element contains text
    public static void assertElementContainsText(WebDriver driver, By locator, String expectedText, String message) {
        try {
            WebElement element = driver.findElement(locator);
            String actualText = element.getText();
            Assert.assertTrue(actualText.contains(expectedText), 
                message + " - Expected to contain: [" + expectedText + 
                "] but found: [" + actualText + "]");
            LoggerUtils.info("Element contains text: " + message);
        } catch (Exception e) {
            LoggerUtils.error("Element text assertion failed: " + message);
            throw new AssertionError(message, e);
        }
    }
}
```

### Test Data Generation

`TestDataGenerator` provides methods for generating test data:

```java
public class TestDataGenerator {
    private static final Faker faker = new Faker();
    
    // Generate random email
    public static String generateEmail() {
        return faker.internet().emailAddress();
    }
    
    // Generate random name
    public static String generateName() {
        return faker.name().fullName();
    }
    
    // Generate random phone number
    public static String generatePhoneNumber() {
        return faker.phoneNumber().cellPhone();
    }
    
    // Generate random address
    public static Address generateAddress() {
        Address address = new Address();
        address.setStreet(faker.address().streetAddress());
        address.setCity(faker.address().city());
        address.setState(faker.address().state());
        address.setZipCode(faker.address().zipCode());
        return address;
    }
    
    // Generate random future date
    public static Date generateFutureDate(int minDays, int maxDays) {
        return faker.date().future(maxDays, minDays, TimeUnit.DAYS);
    }
    
    // Generate random alphanumeric string
    public static String generateAlphanumeric(int length) {
        return faker.regexify("[A-Za-z0-9]{" + length + "}");
    }
}
```

### Error Handling

`ErrorHandler` provides centralized error handling:

```java
public class ErrorHandler {
    // Handle WebDriver exceptions
    public static void handleWebDriverException(WebDriverException e, WebDriver driver, String action) {
        String message = "Error during " + action + ": " + e.getMessage();
        LoggerUtils.error(message, e);
        
        // Capture screenshot
        String screenshotPath = ScreenshotUtils.captureScreenshot(driver, "error_" + System.currentTimeMillis());
        LoggerUtils.info("Screenshot captured: " + screenshotPath);
        
        // Attach screenshot to Allure report
        if (screenshotPath != null) {
            try {
                Allure.addAttachment("Error Screenshot", 
                    new ByteArrayInputStream(Files.readAllBytes(Paths.get(screenshotPath))));
            } catch (IOException ioe) {
                LoggerUtils.error("Failed to attach screenshot to Allure report", ioe);
            }
        }
        
        // Throw custom exception with details
        throw new TestFrameworkException(message, e);
    }
    
    // Handle assertion errors
    public static void handleAssertionError(AssertionError e, WebDriver driver, String assertion) {
        String message = "Assertion failed: " + assertion + " - " + e.getMessage();
        LoggerUtils.error(message, e);
        
        // Capture screenshot
        String screenshotPath = ScreenshotUtils.captureScreenshot(driver, "assertion_" + System.currentTimeMillis());
        LoggerUtils.info("Screenshot captured: " + screenshotPath);
        
        // Attach screenshot to Allure report
        if (screenshotPath != null) {
            try {
                Allure.addAttachment("Assertion Failure Screenshot", 
                    new ByteArrayInputStream(Files.readAllBytes(Paths.get(screenshotPath))));
            } catch (IOException ioe) {
                LoggerUtils.error("Failed to attach screenshot to Allure report", ioe);
            }
        }
        
        // Re-throw the assertion error
        throw e;
    }
}
```

### Screenshot Utilities

`ScreenshotUtils` provides methods for capturing screenshots:

```java
public class ScreenshotUtils {
    // Capture screenshot and save to file
    public static String captureScreenshot(WebDriver driver, String fileName) {
        try {
            if (driver instanceof TakesScreenshot) {
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String directory = "screenshots";
                File dir = new File(directory);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                
                String filePath = directory + File.separator + fileName + ".png";
                FileUtils.copyFile(screenshotFile, new File(filePath));
                LoggerUtils.info("Screenshot saved: " + filePath);
                return filePath;
            }
        } catch (Exception e) {
            LoggerUtils.error("Failed to capture screenshot", e);
        }
        return null;
    }
    
    // Capture screenshot as base64 string
    public static String captureScreenshotAsBase64(WebDriver driver) {
        try {
            if (driver instanceof TakesScreenshot) {
                return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            }
        } catch (Exception e) {
            LoggerUtils.error("Failed to capture screenshot as base64", e);
        }
        return null;
    }
    
    // Capture screenshot of specific element
    public static String captureElementScreenshot(WebDriver driver, By locator, String fileName) {
        try {
            WebElement element = driver.findElement(locator);
            File screenshotFile = element.getScreenshotAs(OutputType.FILE);
            String directory = "screenshots";
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            String filePath = directory + File.separator + fileName + ".png";
            FileUtils.copyFile(screenshotFile, new File(filePath));
            LoggerUtils.info("Element screenshot saved: " + filePath);
            return filePath;
        } catch (Exception e) {
            LoggerUtils.error("Failed to capture element screenshot", e);
        }
        return null;
    }
}
```

### Reporting Utilities

`ReportUtils` manages reporting functionality:

```java
public class ReportUtils {
    private static ExtentReports extentReport;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    // Initialize Extent Report
    public static synchronized ExtentReports initExtentReport() {
        if (extentReport == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports/ExtentReport_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".html");
            
            try {
                sparkReporter.loadXMLConfig("src/test/resources/extent-config.xml");
            } catch (IOException e) {
                LoggerUtils.error("Failed to load Extent Report config", e);
            }
            
            extentReport = new ExtentReports();
            extentReport.attachReporter(sparkReporter);
            
            // Set system info
            extentReport.setSystemInfo("OS", System.getProperty("os.name"));
            extentReport.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReport.setSystemInfo("Browser", ConfigManager.getInstance().getString("browser", "chrome"));
            extentReport.setSystemInfo("Environment", ConfigManager.getInstance().getString("environment", "QA"));
        }
        return extentReport;
    }
    
    // Create test in Extent Report
    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = initExtentReport().createTest(testName, description);
        extentTest.set(test);
        return test;
    }
    
    // Get current test
    public static synchronized ExtentTest getTest() {
        return extentTest.get();
    }
    
    // Log test step
    public static void logStep(String stepDescription) {
        if (extentTest.get() != null) {
            extentTest.get().info(stepDescription);
        }
        
        // Also log to Allure
        Allure.step(stepDescription);
    }
    
    // Log test step with screenshot
    public static void logStepWithScreenshot(WebDriver driver, String stepDescription) {
        String base64Screenshot = ScreenshotUtils.captureScreenshotAsBase64(driver);
        
        if (extentTest.get() != null && base64Screenshot != null) {
            extentTest.get().info(stepDescription, 
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        }
        
        // Also log to Allure
        Allure.step(stepDescription, () -> {
            if (base64Screenshot != null) {
                Allure.addAttachment("Screenshot", 
                    new ByteArrayInputStream(Base64.getDecoder().decode(base64Screenshot)));
            }
        });
    }
    
    // Flush Extent Report
    public static synchronized void flushReport() {
        if (extentReport != null) {
            extentReport.flush();
        }
    }
}
```

## Best Practices

- **Write clear test descriptions** using Allure annotations
- **Keep tests independent** to avoid dependencies between tests
- **Use proper waits** instead of Thread.sleep()
- **Implement proper assertions** with meaningful error messages
- **Take screenshots on failures** for better debugging
- **Use centralized utilities** instead of duplicating code
- **Follow naming conventions** for better readability
- **Add proper documentation** with JavaDoc comments
- **Use constants** instead of hard-coded values
- **Implement proper exception handling** with meaningful error messages

## Troubleshooting

### Common Issues

1. **Element Not Found Exceptions**:
   - Check element locators
   - Increase wait timeouts in config.properties
   - Check if the element is in an iframe
   - Verify page is fully loaded before interacting

2. **Browser Compatibility Issues**:
   - Update browser to latest version
   - Check WebDriverManager version compatibility
   - Try different browser options

3. **Test Data Issues**:
   - Verify test data is properly configured
   - Check for data dependencies between tests
   - Use TestDataGenerator for random data

4. **Performance Issues**:
   - Optimize wait strategies
   - Check for memory leaks in WebDriver instances
   - Adjust Maven memory settings in MAVEN_OPTS

### Debugging Tips

- Enable detailed logging in config.properties
- Check screenshots in the reports directory
- Review test execution logs
- Use PerformanceMonitor to identify slow tests

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests locally
5. Submit a pull request

## License

[Add your license information here] 