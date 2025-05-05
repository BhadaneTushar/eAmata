package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.eAmataNursePage;
import testBase.BaseClass;
import utilities.ErrorMessages;

public class TC005_AddEamataNurse extends BaseClass {

    private eAmataNursePage nursePage;
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
    @Description("Setup WebDriver, initialize Page Objects, and fetch test data from properties file.")
    public void setUp() {
        nursePage = new eAmataNursePage(getDriver());
        validFirstName = properties.getProperty("NurseFirstName");
        validLastName = properties.getProperty("NurseLastName");
        validEmail = properties.getProperty("NurseEmail");
        validPhone = properties.getProperty("NursePhone");
        validNPI = properties.getProperty("NurseNPI");
        validGender = properties.getProperty("NurseGender");
        validLicenseNumber = properties.getProperty("NurseLicenseNumber");
        validState = properties.getProperty("NurseState");
        validExpiryDate = properties.getProperty("NurseLicenseExpiryDate");
        Assert.assertNotNull(validFirstName, "Nurse First Name is not set in the properties file.");
        Assert.assertNotNull(validLastName, "Nurse Last Name is not set in the properties file.");
        Assert.assertNotNull(validEmail, "Nurse Email is not set in the properties file.");
        Assert.assertNotNull(validPhone, "Nurse Phone is not set in the properties file.");
        Assert.assertNotNull(validNPI, "Nurse NPI is not set in the properties file.");
        Assert.assertNotNull(validGender, "Nurse Gender is not set in the properties file.");
        Assert.assertNotNull(validLicenseNumber, "Nurse License Number is not set in the properties file.");
        Assert.assertNotNull(validState, "Nurse State is not set in the properties file.");
        Assert.assertNotNull(validExpiryDate, "Nurse License Expiry Date is not set in the properties file.");
    }

    @Test(priority = 1, groups = { "smoke", "regression" })
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a new eAmata nurse with valid data")
    public void testAddEamataNurse() throws InterruptedException {
        nursePage.navigateToAddNurseForm();
        nursePage.enterNurseDetails(validFirstName, validLastName, validEmail, validPhone, validNPI, validGender);
        nursePage.enterLicenseDetails(validLicenseNumber, validState, validExpiryDate);
        nursePage.saveNurse();
        Assert.assertEquals(nursePage.verifyNurseCreation(), "Nurse added successfully!",
                "Nurse was not added successfully.");
    }

    /*@Test(priority = 2, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when first name is empty")
    public void testEmptyFirstNameValidation() throws InterruptedException {
        nursePage.navigateToAddNurseForm();
        nursePage.enterNurseDetails("", validLastName, validEmail, validPhone, validNPI, validGender);
        nursePage.saveNurse();
        // TODO: Add validation message assertion once error message elements are added
        // to page object
    }

    @Test(priority = 3, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when last name is empty")
    public void testEmptyLastNameValidation() throws InterruptedException {
        nursePage.navigateToAddNurseForm();
        nursePage.enterNurseDetails(validFirstName, "", validEmail, validPhone, validNPI, validGender);
        nursePage.saveNurse();
        // TODO: Add validation message assertion once error message elements are added
        // to page object
    }

    @Test(priority = 4, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when email is invalid")
    public void testInvalidEmailValidation() throws InterruptedException {
        nursePage.navigateToAddNurseForm();
        nursePage.enterNurseDetails(validFirstName, validLastName, "invalid-email", validPhone, validNPI, validGender);
        nursePage.saveNurse();
        // TODO: Add validation message assertion once error message elements are added
        // to page object
    }

    @Test(priority = 5, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when phone number is invalid")
    public void testInvalidPhoneValidation() throws InterruptedException {
        nursePage.navigateToAddNurseForm();
        nursePage.enterNurseDetails(validFirstName, validLastName, validEmail, "123", validNPI, validGender);
        nursePage.saveNurse();
        // TODO: Add validation message assertion once error message elements are added
        // to page object
    }*/
}
