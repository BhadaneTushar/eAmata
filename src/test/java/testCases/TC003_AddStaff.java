package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.StaffPage;
import pageObject.SuperAdminLogin;
import testBase.BaseClass;
import utilities.LoggerUtils;
import utilities.TestDataGenerator;

/**
 * Test class for Staff creation functionality.
 * Contains test cases for valid and invalid staff creation scenarios.
 * 
 * Test Cases:
 * 1. Add staff with valid details
 * 2. Add staff with empty first name
 * 3. Add staff with empty last name
 * 4. Add staff with empty email
 * 5. Add staff with empty phone number
 * 6. Add staff with invalid phone number
 */
public class TC003_AddStaff extends BaseClass {

    private StaffPage staffPage;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private String gender;
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
    public void setup() {
        super.setUp();
        TestDataGenerator data = new TestDataGenerator();
        addStaff = new StaffPage(getDriver());
        firstName = data.generateRandomFirstName();
        lastName = data.generateRandomLastName();
        email = data.generateEmail("staff");
        phoneNumber = data.generatePhoneNumber();
        role = properties.getProperty("StaffRole");
        gender = properties.getProperty("Gender");
        addressLine1 = data.generateAddressLine1();
        addressLine2 = data.generateAddressLine2();
        state = properties.getProperty("State");
        city = data.generateCity();
        zipCode = data.generateZipCode();
    }

    @Test(priority = 1, groups = {"smoke", "regression"})
    @Description("Verify SuperAdmin can successfully add a staff member")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddStaffWithValidDetails() {
        try {
            LoggerUtils.info("Starting test: Add staff with valid details");
            staffPage.addStaff(firstName, lastName, email, phoneNumber, role, gender,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedMessage = firstName + " " + lastName;
            String actualMessage = staffPage.verifySuccessMessage();

            Assert.assertEquals(actualMessage, expectedMessage,
                    "Staff creation failed: Name mismatch. Expected: '" + expectedMessage +
                            "', Actual: '" + actualMessage + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests staff creation with empty first name.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 2, groups = { "regression" })
    @Description("Verify error message when first name is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithEmptyFirstName() {
        try {
            LoggerUtils.info("Starting test: Add staff with empty first name");
            staffPage.addStaff("", lastName, email, phoneNumber, role, gender,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "First Name is required";
            String actualError = staffPage.getFirstNameError();

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
     * Tests staff creation with empty last name.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 3, groups = { "regression" })
    @Description("Verify error message when last name is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithEmptyLastName() {
        try {
            LoggerUtils.info("Starting test: Add staff with empty last name");
            staffPage.addStaff(firstName, "", email, phoneNumber, role, gender,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Last Name is required";
            String actualError = staffPage.getLastNameError();

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
     * Tests staff creation with empty email.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 4, groups = { "regression" })
    @Description("Verify error message when email is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithEmptyEmail() {
        try {
            LoggerUtils.info("Starting test: Add staff with empty email");
            staffPage.addStaff(firstName, lastName, "", phoneNumber, role, gender,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Email is required";
            String actualError = staffPage.getEmptyGmailError();

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
     * Tests staff creation with empty phone number.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 5, groups = { "regression" })
    @Description("Verify error message when phone number is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithEmptyPhoneNumber() {
        try {
            LoggerUtils.info("Starting test: Add staff with empty phone number");
            staffPage.addStaff(firstName, lastName, email, "", role, gender,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Phone is required";
            String actualError = staffPage.getEmptyPhoneError();

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
     * Tests staff creation with invalid phone number.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 6, groups = { "regression" })
    @Description("Verify error message when phone number is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithInvalidPhoneNumber() {
        try {
            LoggerUtils.info("Starting test: Add staff with invalid phone number");
            staffPage.addStaff(firstName, lastName, email, "12345", role, gender,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Invalid phone number. It must be 10 digits.";
            String actualError = staffPage.getPhoneNumberError();

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
