package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.LocationPage;
import testBase.BaseClass;
import utilities.ErrorMessages;
import utilities.LoginUtils;
import utilities.TestDataGenerator;

public class TC004_AddLocation extends BaseClass {

        private final ThreadLocal<LocationPage> locationPage = new ThreadLocal<>();
        private TestDataGenerator dataGenerator;
        private String validName;
        private String validPhone;
        private String validEmail;
        private String validAddressLine1;
        private String validAddressLine2;
        private String validCity;
        private String validZipCode;
        private String validState;

        @BeforeMethod
        @Description("Setup test prerequisites")
        public void setUp() {
                LoginUtils.loginAsSuperAdmin();
                locationPage.set(new LocationPage(getDriver()));
                dataGenerator = new TestDataGenerator();

                validName = dataGenerator.generateCompanyName();
                validPhone = dataGenerator.generatePhoneNumber();
                validEmail = dataGenerator.generateRandomEmail();
                validAddressLine1 = dataGenerator.generateAddressLine1();
                validAddressLine2 = dataGenerator.generateAddressLine2();
                validCity = dataGenerator.generateCity();
                validZipCode = dataGenerator.generateZipCode();
                validState = properties.getProperty("State");
        }

        @Test(priority = 1, groups = { "smoke", "regression" })
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify adding a new location with valid data")
        public void testAddLocation() {
                locationPage.get().addLocation(validName, validPhone, validEmail, validAddressLine1, validAddressLine2,
                                validCity, validZipCode, validState);
                Assert.assertEquals(locationPage.get().getSuccessMessage(), "Location added successfully!",
                                "Location was not added successfully.");
        }

        @Test(priority = 2, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when name is empty")
        public void testEmptyNameValidation() {
                locationPage.get().addLocation("", validPhone, validEmail, validAddressLine1, validAddressLine2,
                                validCity, validZipCode, validState);
                Assert.assertEquals(locationPage.get().getNameRequiredError(), ErrorMessages.LOCATION_NAME_REQUIRED,
                                "Name required error message does not match.");
        }

        @Test(priority = 3, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when phone number is invalid")
        public void testInvalidPhoneValidation() {
                locationPage.get().addLocation(validName, "123", validEmail, validAddressLine1, validAddressLine2,
                                validCity, validZipCode, validState);
                Assert.assertEquals(locationPage.get().getInvalidPhoneNumberError(),
                                ErrorMessages.LOCATION_PHONE_INVALID,
                                "Invalid phone error message does not match.");
        }

        @Test(priority = 4, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when email is empty")
        public void testEmptyEmailValidation() {
                locationPage.get().addLocation(validName, validPhone, "", validAddressLine1, validAddressLine2,
                                validCity, validZipCode, validState);
                Assert.assertEquals(locationPage.get().getEmailRequiredError(), ErrorMessages.LOCATION_EMAIL_REQUIRED,
                                "Email required error message does not match.");
        }

        @Test(priority = 5, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when email is invalid")
        public void testInvalidEmailValidation() {
                locationPage.get().addLocation(validName, validPhone, "invalid-email", validAddressLine1,
                                validAddressLine2,
                                validCity, validZipCode, validState);
                Assert.assertEquals(locationPage.get().getInvalidEmailFormatError(),
                                ErrorMessages.LOCATION_EMAIL_INVALID,
                                "Invalid email error message does not match.");
        }

        @Test(priority = 6, groups = { "regression" })
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify validation when zip code is empty")
        public void testEmptyZipCodeValidation() {
                locationPage.get().addLocation(validName, validPhone, validEmail, validAddressLine1, validAddressLine2,
                                validCity, "", validState);
                Assert.assertEquals(locationPage.get().getZipCodeRequiredError(), ErrorMessages.ZIP_CODE_REQUIRED,
                                "Zip code required error message does not match.");
        }
}
