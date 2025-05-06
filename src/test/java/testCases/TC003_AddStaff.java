package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.StaffPage;
import testBase.BaseClass;
import utilities.ErrorMessages;
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
    private TestDataGenerator dataGenerator;
    private String validFirstName;
    private String validLastName;
    private String validEmail;
    private String validPhone;
    private String validRole;
    private String validGender;
    private String validAddressLine1;
    private String validAddressLine2;
    private String validCity;
    private String validZipCode;
    private String validState;

    /**
     * Sets up the test environment before each test method.
     * Initializes page objects and generates test data.
     * 
     * @param browser The browser to use for testing
     * @throws RuntimeException if setup fails
     */
    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and generate test data.")
    public void setUp() {
        super.setUp();
        staffPage = new StaffPage(getDriver());
        dataGenerator = new TestDataGenerator();

        validFirstName = dataGenerator.generateRandomFirstName();
        validLastName = dataGenerator.generateRandomLastName();
        validEmail = dataGenerator.generateRandomEmail();
        validPhone = dataGenerator.generatePhoneNumber();
        validRole = properties.getProperty("Role");
        validGender = properties.getProperty("Gender");
        validAddressLine1 = dataGenerator.generateAddressLine1();
        validAddressLine2 = dataGenerator.generateAddressLine2();
        validCity = dataGenerator.generateCity();
        validZipCode = dataGenerator.generateZipCode();
        validState = properties.getProperty("State");
    }

    @Test(priority = 1, groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a new staff member with valid data")
    public void testAddStaff() {
        staffPage.addStaff(validFirstName, validLastName, validEmail, validPhone, validRole, validGender,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        String expectedText = validFirstName + " " + validLastName;
        Assert.assertEquals(staffPage.getStaffVerificationText(), expectedText,
                "Staff was not added successfully.");
    }

    @Test(priority = 2, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when first name is empty")
    public void testEmptyFirstNameValidation() {
        staffPage.addStaff("", validLastName, validEmail, validPhone, validRole, validGender,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(staffPage.getFirstNameRequiredError(), ErrorMessages.FIRST_NAME_REQUIRED,
                "First name required error message does not match.");
    }

    @Test(priority = 3, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when last name is empty")
    public void testEmptyLastNameValidation() {
        staffPage.addStaff(validFirstName, "", validEmail, validPhone, validRole, validGender,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(staffPage.getLastNameRequiredError(), ErrorMessages.LAST_NAME_REQUIRED,
                "Last name required error message does not match.");
    }

    @Test(priority = 4, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when email is empty")
    public void testEmptyEmailValidation() {
        staffPage.addStaff(validFirstName, validLastName, "", validPhone, validRole, validGender,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(staffPage.getEmailRequiredError(), ErrorMessages.EMAIL_REQUIRED,
                "Email required error message does not match.");
    }

    @Test(priority = 5, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when phone number is invalid")
    public void testInvalidPhoneValidation() {
        staffPage.addStaff(validFirstName, validLastName, validEmail, "123", validRole, validGender,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(staffPage.getInvalidPhoneNumberError(), ErrorMessages.PHONE_INVALID,
                "Invalid phone error message does not match.");
    }
}
