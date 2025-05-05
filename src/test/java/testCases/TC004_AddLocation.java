package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.LocationPage;
import testBase.BaseClass;
import utilities.LoggerUtils;
import utilities.TestDataGenerator;

/**
 * Test class for Location creation functionality.
 * Contains test cases for valid and invalid location creation scenarios.
 * 
 * Test Cases:
 * 1. Add location with valid details
 * 2. Add location with empty name
 * 3. Add location with empty phone number
 * 4. Add location with empty email
 * 5. Add location with invalid email
 * 6. Add location with empty ZIP code
 */
public class TC004_AddLocation extends BaseClass {

    private LocationPage locationPage;
    private String locationName;
    private String phoneNumber;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zipCode;

    /**
     * Sets up the test environment before each test method.
     * Initializes page objects and generates test data.
     * 
     * @param browser The browser to use for testing
     * @throws RuntimeException if setup fails
     */
    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and generate test data")
    @Parameters("browser")
    public void initializeDriver(@Optional("chrome") String browser) {
        try {
            LoggerUtils.info("Setting up test environment for location creation");
            super.initializeDriver(browser);

            // Initialize page objects and generate test data
            locationPage = new LocationPage(getDriver());
            TestDataGenerator data = new TestDataGenerator();

            // Generate test data
            locationName = data.generateCity();
            phoneNumber = data.generatePhoneNumber();
            email = data.generateRandomEmail();
            addressLine1 = data.generateAddressLine1();
            addressLine2 = data.generateAddressLine2();
            state = properties.getProperty("State");
            city = data.generateCity();
            zipCode = data.generateZipCode();

            LoggerUtils.debug("Test setup completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Failed to setup test environment: " + e.getMessage());
            throw new RuntimeException("Failed to setup test environment", e);
        }
    }

    /**
     * Cleans up after each test method.
     * Closes the browser and performs any necessary cleanup.
     */
    @AfterMethod
    @Description("Cleanup after each test method")
    public void cleanup() {
        try {
            LoggerUtils.info("Cleaning up test environment");
            super.tearDown();
            LoggerUtils.info("Cleanup completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Failed to cleanup: " + e.getMessage());
            throw new RuntimeException("Failed to cleanup", e);
        }
    }

    /**
     * Tests successful location creation with valid details.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 1, groups = { "smoke", "regression" })
    @Description("Verify SuperAdmin can successfully add a location")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddLocationWithValidDetails() {
        try {
            LoggerUtils.info("Starting test: Add location with valid details");
            locationPage.addLocation(locationName, phoneNumber, email, addressLine1,
                    addressLine2, city, zipCode, state);

            String expectedMessage = "Location added successfully!";
            String actualMessage = locationPage.verificationMessage();

            Assert.assertEquals(actualMessage, expectedMessage,
                    "Location creation failed. Expected: '" + expectedMessage +
                            "', Actual: '" + actualMessage + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests location creation with empty name.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 2, groups = { "regression" })
    @Description("Verify error message when location name is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddLocationWithEmptyName() {
        try {
            LoggerUtils.info("Starting test: Add location with empty name");
            locationPage.addLocation("", phoneNumber, email, addressLine1,
                    addressLine2, city, zipCode, state);

            String expectedError = "Name is mandatory";
            String actualError = locationPage.getLocationNameError();

            Assert.assertEquals(actualError, expectedError,
                    "Validation failed! Expected: '" + expectedError +
                            "', Actual: '" + actualError + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests location creation with empty phone number.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 3, groups = { "regression" })
    @Description("Verify error message when phone number is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddLocationWithEmptyPhoneNumber() {
        try {
            LoggerUtils.info("Starting test: Add location with empty phone number");
            locationPage.addLocation(locationName, "", email, addressLine1,
                    addressLine2, city, zipCode, state);

            String expectedError = "Invalid phone number. Please use +91, +1, or +61 followed by 10 to 11 digits, allowing hyphens.";
            String actualError = locationPage.getPhoneNumberError();

            Assert.assertEquals(actualError, expectedError,
                    "Validation failed! Expected: '" + expectedError +
                            "', Actual: '" + actualError + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests location creation with empty email.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 4, groups = { "regression" })
    @Description("Verify error message when email is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddLocationWithEmptyEmail() {
        try {
            LoggerUtils.info("Starting test: Add location with empty email");
            locationPage.addLocation(locationName, phoneNumber, "", addressLine1,
                    addressLine2, city, zipCode, state);

            String expectedError = "Email id is mandatory";
            String actualError = locationPage.getEmptyEmailError();

            Assert.assertEquals(actualError, expectedError,
                    "Validation failed! Expected: '" + expectedError +
                            "', Actual: '" + actualError + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests location creation with invalid email.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 5, groups = { "regression" })
    @Description("Verify error message when email format is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testAddLocationWithInvalidEmail() {
        try {
            LoggerUtils.info("Starting test: Add location with invalid email");
            locationPage.addLocation(locationName, phoneNumber, "invalid-email",
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Invalid email format";
            String actualError = locationPage.getInvalidEmailError();

            Assert.assertEquals(actualError, expectedError,
                    "Validation failed! Expected: '" + expectedError +
                            "', Actual: '" + actualError + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests location creation with empty ZIP code.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 6, groups = { "regression" })
    @Description("Verify error message when ZIP code is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddLocationWithEmptyZipCode() {
        try {
            LoggerUtils.info("Starting test: Add location with empty ZIP code");
            locationPage.addLocation(locationName, phoneNumber, email, addressLine1,
                    addressLine2, city, "", state);

            String expectedError = "Zip code is required";
            String actualError = locationPage.getZipCodeError();

            Assert.assertEquals(actualError, expectedError,
                    "Validation failed! Expected: '" + expectedError +
                            "', Actual: '" + actualError + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }
}
