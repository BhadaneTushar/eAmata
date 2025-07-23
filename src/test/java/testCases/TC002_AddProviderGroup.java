
package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.ProviderGroupPage;
import testBase.BaseClass;
import utilities.AssertionUtils;
import utilities.Constants;
import utilities.ErrorMessages;
import utilities.LoginUtils;
import utilities.LoggerUtils;
import utilities.TestDataGenerator;
import utilities.TestDataCleanup;
import utilities.ErrorHandler;

/**
 * Test class for Provider Group management functionality
 */
public class TC002_AddProviderGroup extends BaseClass {

    private ProviderGroupPage providerGroupPage;
    private String validName;
    private String validEmail;
    private String validPhone;
    private String validNPI;
    private String validSubdomain;
    private String validAddressLine1;
    private String validAddressLine2;
    private String validCity;
    private String validZipCode;
    private String validState;

    /**
     * Set up test data and page objects before each test method
     */
    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and generate test data.")
    public void setUp() {
        LoggerUtils.info("Setting up provider group test data");
        
        // Login as super admin
        LoginUtils.loginAsSuperAdmin();
        
        // Initialize page objects
        providerGroupPage = new ProviderGroupPage();
        
        // Generate test data
        TestDataGenerator dataGenerator = new TestDataGenerator();
        validName = dataGenerator.generateCompanyName();
        validEmail = dataGenerator.generateRandomEmail();
        validPhone = dataGenerator.generatePhoneNumber();
        validNPI = dataGenerator.generateRandomNPI();
        validSubdomain = dataGenerator.generateRandomSubDomain();
        validAddressLine1 = dataGenerator.generateAddressLine1();
        validAddressLine2 = dataGenerator.generateAddressLine2();
        validCity = dataGenerator.generateCity();
        validZipCode = dataGenerator.generateZipCode();
        validState = getConfigManager().getProperty(Constants.DEFAULT_STATE, "Arizona");
        
        LoggerUtils.info("Test setup complete with provider group name: " + validName);
    }

    /**
     * Test adding a provider group with valid data
     */
    @Test(priority = 1, groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a new provider group with valid data")
    @Step("Testing add provider group with valid data")
    public void testAddProviderGroup() {
        String testName = "testAddProviderGroup";
        LoggerUtils.info("Testing add provider group with valid data: " + validName);
        
        try {
            // Add provider group with valid data
            providerGroupPage.addProviderGroup(validName, validEmail, validPhone, validNPI, validSubdomain,
                    validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
            
            // Register test data for cleanup (assuming we get an ID back)
            String groupId = System.currentTimeMillis() + "_" + validName; // Temporary ID generation
            TestDataCleanup.registerProviderGroup(testName, validName, groupId);
            
            // Verify success message
            AssertionUtils.assertEquals(
                providerGroupPage.getSuccessMessage(), 
                "Provider group added successfully!",
                "Provider group was not added successfully."
            );
            
            // Additional verification
            AssertionUtils.assertTrue(
                providerGroupPage.isProviderGroupAddedSuccessfully(),
                "Success message is not displayed after adding provider group"
            );
            
        } catch (Exception e) {
            // Enhanced error handling
            LoggerUtils.error("Error in testAddProviderGroup: " + e.getMessage());
            
            // Let the enhanced error handler deal with it
            boolean recovered = ErrorHandler.handleWebDriverException(e, "provider_group_creation");
            if (!recovered) {
                throw e; // Re-throw if recovery failed
            }
        }
    }

    /**
     * Test validation when provider group name is empty
     */
    @Test(priority = 2, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when provider group name is empty")
    @Step("Testing empty name validation")
    public void testEmptyNameValidation() {
        LoggerUtils.info("Testing empty name validation");
        
        // Add provider group with empty name
        providerGroupPage.addProviderGroup("", validEmail, validPhone, validNPI, validSubdomain,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            providerGroupPage.getNameRequiredError(), 
            ErrorMessages.NAME_REQUIRED,
            "Name required error message does not match."
        );
    }

    /**
     * Test validation when email is invalid
     */
    @Test(priority = 3, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when email is invalid")
    @Step("Testing invalid email validation")
    public void testInvalidEmailValidation() {
        LoggerUtils.info("Testing invalid email validation");
        
        // Add provider group with invalid email
        providerGroupPage.addProviderGroup(validName, "invalid-email", validPhone, validNPI, validSubdomain,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            providerGroupPage.getInvalidEmailError(), 
            ErrorMessages.EMAIL_INVALID,
            "Invalid email error message does not match."
        );
    }

    /**
     * Test validation when phone number is invalid
     */
    @Test(priority = 4, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when phone number is invalid")
    @Step("Testing invalid phone validation")
    public void testInvalidPhoneValidation() {
        LoggerUtils.info("Testing invalid phone validation");
        
        // Add provider group with invalid phone
        providerGroupPage.addProviderGroup(validName, validEmail, "123", validNPI, validSubdomain,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            providerGroupPage.getInvalidPhoneNumberError(), 
            ErrorMessages.PHONE_INVALID,
            "Invalid phone error message does not match."
        );
    }

    /**
     * Test validation when NPI is invalid
     */
    @Test(priority = 5, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when NPI is invalid")
    @Step("Testing invalid NPI validation")
    public void testInvalidNPIValidation() {
        LoggerUtils.info("Testing invalid NPI validation");
        
        // Add provider group with invalid NPI
        providerGroupPage.addProviderGroup(validName, validEmail, validPhone, "123", validSubdomain,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            providerGroupPage.getInvalidNpiNumberError(), 
            ErrorMessages.NPI_INVALID,
            "Invalid NPI error message does not match."
        );
    }

    /**
     * Test validation when subdomain is invalid
     */
    @Test(priority = 6, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when subdomain is invalid")
    @Step("Testing invalid subdomain validation")
    public void testInvalidSubdomainValidation() {
        LoggerUtils.info("Testing invalid subdomain validation");
        
        // Add provider group with invalid subdomain
        providerGroupPage.addProviderGroup(validName, validEmail, validPhone, validNPI, "invalid subdomain",
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        
        // Verify error message
        AssertionUtils.assertEquals(
            providerGroupPage.getInvalidSubDomainError(), 
            ErrorMessages.SUBDOMAIN_INVALID,
            "Invalid subdomain error message does not match."
        );
    }
}
