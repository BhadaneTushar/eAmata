package testCases;

import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.StaffPage;
import pageObject.SuperAdminLogin;
import testBase.BaseClass;
import utilities.TestDataGenerator;

public class TC003_AddStaff extends BaseClass {

    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String role;
    String gender;
    String addressLine1;
    String addressLine2;
    String state;
    String city;
    String zipCode;
    private StaffPage addStaff;

    @BeforeMethod
    public void setup() {
        SuperAdminLogin loginPage = new SuperAdminLogin();
        TestDataGenerator data = new TestDataGenerator();
        loginPage.login("superadminqa@eamata.com", "Eamata@123");
        addStaff = new StaffPage(getDriver());
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
    public void addStaffWithValidDetails() {
        addStaff.addStaff(firstName, lastName, email, phoneNumber, role, gender, addressLine1, addressLine2, city, zipCode, state);
        String expectedMessage = firstName + " " + lastName;
        String actualMessage = addStaff.verifySuccessMessage();

        Assert.assertEquals(actualMessage, expectedMessage, "Staff name does not match!");
    }

    @Test(priority = 2, groups = {"regression"})
    @Description("Verify error message when first name is empty")
    public void addStaffWithEmptyFirstName() {
        addStaff.addStaff("", lastName, email, phoneNumber, role, gender, addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "First Name is required";
        String actualError = addStaff.getFirstNameError();

        Assert.assertEquals(actualError, expectedError, "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 3, groups = {"regression"})
    @Description("Verify error message when last name is empty")
    public void addStaffWithEmptyLastName() {
        addStaff.addStaff(firstName, "", email, phoneNumber, role, gender, addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Last Name is required";
        String actualError = addStaff.getLastNameError();

        Assert.assertEquals(actualError, expectedError, "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 4, groups = {"regression"})
    @Description("Verify error message when email is empty")
    public void addStaffWithEmptyEmail() {
        addStaff.addStaff(firstName, lastName, "", phoneNumber, role, gender, addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Email is required";
        String actualError = addStaff.getEmptyGmailError();

        Assert.assertEquals(actualError, expectedError, "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 5, groups = {"regression"})
    @Description("Verify error message when phone number is empty")
    public void addStaffWithEmptyPhoneNumber() {
        addStaff.addStaff(firstName, lastName, email, "", role, gender, addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Phone is required";
        String actualError = addStaff.getEmptyPhoneError();

        Assert.assertEquals(actualError, expectedError, "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }

    @Test(priority = 6, groups = {"regression"})
    @Description("Verify error message when phone number is invalid")
    public void addStaffWithInvalidPhoneNumber() {
        addStaff.addStaff(firstName, lastName, email, "12345", role, gender, addressLine1, addressLine2, city, zipCode, state);

        String expectedError = "Invalid phone number. It must be 10 digits.";
        String actualError = addStaff.getPhoneNumberError();

        Assert.assertEquals(actualError, expectedError, "Validation failed! Expected: '" + expectedError + "', but got: '" + actualError + "'");
    }
}
