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
import utilities.LoginUtils;

public class TC001_SuperAdminLogin extends BaseClass {

    private final ThreadLocal<SuperAdminLogin> loginPage = new ThreadLocal<>();
    private final ThreadLocal<String> validUsername = new ThreadLocal<>();
    private final ThreadLocal<String> validPassword = new ThreadLocal<>();

    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and fetch credentials from properties file.")
    public void setUp() {
        loginPage.set(new SuperAdminLogin());
        validUsername.set(properties.getProperty("Username"));
        validPassword.set(properties.getProperty("Password"));
        Assert.assertNotNull(validUsername.get(), "Username is not set in the properties file.");
        Assert.assertNotNull(validPassword.get(), "Password is not set in the properties file.");
    }

    @Test(priority = 1, groups = { "smoke", "regression" })
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify SuperAdmin can log in with valid credentials")
    public void superAdminLoginWithValidCredentials() {
        loginPage.get().login(validUsername.get(), validPassword.get());
        String expectedText = "Provider Groups";
        Assert.assertEquals(loginPage.get().getProviderGroupsText(), expectedText,
                "Login failed: Expected text does not match.");
    }

    @Test(priority = 2, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login fails with an incorrect username")
    public void loginWithInvalidUsername() {
        String invalidUsername = "invalidUser";
        loginPage.get().login(invalidUsername, validPassword.get());
        Assert.assertEquals(loginPage.get().getInvalidEmailErrorMessage(), ErrorMessages.LOGIN_EMAIL_INVALID,
                "Error message does not match for invalid username.");
    }

    @Test(priority = 3, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login fails with an incorrect password")
    public void loginWithInvalidPassword() {
        String invalidPassword = "WrongPass123";
        loginPage.get().login(validUsername.get(), invalidPassword);
        Assert.assertEquals(loginPage.get().getInvalidPasswordErrorMessage(), ErrorMessages.LOGIN_PASSWORD_INVALID,
                "Error message does not match for invalid password.");
    }
}
