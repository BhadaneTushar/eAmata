package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.ProviderGroupPage;
import testBase.BaseClass;
import utilities.ErrorMessages;

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

        @BeforeMethod
        @Description("Setup WebDriver, initialize Page Objects, and fetch test data from properties file.")
        public void setUp() {
                providerGroupPage = new ProviderGroupPage();
                validName = properties.getProperty("ProviderGroupName");
                validEmail = properties.getProperty("ProviderGroupEmail");
                validPhone = properties.getProperty("ProviderGroupPhone");
                validNPI = properties.getProperty("ProviderGroupNPI");
                validSubdomain = properties.getProperty("ProviderGroupSubdomain");
                validAddressLine1 = properties.getProperty("ProviderGroupAddressLine1");
                validAddressLine2 = properties.getProperty("ProviderGroupAddressLine2");
                validCity = properties.getProperty("ProviderGroupCity");
                validZipCode = properties.getProperty("ProviderGroupZipCode");
                validState = properties.getProperty("ProviderGroupState");
                Assert.assertNotNull(validName, "Provider Group Name is not set in the properties file.");
                Assert.assertNotNull(validEmail, "Provider Group Email is not set in the properties file.");
                Assert.assertNotNull(validPhone, "Provider Group Phone is not set in the properties file.");
                Assert.assertNotNull(validNPI, "Provider Group NPI is not set in the properties file.");
                Assert.assertNotNull(validSubdomain, "Provider Group Subdomain is not set in the properties file.");
                Assert.assertNotNull(validAddressLine1,
                                "Provider Group Address Line 1 is not set in the properties file.");
                Assert.assertNotNull(validCity, "Provider Group City is not set in the properties file.");
                Assert.assertNotNull(validZipCode, "Provider Group Zip Code is not set in the properties file.");
                Assert.assertNotNull(validState, "Provider Group State is not set in the properties file.");
        }

        @Test(priority = 1, groups = { "smoke", "regression" })
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify adding a new provider group with valid data")
        public void testAddProviderGroup() {
                providerGroupPage.addProviderGroup(validName, validEmail, validPhone, validNPI, validSubdomain,
                                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
                Assert.assertEquals(providerGroupPage.getSuccessMessage(), "Provider group added successfully!",
                                "Provider group was not added successfully.");
        }

        @Test(priority = 2, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when provider group name is empty")
        public void testEmptyNameValidation() {
                providerGroupPage.addProviderGroup("", validEmail, validPhone, validNPI, validSubdomain,
                                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
                Assert.assertEquals(providerGroupPage.getNameRequiredError(), ErrorMessages.NAME_REQUIRED,
                                "Name required error message does not match.");
        }

        @Test(priority = 3, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when email is invalid")
        public void testInvalidEmailValidation() {
                providerGroupPage.addProviderGroup(validName, "invalid-email", validPhone, validNPI, validSubdomain,
                                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
                Assert.assertEquals(providerGroupPage.getInvalidEmailError(), ErrorMessages.EMAIL_INVALID,
                                "Invalid email error message does not match.");
        }

        @Test(priority = 4, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when phone number is invalid")
        public void testInvalidPhoneValidation() {
                providerGroupPage.addProviderGroup(validName, validEmail, "123", validNPI, validSubdomain,
                                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
                Assert.assertEquals(providerGroupPage.getInvalidPhoneNumberError(), ErrorMessages.PHONE_INVALID,
                                "Invalid phone error message does not match.");
        }

        @Test(priority = 5, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when NPI is invalid")
        public void testInvalidNPIValidation() {
                providerGroupPage.addProviderGroup(validName, validEmail, validPhone, "123", validSubdomain,
                                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
                Assert.assertEquals(providerGroupPage.getInvalidNpiNumberError(), ErrorMessages.NPI_INVALID,
                                "Invalid NPI error message does not match.");
        }

        @Test(priority = 6, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when subdomain is invalid")
        public void testInvalidSubdomainValidation() {
                providerGroupPage.addProviderGroup(validName, validEmail, validPhone, validNPI, "invalid subdomain",
                                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
                Assert.assertEquals(providerGroupPage.getInvalidSubDomainError(), ErrorMessages.SUBDOMAIN_INVALID,
                                "Invalid subdomain error message does not match.");
        }
}
