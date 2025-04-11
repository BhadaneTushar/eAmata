package testCases;

import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.eAmataNursePage;
import testBase.BaseClass;
import utilities.Address;
import utilities.TestDataGenerator;

public class TC005_AddEamataNurse extends BaseClass {

    private eAmataNursePage nursePage;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String NPI;
    private String gender;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zipCode;
    private String licenseNumber;
    private String licenseState;
    private String licenseExpiryDate;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        TestDataGenerator data = new TestDataGenerator();
        nursePage = new eAmataNursePage(getDriver());
        firstName = data.generateRandomFirstName();
        lastName = data.generateRandomLastName();
        email = data.generateEmail("nurse");
        phoneNumber = data.generatePhoneNumber();
        NPI = data.generateRandomNPI();
        gender = properties.getProperty("Gender");
        addressLine1 = data.generateAddressLine1();
        addressLine2 = data.generateAddressLine2();
        state = properties.getProperty("State");
        city = data.generateCity();
        zipCode = data.generateZipCode();
        licenseNumber = data.generateRandomNPI();
        licenseState = properties.getProperty("licenseState");
        licenseExpiryDate = properties.getProperty("licenseExpiryDate");
    }

    @Test(priority = 1, groups = {"smoke", "regression"})
    @Description("Verify SuperAdmin can successfully add eAmata Nurse")
    public void testAddEamataNurse() throws InterruptedException {

        nursePage.navigateToAddNurseForm();
        nursePage.enterNurseDetails(firstName, lastName, email, phoneNumber, NPI, gender);
        new Address(getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode,state);
        nursePage.enterLicenseDetails(licenseNumber, licenseState, licenseExpiryDate);
        nursePage.saveNurse();

        String expectedMessage = "Nurse added successfully!";
        String actualMessage = nursePage.verifyNurseCreation();

        Assert.assertEquals(actualMessage, expectedMessage, "Nurse creation failed: Name mismatch.");
    }
}
