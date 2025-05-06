package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.SuperAdminLogin;
import testBase.BaseClass;
import utilities.ErrorMessages;


public class TC001_SuperAdminLogin extends BaseClass {

    private SuperAdminLogin loginPage;
    private String validUsername;
    private String validPassword;


    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and fetch credentials from properties file.")
    public void setUp() {
        loginPage = new SuperAdminLogin();
        validUsername = properties.getProperty("Username");
        validPassword = properties.getProperty("Password");
        Assert.assertNotNull(validUsername, "Username is not set in the properties file.");
        Assert.assertNotNull(validPassword, "Password is not set in the properties file.");
    }

    @Test(priority = 1, groups = { "smoke", "regression" })
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify SuperAdmin can log in with valid credentials")
    public void superAdminLoginWithValidCredentials() {
        loginPage.login(validUsername, validPassword);
        String expectedText = "Provider Groups";
        Assert.assertEquals(loginPage.getProviderGroupsText(), expectedText,
                "Login failed: Expected text does not match.");
    }

    @Test(priority = 2, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login fails with an incorrect username")
    public void loginWithInvalidUsername() {
        String invalidUsername = "invalidUser";
        loginPage.login(invalidUsername, validPassword);
        Assert.assertEquals(loginPage.getInvalidEmailErrorMessage(), ErrorMessages.LOGIN_EMAIL_INVALID,
                "Error message does not match for invalid username.");
    }

    @Test(priority = 3, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login fails with an incorrect password")
    public void loginWithInvalidPassword() {
        String invalidPassword = "WrongPass123";
        loginPage.login(validUsername, invalidPassword);
        Assert.assertEquals(loginPage.getInvalidPasswordErrorMessage(), ErrorMessages.LOGIN_PASSWORD_INVALID,
                "Error message does not match for invalid password.");
    }
}
