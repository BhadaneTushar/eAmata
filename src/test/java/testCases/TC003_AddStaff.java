package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.StaffPage;
import testBase.BaseClass;
import utilities.AssertionUtils;
import utilities.Constants;
import utilities.ErrorMessages;
import utilities.LoginUtils;
import utilities.LoggerUtils;
import utilities.TestDataGenerator;

/**
 * Test class for Staff management functionality
 *
 * Flow:
 * - Inherits WebDriver lifecycle from `testBase.BaseClass`.
 * - @BeforeMethod setup():
 *   - Logs in via `LoginUtils.loginAsSuperAdmin()` which internally uses `SuperAdminLogin` page and `ConfigManager` credentials.
 *   - Initializes `StaffPage` and generates test data via `TestDataGenerator` and defaults from `ConfigManager`.
 * - Each @Test calls `staffPage.addStaff(...)` and then reads UI messages for assertions.
 *
 * Data:
 * - Inputs: names/emails/phones/role/gender/address from `TestDataGenerator` and config defaults.
 * - Outputs: success/error messages returned from page methods for validation.
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
     * Set up test data and page objects before each test method
     */
    @BeforeMethod
    @Description("Setup test prerequisites")
    public void setup() {
        LoggerUtils.info("Setting up staff test data");
        
        // Login as super admin
        LoginUtils.loginAsSuperAdmin();
        
        // Initialize test data
        TestDataGenerator data = new TestDataGenerator();
        staffPage = new StaffPage();
        firstName = data.generateRandomFirstName();
        lastName = data.generateRandomLastName();
        email = data.generateEmail("staff");
        phoneNumber = data.generatePhoneNumber();
        role = getConfigManager().getProperty("StaffRole", "Admin");
        gender = getConfigManager().getProperty(Constants.DEFAULT_GENDER, "Male");
        addressLine1 = data.generateAddressLine1();
        addressLine2 = data.generateAddressLine2();
        state = getConfigManager().getProperty(Constants.DEFAULT_STATE, "Arizona");
        city = data.generateCity();
        zipCode = data.generateZipCode();
        
        LoggerUtils.info("Test setup complete with staff name: " + firstName + " " + lastName);
    }

    /**
     * Test adding a staff member with valid details
     */
    @Test(priority = 1, groups = {"smoke", "regression"})
    @Description("Verify SuperAdmin can successfully add a staff member")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Testing add staff with valid details")
    public void testAddStaffWithValidDetails() {
        LoggerUtils.info("Testing add staff with valid details: " + firstName + " " + lastName);
        
        // Add staff with valid details
        staffPage.addStaff(firstName, lastName, email, phoneNumber, role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        // Verify success message
        String expectedMessage = "User added successfully!";
        String actualMessage = staffPage.getStaffVerificationText();
        AssertionUtils.assertEquals(
            actualMessage, 
            expectedMessage,
            "Staff creation failed: Expected: '" + expectedMessage + "', Actual: '" + actualMessage + "'"
        );
    }

    /**
     * Test validation when first name is empty
     */
    @Test(priority = 2, groups = {"regression"})
    @Description("Verify error message when first name is empty")
    @Severity(SeverityLevel.NORMAL)
    @Step("Testing empty first name validation")
    public void testAddStaffWithEmptyFirstName() {
        LoggerUtils.info("Testing empty first name validation");
        
        // Add staff with empty first name
        staffPage.addStaff("", lastName, email, phoneNumber, role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        // Verify error message
        AssertionUtils.assertEquals(
            staffPage.getFirstNameRequiredError(), 
            ErrorMessages.FIRST_NAME_REQUIRED,
            "First name required error message does not match"
        );
    }

    /**
     * Test validation when last name is empty
     */
    @Test(priority = 3, groups = {"regression"})
    @Description("Verify error message when last name is empty")
    @Severity(SeverityLevel.NORMAL)
    @Step("Testing empty last name validation")
    public void testAddStaffWithEmptyLastName() {
        LoggerUtils.info("Testing empty last name validation");
        
        // Add staff with empty last name
        staffPage.addStaff(firstName, "", email, phoneNumber, role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        // Verify error message
        AssertionUtils.assertEquals(
            staffPage.getLastNameRequiredError(), 
            ErrorMessages.LAST_NAME_REQUIRED,
            "Last name required error message does not match"
        );
    }

    /**
     * Test validation when email is empty
     */
    @Test(priority = 4, groups = {"regression"})
    @Description("Verify error message when email is empty")
    @Severity(SeverityLevel.NORMAL)
    @Step("Testing empty email validation")
    public void testAddStaffWithEmptyEmail() {
        LoggerUtils.info("Testing empty email validation");
        
        // Add staff with empty email
        staffPage.addStaff(firstName, lastName, "", phoneNumber, role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        // Verify error message
        String expectedError = "Email is required";
        String actualError = staffPage.getEmailRequiredError();
        AssertionUtils.assertEquals(
            actualError, 
            expectedError,
            "Validation failed! Expected: '" + expectedError + "', Actual: '" + actualError + "'"
        );
    }

    /**
     * Test validation when phone number is empty
     */
    @Test(priority = 5, groups = {"regression"})
    @Description("Verify error message when phone number is empty")
    @Severity(SeverityLevel.NORMAL)
    @Step("Testing empty phone number validation")
    public void testAddStaffWithEmptyPhoneNumber() {
        LoggerUtils.info("Testing empty phone number validation");
        
        // Add staff with empty phone number
        staffPage.addStaff(firstName, lastName, email, "", role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        // Verify error message
        String expectedError = "Phone is required";
        String actualError = staffPage.getPhoneRequiredError();
        AssertionUtils.assertEquals(
            actualError, 
            expectedError,
            "Validation failed! Expected: '" + expectedError + "', Actual: '" + actualError + "'"
        );
    }

    /**
     * Test validation when phone number is invalid
     */
    @Test(priority = 6, groups = {"regression"})
    @Description("Verify error message when phone number is invalid")
    @Severity(SeverityLevel.NORMAL)
    @Step("Testing invalid phone number validation")
    public void testAddStaffWithInvalidPhoneNumber() {
        LoggerUtils.info("Testing invalid phone number validation");
        
        // Add staff with invalid phone number
        staffPage.addStaff(firstName, lastName, email, "12345", role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        // Verify error message
        String expectedError = "Invalid phone number. It must be 10 digits.";
        String actualError = staffPage.getInvalidPhoneNumberError();
        AssertionUtils.assertEquals(
            actualError, 
            expectedError,
            "Validation failed! Expected: '" + expectedError + "', Actual: '" + actualError + "'"
        );
    }
}
