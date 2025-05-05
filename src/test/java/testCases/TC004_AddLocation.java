package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.LocationPage;
import testBase.BaseClass;
import utilities.ErrorMessages;

public class TC004_AddLocation extends BaseClass {

    private LocationPage locationPage;
    private String validName;
    private String validPhone;
    private String validEmail;
    private String validAddressLine1;
    private String validAddressLine2;
    private String validCity;
    private String validZipCode;
    private String validState;

    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and fetch test data from properties file.")
    public void setUp() {
        locationPage = new LocationPage(getDriver());
        validName = properties.getProperty("LocationName");
        validPhone = properties.getProperty("LocationPhone");
        validEmail = properties.getProperty("LocationEmail");
        validAddressLine1 = properties.getProperty("LocationAddressLine1");
        validAddressLine2 = properties.getProperty("LocationAddressLine2");
        validCity = properties.getProperty("LocationCity");
        validZipCode = properties.getProperty("LocationZipCode");
        validState = properties.getProperty("LocationState");
        Assert.assertNotNull(validName, "Location Name is not set in the properties file.");
        Assert.assertNotNull(validPhone, "Location Phone is not set in the properties file.");
        Assert.assertNotNull(validEmail, "Location Email is not set in the properties file.");
        Assert.assertNotNull(validAddressLine1, "Location Address Line 1 is not set in the properties file.");
        Assert.assertNotNull(validCity, "Location City is not set in the properties file.");
        Assert.assertNotNull(validZipCode, "Location Zip Code is not set in the properties file.");
        Assert.assertNotNull(validState, "Location State is not set in the properties file.");
    }

    @Test(priority = 1, groups = { "smoke", "regression" })
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a new location with valid data")
    public void testAddLocation() {
        locationPage.addLocation(validName, validPhone, validEmail, validAddressLine1, validAddressLine2,
                validCity, validZipCode, validState);
        Assert.assertEquals(locationPage.getSuccessMessage(), "Location added successfully!",
                "Location was not added successfully.");
    }

    @Test(priority = 2, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when name is empty")
    public void testEmptyNameValidation() {
        locationPage.addLocation("", validPhone, validEmail, validAddressLine1, validAddressLine2,
                validCity, validZipCode, validState);
        Assert.assertEquals(locationPage.getNameRequiredError(), ErrorMessages.LOCATION_NAME_REQUIRED,
                "Name required error message does not match.");
    }

    @Test(priority = 3, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when phone number is invalid")
    public void testInvalidPhoneValidation() {
        locationPage.addLocation(validName, "123", validEmail, validAddressLine1, validAddressLine2,
                validCity, validZipCode, validState);
        Assert.assertEquals(locationPage.getInvalidPhoneNumberError(), ErrorMessages.LOCATION_PHONE_INVALID,
                "Invalid phone error message does not match.");
    }

    @Test(priority = 4, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when email is empty")
    public void testEmptyEmailValidation() {
        locationPage.addLocation(validName, validPhone, "", validAddressLine1, validAddressLine2,
                validCity, validZipCode, validState);
        Assert.assertEquals(locationPage.getEmailRequiredError(), ErrorMessages.LOCATION_EMAIL_REQUIRED,
                "Email required error message does not match.");
    }

    @Test(priority = 5, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when email is invalid")
    public void testInvalidEmailValidation() {
        locationPage.addLocation(validName, validPhone, "invalid-email", validAddressLine1, validAddressLine2,
                validCity, validZipCode, validState);
        Assert.assertEquals(locationPage.getInvalidEmailFormatError(), ErrorMessages.LOCATION_EMAIL_INVALID,
                "Invalid email error message does not match.");
    }

    @Test(priority = 6, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when zip code is empty")
    public void testEmptyZipCodeValidation() {
        locationPage.addLocation(validName, validPhone, validEmail, validAddressLine1, validAddressLine2,
                validCity, "", validState);
        Assert.assertEquals(locationPage.getZipCodeRequiredError(), ErrorMessages.ZIP_CODE_REQUIRED,
                "Zip code required error message does not match.");
    }
}
