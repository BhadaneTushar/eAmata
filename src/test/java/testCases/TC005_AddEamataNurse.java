package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.eAmataNursePage;
import testBase.BaseClass;
import utilities.Address;
import utilities.AssertionUtils;
import utilities.Constants;
import utilities.LoginUtils;
import utilities.LoggerUtils;
import utilities.TestDataGenerator;

/**
 * Test class for eAmata Nurse management functionality
 */
public class TC005_AddEamataNurse extends BaseClass {
    private final ThreadLocal<eAmataNursePage> nursePage = new ThreadLocal<>();
    private TestDataGenerator dataGenerator;

    /**
     * Set up test data and page objects before each test method
     */
    @BeforeMethod
    @Description("Setup test prerequisites")
    public void setUp() {
        LoggerUtils.info("Setting up eAmata nurse test data");
        
        // Login as super admin
        LoginUtils.loginAsSuperAdmin();
        
        // Initialize page objects and test data
        nursePage.set(new eAmataNursePage());
        dataGenerator = new TestDataGenerator();
        
        LoggerUtils.info("Test setup complete");
    }

    /**
     * Test adding a nurse with valid details
     */
    @Test(priority = 1, groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a new eAmata nurse")
    @Step("Testing add eAmata nurse with valid details")
    public void addEamataNurse() {
        LoggerUtils.info("Testing add eAmata nurse with valid details");
        
        // Generate nurse test data
        String firstName = dataGenerator.generateRandomFirstName();
        String lastName = dataGenerator.generateRandomLastName();
        String email = dataGenerator.generateEmail("nurse");
        String phone = dataGenerator.generatePhoneNumber();
        String npi = dataGenerator.generateRandomNPI();
        String gender = getConfigManager().getProperty(Constants.DEFAULT_GENDER, "Male");
        String licenseNumber = dataGenerator.generatePhoneNumber();
        String licenseState = getConfigManager().getProperty(Constants.DEFAULT_STATE, "Arizona");
        
        // Use current year + 1 month for license expiry to avoid date picker issues
        java.time.LocalDate currentDate = java.time.LocalDate.now();
        java.time.LocalDate expiryDate = currentDate.plusMonths(1);
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String licenseExpiryDate = expiryDate.format(formatter);
        
        LoggerUtils.info("Using license expiry date: " + licenseExpiryDate);

        // Generate address details
        String addressLine1 = dataGenerator.generateAddressLine1();
        String addressLine2 = dataGenerator.generateAddressLine2();
        String city = dataGenerator.generateCity();
        String zipCode = dataGenerator.generateZipCode();
        String state = getConfigManager().getProperty(Constants.DEFAULT_STATE, "Arizona");
        
        LoggerUtils.info("Adding nurse: " + firstName + " " + lastName);

        // Navigate to add nurse form and enter details
        nursePage.get().navigateToAddNurseForm();
        nursePage.get().enterNurseDetails(firstName, lastName, email, phone, npi, gender);
        nursePage.get().enterLicenseDetails(licenseNumber, licenseState, licenseExpiryDate);

        // Enter address details using Address utility
        new Address(getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);

        // Save nurse and verify
        nursePage.get().saveNurse();

        // Verify success message
        AssertionUtils.assertEquals(
            nursePage.get().verifyNurseCreation(), 
            "Nurse added successfully!",
            "Nurse was not added successfully"
        );
    }
}
