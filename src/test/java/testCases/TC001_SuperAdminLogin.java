package testCases;

import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.Logout;
import pageObject.SuperAdminLogin;
import testBase.BaseClass;

public class TC001_SuperAdminLogin extends BaseClass {

    private SuperAdminLogin loginPage;
    private Logout logoutPage;
    private String validUsername;
    private String validPassword;

    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and fetch credentials from properties file.")
    public void setUp() {

        loginPage = new SuperAdminLogin();
        logoutPage = new Logout();
        validUsername = properties.getProperty("Username");
        validPassword = properties.getProperty("Password");
        Assert.assertNotNull(validUsername, "Username is not set in the properties file.");
        Assert.assertNotNull(validPassword, "Password is not set in the properties file.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"})
    @Description("Verify SuperAdmin can log in with valid credentials")
    public void SuperAdminLoginWithValidCredentials() {
        loginPage.login(validUsername, validPassword);

        String expectedText = "Provider Groups";

        Assert.assertEquals(loginPage.getVerificationText(), expectedText, "Login failed: Expected text does not match.");
    }

  /*  @Test(priority = 2, groups = {"smoke", "regression"})
    @Description("Verify SuperAdmin can log out successfully")
    public void SuperAdminLogout() {
        logoutPage.logout();
        Assert.assertEquals(logoutPage.verifyLogout(), "Login", "Logout test failed: Expected text not found.");
    }*/

    @Test(priority = 2, groups = {"regression"})
    @Description("Verify login fails with an incorrect username")
    public void loginWithInvalidUsername() {
        String invalidUsername = "invalidUser";
        loginPage.login(invalidUsername, validPassword);

        String expectedError = "Invalid email address";

        Assert.assertEquals(loginPage.getInvalidUsernameErrorMessage(), expectedError, "Error message does not match for invalid username.");
    }

    @Test(priority = 3, groups = {"regression"})
    @Description("Verify login fails with an incorrect password")
    public void loginWithInvalidPassword() {
        String invalidPassword = "WrongPass123";
        loginPage.login(validUsername, invalidPassword);

        String expectedError = "Password must be 8+ characters, with at least one uppercase, one lowercase, one number, and one special character. No spaces.";

        Assert.assertEquals(loginPage.getInvalidPasswordErrorMessage(), expectedError, "Error message does not match for invalid password.");
    }
}
