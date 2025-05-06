package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.StaffPage;
import testBase.BaseClass;
import utilities.TestDataGenerator;


public class TC003_AddStaff extends BaseClass {

    private StaffPage staffPage;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private String gender;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zipCode;


    @BeforeMethod
    public void setup() {
        TestDataGenerator data = new TestDataGenerator();
        staffPage = new StaffPage(getDriver());
        firstName = data.generateRandomFirstName();
        lastName = data.generateRandomLastName();
        email = data.generateEmail("staff");
        phoneNumber = data.generatePhoneNumber();
        role = properties.getProperty("StaffRole");
        gender = properties.getProperty("Gender");
        addressLine1 = data.generateAddressLine1();
        addressLine2 = data.generateAddressLine2();
        state = properties.getProperty("State");
        city = data.generateCity();
        zipCode = data.generateZipCode();
    }

    @Test(priority = 1, groups = {"smoke", "regression"})
    @Description("Verify SuperAdmin can successfully add a staff member")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddStaffWithValidDetails() {

        staffPage.addStaff(firstName, lastName, email, phoneNumber, role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        String expectedMessage = "User added successfully!";
        //User added successfully!
        String actualMessage = staffPage.getStaffVerificationText();
        Assert.assertEquals(actualMessage, expectedMessage,
                "Staff creation failed: Expected: '" + expectedMessage +
                        "', Actual: '" + actualMessage + "'");

    }


    @Test(priority = 2, groups = {"regression"})
    @Description("Verify error message when first name is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithEmptyFirstName() {

        staffPage.addStaff("", lastName, email, phoneNumber, role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "First Name is required";
        String actualError = staffPage.getFirstNameRequiredError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError +
                        "', Actual: '" + actualError + "'");


    }


    @Test(priority = 3, groups = {"regression"})
    @Description("Verify error message when last name is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithEmptyLastName() {

        staffPage.addStaff(firstName, "", email, phoneNumber, role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Last Name is required";
        String actualError = staffPage.getLastNameRequiredError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError +
                        "', Actual: '" + actualError + "'");

    }


    @Test(priority = 4, groups = {"regression"})
    @Description("Verify error message when email is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithEmptyEmail() {

        staffPage.addStaff(firstName, lastName, "", phoneNumber, role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Email is required";
        String actualError = staffPage.getEmailRequiredError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError +
                        "', Actual: '" + actualError + "'");


    }


    @Test(priority = 5, groups = {"regression"})
    @Description("Verify error message when phone number is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithEmptyPhoneNumber() {


        staffPage.addStaff(firstName, lastName, email, "", role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Phone is required";
        String actualError = staffPage.getPhoneRequiredError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError +
                        "', Actual: '" + actualError + "'");


    }


    @Test(priority = 6, groups = {"regression"})
    @Description("Verify error message when phone number is invalid")
    @Severity(SeverityLevel.NORMAL)
    public void testAddStaffWithInvalidPhoneNumber() {

        staffPage.addStaff(firstName, lastName, email, "12345", role, gender,
                addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Invalid phone number. It must be 10 digits.";
        String actualError = staffPage.getInvalidPhoneNumberError();

        Assert.assertEquals(actualError, expectedError,
                "Validation failed! Expected: '" + expectedError +
                        "', Actual: '" + actualError + "'");

    }
}
