package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.eAmataNursePage;
import testBase.BaseClass;
import utilities.Address;
import utilities.LoggerUtils;
import utilities.TestDataGenerator;

/**
 * Test class for eAmata Nurse creation functionality.
 * Contains test cases for valid and invalid nurse creation scenarios.
 * 
 * Test Cases:
 * 1. Add nurse with valid details
 * 2. Add nurse with empty first name
 * 3. Add nurse with empty last name
 * 4. Add nurse with empty email
 * 5. Add nurse with invalid email
 * 6. Add nurse with empty phone number
 * 7. Add nurse with invalid phone number
 * 8. Add nurse with empty NPI
 * 9. Add nurse with invalid NPI
 * 10. Add nurse with empty license details
 */
public class TC005_AddEamataNurse extends BaseClass {

    private eAmataNursePage nursePage;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String NPI;
    private String gender;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zipCode;
    private String licenseNumber;
    private String licenseState;
    private String licenseExpiryDate;

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
            LoggerUtils.info("Setting up test environment for eAmata nurse creation");
            super.initializeDriver(browser);

            // Initialize page objects and generate test data
            nursePage = new eAmataNursePage(getDriver());
            TestDataGenerator data = new TestDataGenerator();

            // Generate test data
            firstName = data.generateRandomFirstName();
            lastName = data.generateRandomLastName();
            email = data.generateEmail("nurse");
            phoneNumber = data.generatePhoneNumber();
            NPI = data.generateRandomNPI();
            gender = properties.getProperty("Gender");
            addressLine1 = data.generateAddressLine1();
            addressLine2 = data.generateAddressLine2();
            state = properties.getProperty("State");
            city = data.generateCity();
            zipCode = data.generateZipCode();
            licenseNumber = data.generateRandomNPI();
            licenseState = properties.getProperty("licenseState");
            licenseExpiryDate = properties.getProperty("licenseExpiryDate");

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
     * Tests successful nurse creation with valid details.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 1, groups = { "smoke", "regression" })
    @Description("Verify SuperAdmin can successfully add eAmata Nurse")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddEamataNurseWithValidDetails() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with valid details");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails(firstName, lastName, email, phoneNumber, NPI, gender);
            
            new Address(getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
            nursePage.enterLicenseDetails(licenseNumber, licenseState, licenseExpiryDate);
            nursePage.saveNurse();

            String expectedMessage = "Nurse added successfully!";
            String actualMessage = nursePage.verifyNurseCreation();

            Assert.assertEquals(actualMessage, expectedMessage,
                    "Nurse creation failed. Expected: '" + expectedMessage +
                            "', Actual: '" + actualMessage + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests nurse creation with empty first name.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 2, groups = { "regression" })
    @Description("Verify error message when first name is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddEamataNurseWithEmptyFirstName() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with empty first name");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails("", lastName, email, phoneNumber, NPI, gender);
            nursePage.saveNurse();

            String expectedError = "First Name is required";
            String actualError = nursePage.getFirstNameError();

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
     * Tests nurse creation with empty last name.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 3, groups = { "regression" })
    @Description("Verify error message when last name is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddEamataNurseWithEmptyLastName() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with empty last name");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails(firstName, "", email, phoneNumber, NPI, gender);
            nursePage.saveNurse();

            String expectedError = "Last Name is required";
            String actualError = nursePage.getLastNameError();

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
     * Tests nurse creation with empty email.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 4, groups = { "regression" })
    @Description("Verify error message when email is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddEamataNurseWithEmptyEmail() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with empty email");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails(firstName, lastName, "", phoneNumber, NPI, gender);
            nursePage.saveNurse();

            String expectedError = "Email is required";
            String actualError = nursePage.getEmailError();

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
     * Tests nurse creation with invalid email.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 5, groups = { "regression" })
    @Description("Verify error message when email format is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testAddEamataNurseWithInvalidEmail() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with invalid email");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails(firstName, lastName, "invalid-email", phoneNumber, NPI, gender);
            nursePage.saveNurse();

            String expectedError = "Invalid email format";
            String actualError = nursePage.getEmailError();

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
     * Tests nurse creation with empty phone number.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 6, groups = { "regression" })
    @Description("Verify error message when phone number is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddEamataNurseWithEmptyPhoneNumber() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with empty phone number");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails(firstName, lastName, email, "", NPI, gender);
            nursePage.saveNurse();

            String expectedError = "Phone number is required";
            String actualError = nursePage.getPhoneNumberError();

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
     * Tests nurse creation with invalid phone number.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 7, groups = { "regression" })
    @Description("Verify error message when phone number format is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testAddEamataNurseWithInvalidPhoneNumber() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with invalid phone number");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails(firstName, lastName, email, "12345", NPI, gender);
            nursePage.saveNurse();

            String expectedError = "Invalid phone number format";
            String actualError = nursePage.getPhoneNumberError();

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
     * Tests nurse creation with empty NPI.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 8, groups = { "regression" })
    @Description("Verify error message when NPI is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddEamataNurseWithEmptyNPI() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with empty NPI");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails(firstName, lastName, email, phoneNumber, "", gender);
            nursePage.saveNurse();

            String expectedError = "NPI is required";
            String actualError = nursePage.getNPIError();

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
     * Tests nurse creation with invalid NPI.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 9, groups = { "regression" })
    @Description("Verify error message when NPI format is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testAddEamataNurseWithInvalidNPI() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with invalid NPI");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails(firstName, lastName, email, phoneNumber, "12345", gender);
            nursePage.saveNurse();

            String expectedError = "Invalid NPI format";
            String actualError = nursePage.getNPIError();

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
     * Tests nurse creation with empty license details.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 10, groups = { "regression" })
    @Description("Verify error message when license details are empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddEamataNurseWithEmptyLicenseDetails() {
        try {
            LoggerUtils.info("Starting test: Add eAmata nurse with empty license details");
            nursePage.navigateToAddNurseForm();
            nursePage.enterNurseDetails(firstName, lastName, email, phoneNumber, NPI, gender);
            new Address(getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
            nursePage.enterLicenseDetails("", "", "");
            nursePage.saveNurse();

            String expectedError = "License details are required";
            String actualError = nursePage.getLicenseError();

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
