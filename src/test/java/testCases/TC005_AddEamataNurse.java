package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.eAmataNursePage;
import testBase.BaseClass;
import utilities.TestDataGenerator;


public class TC005_AddEamataNurse extends BaseClass {

    private eAmataNursePage nursePage;
    private TestDataGenerator dataGenerator;
    private String validFirstName;
    private String validLastName;
    private String validEmail;
    private String validPhone;
    private String validNPI;
    private String validGender;
    private String validLicenseNumber;
    private String validState;
    private String validExpiryDate;


    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and generate test data.")
    public void setUp() {
        super.setUp();
        nursePage = new eAmataNursePage(getDriver());
        dataGenerator = new TestDataGenerator();

        validFirstName = dataGenerator.generateRandomFirstName();
        validLastName = dataGenerator.generateRandomLastName();
        validEmail = dataGenerator.generateRandomEmail();
        validPhone = dataGenerator.generatePhoneNumber();
        validNPI = dataGenerator.generateRandomNPI();
        validGender = properties.getProperty("Gender"); // Using a fixed gender for consistency
        validLicenseNumber = dataGenerator.generatePhoneNumber(); // Generate RN license number
        validState = properties.getProperty("State"); // Using a fixed state for consistency
        validExpiryDate = properties.getProperty("licenseExpiryDate");
    }

    @Test(priority = 1, groups = { "smoke", "regression" })
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a new eAmata nurse with valid data")
    public void testAddEamataNurse() {
        nursePage.navigateToAddNurseForm();
        nursePage.enterNurseDetails(validFirstName, validLastName, validEmail, validPhone, validNPI, validGender);
        nursePage.enterLicenseDetails(validLicenseNumber, validState, validExpiryDate);
        nursePage.saveNurse();
        Assert.assertEquals(nursePage.verifyNurseCreation(), "Nurse added successfully!",
                "Nurse was not added successfully.");
    }

    /*
     * @Test(priority = 2, groups = { "regression" })
     *
     * @Severity(SeverityLevel.NORMAL)
     *
     * @Description("Verify validation when first name is empty")
     * public void testEmptyFirstNameValidation() throws InterruptedException {
     * nursePage.navigateToAddNurseForm();
     * nursePage.enterNurseDetails("", validLastName, validEmail, validPhone,
     * validNPI, validGender);
     * nursePage.saveNurse();
     * // TODO: Add validation message assertion once error message elements are
     * added
     * // to page object
     * }
     *
     * @Test(priority = 3, groups = { "regression" })
     *
     * @Severity(SeverityLevel.NORMAL)
     *
     * @Description("Verify validation when last name is empty")
     * public void testEmptyLastNameValidation() throws InterruptedException {
     * nursePage.navigateToAddNurseForm();
     * nursePage.enterNurseDetails(validFirstName, "", validEmail, validPhone,
     * validNPI, validGender);
     * nursePage.saveNurse();
     * // TODO: Add validation message assertion once error message elements are
     * added
     * // to page object
     * }
     *
     * @Test(priority = 4, groups = { "regression" })
     *
     * @Severity(SeverityLevel.NORMAL)
     *
     * @Description("Verify validation when email is invalid")
     * public void testInvalidEmailValidation() throws InterruptedException {
     * nursePage.navigateToAddNurseForm();
     * nursePage.enterNurseDetails(validFirstName, validLastName, "invalid-email",
     * validPhone, validNPI, validGender);
     * nursePage.saveNurse();
     * // TODO: Add validation message assertion once error message elements are
     * added
     * // to page object
     * }
     *
     * @Test(priority = 5, groups = { "regression" })
     *
     * @Severity(SeverityLevel.NORMAL)
     *
     * @Description("Verify validation when phone number is invalid")
     * public void testInvalidPhoneValidation() throws InterruptedException {
     * nursePage.navigateToAddNurseForm();
     * nursePage.enterNurseDetails(validFirstName, validLastName, validEmail, "123",
     * validNPI, validGender);
     * nursePage.saveNurse();
     * // TODO: Add validation message assertion once error message elements are
     * added
     * // to page object
     * }
     */
}
