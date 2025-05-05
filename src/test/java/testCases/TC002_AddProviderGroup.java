package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.Optional;
import pageObject.ProviderGroupPage;
import testBase.BaseClass;
import utilities.LoggerUtils;
import utilities.TestDataGenerator;

/**
 * Test class for Provider Group creation functionality.
 * Contains test cases for valid and invalid provider group creation scenarios.
 */
public class TC002_AddProviderGroup extends BaseClass {

        private ProviderGroupPage providerGroupPage;
        private String providerGroupName;
        private String email;
        private String phoneNumber;
        private String npiNumber;
        private String subDomain;
        private String addressLine1;
        private String addressLine2;
        private String state;
        private String city;
        private String zipCode;

        /**
         * Sets up test data before each test method.
         * Initializes page object and generates test data.
         */
        @BeforeMethod
        @Description("Setup test environment and generate test data")
        @Parameters("browser")
        public void initializeDriver(@Optional("chrome") String browser) {
                LoggerUtils.info("Setting up test environment for provider group creation");
                super.initializeDriver(browser);
                TestDataGenerator data = new TestDataGenerator();
                providerGroupPage = new ProviderGroupPage(getDriver());

                // Generate test data
                providerGroupName = data.generateCompanyName();
                email = data.generateRandomEmail();
                phoneNumber = data.generatePhoneNumber();
                npiNumber = data.generateRandomNPI();
                subDomain = data.generateRandomSubDomain();
                addressLine1 = data.generateAddressLine1();
                addressLine2 = data.generateAddressLine2();
                state = properties.getProperty("State");
                city = data.generateCity();
                zipCode = data.generateZipCode();

                LoggerUtils.debug("Test data generated successfully");
        }

        @AfterMethod
        @Description("Cleanup after each test")
        public void cleanup() {
                LoggerUtils.info("Cleaning up test environment");
                // Add cleanup logic here if needed
                super.tearDown();
        }

        /**
         * Tests successful provider group creation with valid details.
         */
        @Test(priority = 1, groups = { "smoke", "regression" })
        @Description("Verify SuperAdmin can successfully add a provider group")
        @Severity(SeverityLevel.CRITICAL)
        public void testAddProviderGroupWithValidDetails() {
                LoggerUtils.info("Starting test: Add provider group with valid details");
                providerGroupPage.addProviderGroup(providerGroupName, email, phoneNumber, npiNumber, subDomain,
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedMessage = "Provider group added successfully!";
                String actualMessage = providerGroupPage.getSuccessMessage();

                Assert.assertEquals(actualMessage, expectedMessage,
                                "Provider Group creation failed! Expected: '" + expectedMessage + "', but got: '"
                                                + actualMessage + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with empty name.
         */
        @Test(priority = 2, groups = { "regression" })
        @Description("Verify error message when Provider Group Name is empty")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithEmptyName() {
                LoggerUtils.info("Starting test: Add provider group with empty name");
                providerGroupPage.addProviderGroup(
                                "", email, phoneNumber, npiNumber, subDomain,
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedError = "Name is required";
                String actualError = providerGroupPage.getProviderGroupNameError();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with invalid email format.
         */
        @Test(priority = 3, groups = { "regression" })
        @Description("Verify error message when email format is invalid")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithInvalidEmail() {
                LoggerUtils.info("Starting test: Add provider group with invalid email");
                providerGroupPage.addProviderGroup(
                                providerGroupName, "invalid-email", phoneNumber, npiNumber, subDomain,
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedError = "Invalid email format";
                String actualError = providerGroupPage.getInvalidEmailError();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with invalid phone number.
         */
        @Test(priority = 4, groups = { "regression" })
        @Description("Verify error message when phone number format is invalid")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithInvalidPhone() {
                LoggerUtils.info("Starting test: Add provider group with invalid phone number");
                providerGroupPage.addProviderGroup(
                                providerGroupName, email, "12345", npiNumber, subDomain,
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedError = "Invalid phone number format";
                String actualError = providerGroupPage.getPhoneNumberError();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with invalid NPI.
         */
        @Test(priority = 5, groups = { "regression" })
        @Description("Verify error message when NPI format is invalid")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithInvalidNPI() {
                LoggerUtils.info("Starting test: Add provider group with invalid NPI");
                providerGroupPage.addProviderGroup(
                                providerGroupName, email, phoneNumber, "12345", subDomain,
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedError = "Invalid NPI format";
                String actualError = providerGroupPage.getNPIError();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with empty NPI number.
         */
        @Test(priority = 6, groups = { "regression" })
        @Description("Verify error message when NPI number is empty")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithEmptyNpiNumber() {
                LoggerUtils.info("Starting test: Add provider group with empty NPI number");
                providerGroupPage.addProviderGroup(
                                providerGroupName, email, phoneNumber, "", subDomain,
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedError = "NPI is required";
                String actualError = providerGroupPage.getEmptyNPIError();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with empty phone number.
         */
        @Test(priority = 7, groups = { "regression" })
        @Description("Verify error message when phone number is empty")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithEmptyPhoneNumber() {
                LoggerUtils.info("Starting test: Add provider group with empty phone number");
                providerGroupPage.addProviderGroup(
                                providerGroupName, email, "", npiNumber, subDomain,
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedError = "Phone is required";
                String actualError = providerGroupPage.getEmptyPhoneError();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with empty email.
         */
        @Test(priority = 8, groups = { "regression" })
        @Description("Verify error message when email is empty")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithEmptyEmail() {
                LoggerUtils.info("Starting test: Add provider group with empty email");
                providerGroupPage.addProviderGroup(
                                providerGroupName, "", phoneNumber, npiNumber, subDomain,
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedError = "Email is required";
                String actualError = providerGroupPage.getEmptyEmailError();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with missing address details.
         */
        @Test(priority = 9, groups = { "regression" })
        @Description("Verify error message when address details are missing")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithMissingAddress() {
                LoggerUtils.info("Starting test: Add provider group with missing address");
                providerGroupPage.addProviderGroup(
                                providerGroupName, email, phoneNumber, npiNumber, subDomain,
                                "", "", city, zipCode, state);

                String expectedError = "Line 1 is required";
                String actualError = providerGroupPage.getAddressError();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with invalid subdomain.
         */
        @Test(priority = 10, groups = { "regression" })
        @Description("Verify error message when subdomain format is invalid")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithInvalidSubdomain() {
                LoggerUtils.info("Starting test: Add provider group with invalid subdomain");
                providerGroupPage.addProviderGroup(
                                providerGroupName, email, phoneNumber, npiNumber, "invalid@subdomain",
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedError = "Invalid subdomain format";
                String actualError = providerGroupPage.getSubdomainError();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }

        /**
         * Tests provider group creation with empty subdomain.
         */
        @Test(priority = 11, groups = { "regression" })
        @Description("Verify error message when Subdomain is missing")
        @Severity(SeverityLevel.NORMAL)
        public void testAddProviderGroupWithEmptySubdomain() {
                LoggerUtils.info("Starting test: Add provider group with empty subdomain");
                providerGroupPage.addProviderGroup(providerGroupName, email, phoneNumber, npiNumber, "",
                                addressLine1, addressLine2, city, zipCode, state);

                String expectedError = "Sub domain field is required";
                String actualError = providerGroupPage.getEmptySubdomain();

                Assert.assertEquals(actualError, expectedError,
                                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
                LoggerUtils.info("Test completed successfully");
        }
}
