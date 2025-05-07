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