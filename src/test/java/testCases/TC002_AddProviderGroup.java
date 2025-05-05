package testCases;

import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.ProviderGroupPage;
import testBase.BaseClass;
import utilities.TestDataGenerator;

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

    @BeforeMethod
    public void setUp() {
        super.setUp();
        TestDataGenerator data = new TestDataGenerator();
        providerGroupPage = new ProviderGroupPage();
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
    }

    @Test(priority = 1, groups = {"smoke", "regression"})
    @Description("Verify SuperAdmin can successfully add a provider group")
    public void addProviderGroupWithValidDetails() {
        providerGroupPage.addProviderGroup(providerGroupName, email, phoneNumber, npiNumber, subDomain,
                addressLine1, addressLine2, city, zipCode, state);

        String expectedMessage = "Provider group added successfully!";
        String actualMessage = providerGroupPage.getSuccessMessage();

        Assert.assertEquals(actualMessage, expectedMessage,
                "Provider Group creation failed! Expected: '" + expectedMessage + "', but got: '" + actualMessage + "'");
    }

    @Test(priority = 2, groups = {"regression"})
    @Description("Verify error message when Provider Group Name is empty")
    public void addProviderGroupWithEmptyName() {
        providerGroupPage.addProviderGroup(
                "", email, phoneNumber, npiNumber, subDomain,
                addressLine1, addressLine2, city, zipCode, state
        );

        String expectedError = "Name is required";
        String actualError = providerGroupPage.getProviderGroupNameError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 3, groups = {"regression"})
    @Description("Verify error message when email format is invalid")
    public void addProviderGroupWithInvalidEmail() {
        providerGroupPage.addProviderGroup(
                providerGroupName, "invalidemail", phoneNumber, npiNumber, subDomain,
                addressLine1, addressLine2, city, zipCode, state
        );

        String expectedError = "Invalid email address";
        String actualError = providerGroupPage.getInvalidEmail();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 4, groups = {"regression"})
    @Description("Verify error message when phone number is invalid")
    public void addProviderGroupWithInvalidPhoneNumber() {
        providerGroupPage.addProviderGroup(
                providerGroupName, email, "12345", npiNumber, subDomain,
                addressLine1, addressLine2, city, zipCode, state
        );

        String expectedError = "Invalid phone number. It must be 10 digits.";
        String actualError = providerGroupPage.getPhoneError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 5, groups = {"regression"})
    @Description("Verify error message when NPI number is invalid")
    public void addProviderGroupWithInvalidNpiNumber() {
        providerGroupPage.addProviderGroup(
                providerGroupName, email, phoneNumber, "123456", subDomain,
                addressLine1, addressLine2, city, zipCode, state
        );

        String expectedError = "Must be 10 digits";
        String actualError = providerGroupPage.getInvalidNPI();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 6, groups = {"regression"})
    @Description("Verify error message when NPI number is empty")
    public void addProviderGroupWithEmptyNpiNumber() {
        providerGroupPage.addProviderGroup(
                providerGroupName, email, phoneNumber, "", subDomain,
                addressLine1, addressLine2, city, zipCode, state
        );

        String expectedError = "NPI is required";
        String actualError = providerGroupPage.getEmptyNPIError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 7, groups = {"regression"})
    @Description("Verify error message when phone number is empty")
    public void addProviderGroupWithEmptyPhoneNumber() {
        providerGroupPage.addProviderGroup(
                providerGroupName, email, "", npiNumber, subDomain,
                addressLine1, addressLine2, city, zipCode, state
        );

        String expectedError = "Phone is required";
        String actualError = providerGroupPage.getEmptyPhoneError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 8, groups = {"regression"})
    @Description("Verify error message when email is empty")
    public void addProviderGroupWithEmptyEmail() {
        providerGroupPage.addProviderGroup(
                providerGroupName, "", phoneNumber, npiNumber, subDomain,
                addressLine1, addressLine2, city, zipCode, state
        );

        String expectedError = "Email is required";
        String actualError = providerGroupPage.getEmptyGmailError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 9, groups = {"regression"})
    @Description("Verify error message when address details are missing")
    public void addProviderGroupWithMissingAddress() {
        providerGroupPage.addProviderGroup(
                providerGroupName, email, phoneNumber, npiNumber, subDomain,
                "", "", city, zipCode, state
        );

        String expectedError = "Line 1 is required";
        String actualError = providerGroupPage.getAddressError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 10, groups = {"regression"})
    @Description("Verify error message when SubDomain details are invalid")
    public void addProviderGroupWithInvalidSubdomain() {
        providerGroupPage.addProviderGroup(providerGroupName, email, phoneNumber, npiNumber, "ABC",
                addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Subdomain must only contain lowercase letters, numbers, and hyphens, and must not start or end with a hyphen.";
        String actualError = providerGroupPage.getInvalidSubdomain();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 11, groups = {"regression"})
    @Description("Verify error message when Subdomain is missing")
    public void addProviderGroupWithEmptySubdomain() {
        providerGroupPage.addProviderGroup(providerGroupName, email, phoneNumber, npiNumber, "",
                addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Sub domain field is required";
        String actualError = providerGroupPage.getEmptySubdomain();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }
}
