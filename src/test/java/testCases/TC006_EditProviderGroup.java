package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.ProviderGroupPage;
import testBase.BaseClass;
import utilities.LoggerUtils;
import utilities.TestDataGenerator;

/**
 * Test class for Provider Group editing functionality.
 * Contains test cases for valid and invalid provider group editing scenarios.
 * 
 * Test Cases:
 * 1. Edit provider group with valid details
 * 2. Edit provider group with empty name
 * 3. Edit provider group with empty email
 * 4. Edit provider group with invalid email
 * 5. Edit provider group with empty phone number
 * 6. Edit provider group with invalid phone number
 * 7. Edit provider group with empty NPI
 * 8. Edit provider group with invalid NPI
 * 9. Edit provider group with empty subdomain
 * 10. Edit provider group with invalid subdomain
 */
public class TC006_EditProviderGroup extends BaseClass {

    private ProviderGroupPage providerGroupPage;
    private String providerGroupName;
    private String email;
    private String phoneNumber;
    private String NPI;
    private String subdomain;
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
            LoggerUtils.info("Setting up test environment for provider group editing");
            super.initializeDriver(browser);

            // Initialize page objects and generate test data
            providerGroupPage = new ProviderGroupPage(getDriver());
            TestDataGenerator data = new TestDataGenerator();

            // Generate test data
            providerGroupName = data.generateCompanyName();
            email = data.generateRandomEmail();
            phoneNumber = data.generatePhoneNumber();
            NPI = data.generateRandomNPI();
            subdomain = data.generateRandomSubDomain();
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
     * Tests successful provider group editing with valid details.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 1, groups = { "smoke", "regression" })
    @Description("Verify SuperAdmin can successfully edit a provider group")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditProviderGroupWithValidDetails() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with valid details");
            providerGroupPage.editProviderGroup(providerGroupName, email, phoneNumber, NPI, subdomain,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedMessage = "Provider group updated successfully!";
            String actualMessage = providerGroupPage.verificationMessage();

            Assert.assertEquals(actualMessage, expectedMessage,
                    "Provider group update failed. Expected: '" + expectedMessage +
                            "', Actual: '" + actualMessage + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests provider group editing with empty name.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 2, groups = { "regression" })
    @Description("Verify error message when provider group name is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testEditProviderGroupWithEmptyName() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with empty name");
            providerGroupPage.editProviderGroup("", email, phoneNumber, NPI, subdomain,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Name is mandatory";
            String actualError = providerGroupPage.getProviderGroupNameError();

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
     * Tests provider group editing with empty email.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 3, groups = { "regression" })
    @Description("Verify error message when email is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testEditProviderGroupWithEmptyEmail() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with empty email");
            providerGroupPage.editProviderGroup(providerGroupName, "", phoneNumber, NPI, subdomain,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Email id is mandatory";
            String actualError = providerGroupPage.getEmptyEmailError();

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
     * Tests provider group editing with invalid email.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 4, groups = { "regression" })
    @Description("Verify error message when email format is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testEditProviderGroupWithInvalidEmail() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with invalid email");
            providerGroupPage.editProviderGroup(providerGroupName, "invalid-email", phoneNumber, NPI, subdomain,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Invalid email format";
            String actualError = providerGroupPage.getInvalidEmailError();

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
     * Tests provider group editing with empty phone number.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 5, groups = { "regression" })
    @Description("Verify error message when phone number is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testEditProviderGroupWithEmptyPhoneNumber() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with empty phone number");
            providerGroupPage.editProviderGroup(providerGroupName, email, "", NPI, subdomain,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Invalid phone number. Please use +91, +1, or +61 followed by 10 to 11 digits, allowing hyphens.";
            String actualError = providerGroupPage.getPhoneNumberError();

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
     * Tests provider group editing with invalid phone number.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 6, groups = { "regression" })
    @Description("Verify error message when phone number format is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testEditProviderGroupWithInvalidPhoneNumber() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with invalid phone number");
            providerGroupPage.editProviderGroup(providerGroupName, email, "12345", NPI, subdomain,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Invalid phone number format";
            String actualError = providerGroupPage.getPhoneNumberError();

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
     * Tests provider group editing with empty NPI.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 7, groups = { "regression" })
    @Description("Verify error message when NPI is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testEditProviderGroupWithEmptyNPI() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with empty NPI");
            providerGroupPage.editProviderGroup(providerGroupName, email, phoneNumber, "", subdomain,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "NPI is required";
            String actualError = providerGroupPage.getNPIError();

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
     * Tests provider group editing with invalid NPI.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 8, groups = { "regression" })
    @Description("Verify error message when NPI format is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testEditProviderGroupWithInvalidNPI() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with invalid NPI");
            providerGroupPage.editProviderGroup(providerGroupName, email, phoneNumber, "12345", subdomain,
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Invalid NPI format";
            String actualError = providerGroupPage.getNPIError();

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
     * Tests provider group editing with empty subdomain.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 9, groups = { "regression" })
    @Description("Verify error message when subdomain is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testEditProviderGroupWithEmptySubdomain() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with empty subdomain");
            providerGroupPage.editProviderGroup(providerGroupName, email, phoneNumber, NPI, "",
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Subdomain is required";
            String actualError = providerGroupPage.getSubdomainError();

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
     * Tests provider group editing with invalid subdomain.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 10, groups = { "regression" })
    @Description("Verify error message when subdomain format is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testEditProviderGroupWithInvalidSubdomain() {
        try {
            LoggerUtils.info("Starting test: Edit provider group with invalid subdomain");
            providerGroupPage.editProviderGroup(providerGroupName, email, phoneNumber, NPI, "invalid@subdomain",
                    addressLine1, addressLine2, city, zipCode, state);

            String expectedError = "Invalid subdomain format";
            String actualError = providerGroupPage.getSubdomainError();

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
