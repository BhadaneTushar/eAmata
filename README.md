# eAmata Test Automation Project

This project contains automated tests for the eAmata application, using Selenium WebDriver, TestNG, and Allure for reporting.

## Prerequisites

- Java 17 or higher
- Maven
- Chrome browser
- ChromeDriver (compatible with your Chrome version)

## Project Structure

```
eAmata/
├── src/
│   ├── main/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── eamata/
│       │           ├── pages/
│       │           ├── tests/
│       │           └── utils/
│       └── resources/
│           └── testng.xml
├── .github/
│   └── workflows/
│       └── ci.yml
├── pom.xml
└── README.md
```

## Setup

1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd eAmata
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

3. Configure ChromeDriver:
   - Download ChromeDriver compatible with your Chrome version
   - Place it in the project root directory or update the path in your configuration

## Running Tests

### Local Execution

Run all tests:
```bash
mvn clean test
```

Run specific test class:
```bash
mvn test -Dtest=TestClassName
```

### Viewing Reports

After test execution, generate and view the Allure report:
```bash
mvn allure:report
mvn allure:serve
```

## CI/CD Pipeline

The project includes a CI/CD pipeline that runs on every push to the development branch. The pipeline:

1. Builds the project
2. Runs all tests
3. Generates Allure reports
4. Sends test results to Google Chat

### Pipeline Features

- Automated test execution
- Test report generation
- Artifact uploads
- Google Chat notifications with test results

### Google Chat Integration

To enable Google Chat notifications:

1. Create a webhook in your Google Chat space
2. Add the webhook URL as a GitHub secret:
   - Go to repository Settings > Secrets and variables > Actions
   - Create a new secret named `GOOGLE_CHAT_WEBHOOK_URL`
   - Add your webhook URL

## Test Reports

Test reports are available in multiple formats:

- Allure Reports: Detailed HTML reports with test results, screenshots, and logs
- TestNG Reports: XML and HTML reports in the `test-output` directory
- Screenshots: Captured during test failures in the `screenshots` directory

## Contributing

1. Create a new branch for your feature
2. Make your changes
3. Run tests locally
4. Submit a pull request

## Best Practices

- Write clear test descriptions
- Use page object model for better maintainability
- Take screenshots on test failures
- Keep test data separate from test logic
- Use meaningful test names and descriptions

## Troubleshooting

Common issues and solutions:

1. ChromeDriver version mismatch:
   - Ensure ChromeDriver version matches your Chrome browser version
   - Update ChromeDriver path in configuration

2. Test failures:
   - Check screenshots in the `screenshots` directory
   - Review Allure reports for detailed failure information
   - Check test logs in the `logs` directory

## License

[Add your license information here]

## Contact

[Add your contact information here]

# Framework Enhancements

## DRY Principle Implementation

The framework has been enhanced to follow the DRY (Don't Repeat Yourself) principle through the following improvements:

### Centralized Element Interactions
- Created `ElementActions` utility class that centralizes all Selenium interactions
- Implemented robust waiting mechanisms with proper error handling
- Added fallback mechanisms (e.g., JavaScript click when regular click fails)

### Removed Duplication
- Eliminated duplicate code for common operations across page objects
- Centralized waiting logic in utility classes
- Standardized element interaction patterns

### Modular Structure
- Clear separation between:
  - Page Objects: Represent UI elements and actions
  - Test Cases: Implement test scenarios
  - Utilities: Provide reusable functionality
  - Base Classes: Define common behavior

## Configuration Management

- Created `ConfigManager` class to handle all configuration properties
- Implemented singleton pattern for efficient resource usage
- Added type-safe methods for retrieving configuration values
- Centralized default values for configuration properties

## Constants Management

- Created `Constants` class to eliminate hard-coded values
- Categorized constants for better organization:
  - File paths
  - Timeouts
  - XPath expressions
  - Browser options
  - Configuration keys
  - Error messages

## WebDriver Management

- Created `WebDriverFactory` to handle browser initialization
- Implemented factory pattern for creating different browser instances
- Centralized browser-specific configuration
- Added headless mode support for all browsers

## Enhanced Reporting

- Improved `AllureListener` for better test reporting
- Added detailed logging throughout the framework
- Implemented screenshot capture for test failures
- Added test execution metrics

## Assertions

- Created `AssertionUtils` to centralize assertion logic
- Added enhanced logging for assertions
- Implemented soft assertions for non-critical validations
- Added element-specific assertions

## Screenshot Handling

- Created `ScreenshotUtils` to centralize screenshot capture
- Added multiple output formats (file, byte array, base64)
- Implemented automatic directory creation for screenshots

## Best Practices

- Applied Java naming conventions throughout the codebase
- Added comprehensive JavaDoc comments
- Implemented proper exception handling
- Used design patterns where appropriate (Singleton, Factory, Page Object)
- Followed single responsibility principle for all classes

# Recent Framework Updates

## Framework Enhancements - June 2025

The eAmata automation framework has been significantly enhanced to improve maintainability and follow best practices:

### Core Framework Improvements
- Created centralized utility classes for element interactions, waits, assertions, etc.
- Removed hard-coded values and moved them to Constants class
- Implemented ConfigManager for type-safe configuration access
- Added robust error handling and logging throughout the framework

### Page Object Model Enhancements
- Updated all page objects to use the centralized ElementActions utility
- Added proper JavaDoc documentation for methods and classes
- Added Allure steps and logging for better reporting
- Standardized constructor pattern across all page objects

### Test Class Improvements
- Updated test classes to use AssertionUtils instead of direct TestNG assertions
- Added detailed logging for better troubleshooting
- Improved test setup with ConfigManager instead of direct property access
- Added proper test documentation with Allure annotations

### Best Practices Applied
- Applied DRY principle by centralizing common functionality
- Improved error messages and logging for better debugging
- Enhanced code readability with consistent formatting and naming
- Added proper exception handling with meaningful error messages

These improvements make the framework more maintainable, scalable, and easier to use for the entire team.

# Enhanced Reporting

## Advanced Reporting Framework

The eAmata automation framework now includes an advanced reporting system that integrates both Allure and Extent Reports for comprehensive test result visualization and analysis.

### Key Reporting Features

- **Dual Reporting System**: Generates both Allure and Extent reports from a single test execution
- **Rich Test Details**: Captures test steps, screenshots, logs, and error details
- **Execution Metrics**: Tracks and reports test execution times at suite, class, and method levels
- **Failure Analysis**: Provides detailed failure information with screenshots and current URL
- **Parallel Execution Support**: Thread-safe reporting for parallel test execution

### Report Utilities

- **ReportUtils**: Centralized utility for reporting functions used by both Allure and Extent reports
- **TestListener**: Advanced TestNG listener that handles reporting events for both reporting frameworks
- **Enhanced Screenshots**: Multiple screenshot capture methods with different output formats

### Viewing Reports

After test execution, you can view the reports:

**Extent Reports**:
- HTML reports are automatically generated in the `reports` directory
- Open the latest HTML report in any browser

**Allure Reports**:
```bash
mvn allure:report   # Generate the report
mvn allure:serve    # Open the report in a browser
``` 