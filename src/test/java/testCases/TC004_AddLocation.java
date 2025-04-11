package testCases;

import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.LocationPage;
import testBase.BaseClass;
import utilities.TestDataGenerator;

public class TC004_AddLocation extends BaseClass {

    private LocationPage addLocationPage;
    private String locationName;
    private String phoneNumber;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zipCode;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        TestDataGenerator data = new TestDataGenerator();
        addLocationPage = new LocationPage(getDriver());
        locationName = data.generateCity();
        phoneNumber = data.generatePhoneNumber();
        email = data.generateRandomEmail();
        addressLine1 = data.generateAddressLine1();
        addressLine2 = data.generateAddressLine2();
        state = properties.getProperty("State");
        city = data.generateCity();
        zipCode = data.generateZipCode();
    }

    @Test(priority = 1, groups = {"smoke", "regression"})
    @Description("Verify SuperAdmin can successfully add a location")
    public void addLocationWithValidDetails() {
        addLocationPage.addLocation(locationName, phoneNumber, email, addressLine1, addressLine2, city, zipCode, state);
        String expectedMessage = "Location added successfully!";
        String actualMessage = addLocationPage.verificationMessage();
        Assert.assertEquals(actualMessage, expectedMessage, "The location was not added successfully.");
    }

    @Test(priority = 2, groups = {"regression"})
    @Description("Verify error message when location name is empty")
    public void addLocationWithEmptyName() {
        addLocationPage.addLocation("", phoneNumber, email, addressLine1, addressLine2, city, zipCode, state);
        Assert.assertEquals(addLocationPage.getLocationNameError(), "Name is mandatory");
    }

    @Test(priority = 3, groups = {"regression"})
    @Description("Verify error message when phone number is empty")
    public void addLocationWithEmptyPhoneNumber() {
        addLocationPage.addLocation(locationName, "", email, addressLine1, addressLine2, city, zipCode, state);
        Assert.assertEquals(addLocationPage.getPhoneNumberError(), "Invalid phone number. Please use +91, +1, or +61 followed by 10 to 11 digits, allowing hyphens.");
    }

    @Test(priority = 4, groups = {"regression"})
    @Description("Verify error message when email is empty")
    public void addLocationWithEmptyEmail() {
        addLocationPage.addLocation(locationName, phoneNumber, "", addressLine1, addressLine2, city, zipCode, state);
        Assert.assertEquals(addLocationPage.getEmptyEmailError(), "Email id is mandatory");
    }

    @Test(priority = 5, groups = {"regression"})
    @Description("Verify error message when email format is invalid")
    public void addLocationWithInvalidEmail() {
        addLocationPage.addLocation(locationName, phoneNumber, "invalid-email", addressLine1, addressLine2, city, zipCode, state);
        Assert.assertEquals(addLocationPage.getInvalidEmailError(), "Invalid email format");
    }

    @Test(priority = 6, groups = {"regression"})
    @Description("Verify error message when ZIP code is empty")
    public void addLocationWithEmptyZipCode() {
        addLocationPage.addLocation(locationName, phoneNumber, email, addressLine1, addressLine2, city, "", state);
        Assert.assertEquals(addLocationPage.getZipCodeError(), "Zip code is required");
    }
}
