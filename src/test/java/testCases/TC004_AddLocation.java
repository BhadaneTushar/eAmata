package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.LocationPage;
import testBase.BaseClass;
import utilities.AssertionUtils;
import utilities.Constants;
import utilities.ErrorMessages;
import utilities.LoginUtils;
import utilities.LoggerUtils;
import utilities.TestDataGenerator;

/**
 * Test class for Location management functionality
 *
 * Flow:
 * - Inherits WebDriver lifecycle from `testBase.BaseClass`.
 * - @BeforeMethod setUp():
 *   - Logs in via `LoginUtils.loginAsSuperAdmin()` which uses `SuperAdminLogin` and `ConfigManager` credentials.
 *   - Initializes `LocationPage` and generates test data via `TestDataGenerator` with defaults from `ConfigManager`.
 * - Each @Test:
 *   - Calls `locationPage.addLocation(...)` to perform the flow, then reads UI messages for assertions.
 *
 * Data:
 * - Inputs: name/phone/email/address/city/zip/state from `TestDataGenerator` and config defaults.
 * - Outputs: success/error messages used by assertions.
 */
public class TC004_AddLocation extends BaseClass {

    private final ThreadLocal<LocationPage> locationPage = new ThreadLocal<>();
    private TestDataGenerator dataGenerator;
    private String validName;
    private String validPhone;
    private String validEmail;
    private String validAddressLine1;
    private String validAddressLine2;
    private String validCity;
    private String validZipCode;
    private String validState;

    /**
     * Set up test data and page objects before each test method
     */
    @BeforeMethod
    @Description("Setup test prerequisites")
    public void setUp() {
        LoggerUtils.info("Setting up location test data");
        
        // Login as super admin
        LoginUtils.loginAsSuperAdmin();
        
        // Initialize page objects
        locationPage.set(new LocationPage());
        
        // Generate test data
        dataGenerator = new TestDataGenerator();
        validName = dataGenerator.generateCompanyName();
        validPhone = dataGenerator.generatePhoneNumber();
        validEmail = dataGenerator.generateRandomEmail();
        validAddressLine1 = dataGenerator.generateAddressLine1();
        validAddressLine2 = dataGenerator.generateAddressLine2();
        validCity = dataGenerator.generateCity();
        validZipCode = dataGenerator.generateZipCode();
        validState = getConfigManager().getProperty(Constants.DEFAULT_STATE, "Arizona");
        
        LoggerUtils.info("Test setup complete with location name: " + validName);
    }

    /**
     * Test adding a location with valid data
     */
    @Test(priority = 1, groups = {"smoke", "regression"}, retryAnalyzer = utilities.RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a new location with valid data")
    @Step("Testing add location with valid data")
    public void testAddLocation() {
        LoggerUtils.info("Testing add location with valid data: " + validName);
        
        // Add location with valid data
        locationPage.get().addLocation(validName, validPhone, validEmail, validAddressLine1, validAddressLine2,
                validCity, validZipCode, validState);
        
        // Verify success message
        AssertionUtils.assertEquals(
            locationPage.get().getSuccessMessage(), 
            "Location added successfully!",
            "Location was not added successfully."
        );
        
        // Additional verification
        AssertionUtils.assertTrue(
            locationPage.get().isLocationAddedSuccessfully(),
            "Success message is not displayed after adding location"
        );
    }

    /**
     * Test validation when name is empty
     */
    @Test(priority = 2, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when name is empty")
    @Step("Testing empty name validation")
    public void testEmptyNameValidation() {
        LoggerUtils.info("Testing empty name validation");
        
        // Add location with empty name
        locationPage.get().addLocation("", validPhone, validEmail, validAddressLine1, validAddressLine2,
                validCity, validZipCode, validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            locationPage.get().getNameRequiredError(), 
            ErrorMessages.LOCATION_NAME_REQUIRED,
            "Name required error message does not match."
        );
    }

    /**
     * Test validation when phone number is invalid
     */
    @Test(priority = 3, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when phone number is invalid")
    @Step("Testing invalid phone validation")
    public void testInvalidPhoneValidation() {
        LoggerUtils.info("Testing invalid phone validation");
        
        // Add location with invalid phone
        locationPage.get().addLocation(validName, "123", validEmail, validAddressLine1, validAddressLine2,
                validCity, validZipCode, validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            locationPage.get().getInvalidPhoneNumberError(),
            ErrorMessages.LOCATION_PHONE_INVALID,
            "Invalid phone error message does not match."
        );
    }

    /**
     * Test validation when email is empty
     */
    @Test(priority = 4, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when email is empty")
    @Step("Testing empty email validation")
    public void testEmptyEmailValidation() {
        LoggerUtils.info("Testing empty email validation");
        
        // Add location with empty email
        locationPage.get().addLocation(validName, validPhone, "", validAddressLine1, validAddressLine2,
                validCity, validZipCode, validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            locationPage.get().getEmailRequiredError(), 
            ErrorMessages.LOCATION_EMAIL_REQUIRED,
            "Email required error message does not match."
        );
    }

    /**
     * Test validation when email is invalid
     */
    @Test(priority = 5, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when email is invalid")
    @Step("Testing invalid email validation")
    public void testInvalidEmailValidation() {
        LoggerUtils.info("Testing invalid email validation");
        
        // Add location with invalid email
        locationPage.get().addLocation(validName, validPhone, "invalid-email", validAddressLine1,
                validAddressLine2, validCity, validZipCode, validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            locationPage.get().getInvalidEmailFormatError(),
            ErrorMessages.LOCATION_EMAIL_INVALID,
            "Invalid email error message does not match."
        );
    }

    /**
     * Test validation when zip code is empty
     */
    @Test(priority = 6, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when zip code is empty")
    @Step("Testing empty zip code validation")
    public void testEmptyZipCodeValidation() {
        LoggerUtils.info("Testing empty zip code validation");
        
        // Add location with empty zip code
        locationPage.get().addLocation(validName, validPhone, validEmail, validAddressLine1, validAddressLine2,
                validCity, "", validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            locationPage.get().getZipCodeRequiredError(), 
            ErrorMessages.ZIP_CODE_REQUIRED,
            "Zip code required error message does not match."
        );
    }
}
