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
import utilities.LoginUtils;
import utilities.TestDataGenerator;

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
    @Description("Setup WebDriver, initialize Page Objects, and generate test data.")
    public void setUp() {
        LoginUtils.loginAsSuperAdmin();
        providerGroupPage = new ProviderGroupPage(getDriver());
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
        validState = properties.getProperty("State"); // Using a fixed state for consistency
    }

    @Test(priority = 1, groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a new provider group with valid data")
    public void testAddProviderGroup() {
        providerGroupPage.addProviderGroup(validName, validEmail, validPhone, validNPI, validSubdomain,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(providerGroupPage.getSuccessMessage(), "Provider group added successfully!",
                "Provider group was not added successfully.");
    }

    @Test(priority = 2, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when provider group name is empty")
    public void testEmptyNameValidation() {
        providerGroupPage.addProviderGroup("", validEmail, validPhone, validNPI, validSubdomain,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(providerGroupPage.getNameRequiredError(), ErrorMessages.NAME_REQUIRED,
                "Name required error message does not match.");
    }

    @Test(priority = 3, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when email is invalid")
    public void testInvalidEmailValidation() {
        providerGroupPage.addProviderGroup(validName, "invalid-email", validPhone, validNPI, validSubdomain,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(providerGroupPage.getInvalidEmailError(), ErrorMessages.EMAIL_INVALID,
                "Invalid email error message does not match.");
    }

    @Test(priority = 4, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when phone number is invalid")
    public void testInvalidPhoneValidation() {
        providerGroupPage.addProviderGroup(validName, validEmail, "123", validNPI, validSubdomain,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(providerGroupPage.getInvalidPhoneNumberError(), ErrorMessages.PHONE_INVALID,
                "Invalid phone error message does not match.");
    }

    @Test(priority = 5, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when NPI is invalid")
    public void testInvalidNPIValidation() {
        providerGroupPage.addProviderGroup(validName, validEmail, validPhone, "123", validSubdomain,
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(providerGroupPage.getInvalidNpiNumberError(), ErrorMessages.NPI_INVALID,
                "Invalid NPI error message does not match.");
    }

    @Test(priority = 6, groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when subdomain is invalid")
    public void testInvalidSubdomainValidation() {
        providerGroupPage.addProviderGroup(validName, validEmail, validPhone, validNPI, "invalid subdomain",
                validAddressLine1, validAddressLine2, validCity, validZipCode, validState);
        Assert.assertEquals(providerGroupPage.getInvalidSubDomainError(), ErrorMessages.SUBDOMAIN_INVALID,
                "Invalid subdomain error message does not match.");
    }
}
