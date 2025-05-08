package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.eAmataNursePage;
import testBase.BaseClass;
import utilities.Address;
import utilities.LoginUtils;
import utilities.TestDataGenerator;

public class TC005_AddEamataNurse extends BaseClass {
    private final ThreadLocal<eAmataNursePage> nursePage = new ThreadLocal<>();
    private TestDataGenerator dataGenerator;

    @BeforeMethod
    @Description("Setup test prerequisites")
    public void setUp() {
        LoginUtils.loginAsSuperAdmin();
        nursePage.set(new eAmataNursePage(getDriver()));
        dataGenerator = new TestDataGenerator();
    }

    @Test(priority = 1, groups = { "smoke", "regression" })
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a new eAmata nurse")
    public void addEamataNurse() {
        String firstName = "Test";
        String lastName = "Nurse";
        String email = "testnurse@example.com";
        String phone = "1234567890";
        String npi = "1234567890";
        String gender = "Male";
        String licenseNumber = "RN123456";
        String licenseState = "Arizona";
        String licenseExpiryDate = "12/31/2025";

        // Generate address details
        String addressLine1 = dataGenerator.generateAddressLine1();
        String addressLine2 = dataGenerator.generateAddressLine2();
        String city = dataGenerator.generateCity();
        String zipCode = dataGenerator.generateZipCode();
        String state = properties.getProperty("State");

        nursePage.get().navigateToAddNurseForm();
        nursePage.get().enterNurseDetails(firstName, lastName, email, phone, npi, gender);
        nursePage.get().enterLicenseDetails(licenseNumber, licenseState, licenseExpiryDate);

        // Enter address details using Address utility
        new Address(getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);

        nursePage.get().saveNurse();

        Assert.assertEquals(nursePage.get().verifyNurseCreation(), "Nurse added successfully!",
                "Nurse was not added successfully");
    }
}
