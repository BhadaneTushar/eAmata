package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.Logout;
import pageObject.SuperAdminLogin;
import testBase.BaseClass;
import utilities.LoggerUtils;

/**
 * Test class for Super Admin login functionality.
 * Contains test cases for valid and invalid login scenarios.
 * 
 * Test Cases:
 * 1. Login with valid credentials
 * 2. Login with invalid username
 * 3. Login with invalid password
 * 4. Login with empty username
 * 5. Login with empty password
 */
public class TC001_SuperAdminLogin extends BaseClass {

    private SuperAdminLogin loginPage;
    private Logout logoutPage;
    private String validUsername;
    private String validPassword;

    /**
     * Sets up the test environment before each test method.
     * Initializes page objects and retrieves credentials from properties.
     * 
     * @throws RuntimeException if setup fails
     */
    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and fetch credentials from properties file")
    @Parameters("browser")
    public void initializeDriver(@Optional("chrome") String browser) {
        try {
            LoggerUtils.info("Setting up test environment for Super Admin login");
            super.initializeDriver(browser);
            loginPage = new SuperAdminLogin();
            logoutPage = new Logout();
            validUsername = properties.getProperty("Username");
            validPassword = properties.getProperty("Password");

            Assert.assertNotNull(validUsername, "Username is not set in the properties file");
            Assert.assertNotNull(validPassword, "Password is not set in the properties file");
            LoggerUtils.debug("Test setup completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Failed to setup test environment: " + e.getMessage());
            throw new RuntimeException("Failed to setup test environment", e);
        }
    }

    /**
     * Cleans up after each test method.
     * Performs logout and closes the browser.
     */
    @AfterMethod
    @Description("Cleanup after each test method")
    public void cleanup() {
        try {
            LoggerUtils.info("Cleaning up test environment");
            /*logoutPage.logout();
            LoggerUtils.info("Logout completed successfully");*/
        } catch (Exception e) {
            LoggerUtils.error("Failed to cleanup: " + e.getMessage());
            throw new RuntimeException("Failed to cleanup", e);
        }
    }

    /**
     * Tests successful login with valid credentials.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 1, groups = { "smoke", "regression" })
    @Description("Verify SuperAdmin can log in with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuperAdminLoginWithValidCredentials() {
        try {
            LoggerUtils.info("Starting test: SuperAdmin login with valid credentials");
            loginPage.login(validUsername, validPassword);

            String expectedText = "Provider Groups";
            String actualText = loginPage.getVerificationText();

            Assert.assertEquals(actualText, expectedText,
                    "Login failed: Expected text '" + expectedText + "' but found '" + actualText + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /*
     * @Test(priority = 2, groups = {"smoke", "regression"})
     * 
     * @Description("Verify SuperAdmin can log out successfully")
     * public void SuperAdminLogout() {
     * logoutPage.logout();
     * Assert.assertEquals(logoutPage.verifyLogout(), "Login",
     * "Logout test failed: Expected text not found.");
     * }
     */

    /**
     * Tests login with invalid username.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 2, groups = { "regression" })
    @Description("Verify login fails with an incorrect username")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithInvalidUsername() {
        try {
            LoggerUtils.info("Starting test: Login with invalid username");
            String invalidUsername = "invalidUser";
            loginPage.login(invalidUsername, validPassword);

            String expectedError = "Invalid email address";
            String actualError = loginPage.getInvalidUsernameErrorMessage();

            Assert.assertEquals(actualError, expectedError,
                    "Error message mismatch for invalid username. Expected: '" + expectedError +
                            "', Actual: '" + actualError + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests login with invalid password.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 3, groups = { "regression" })
    @Description("Verify login fails with an incorrect password")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithInvalidPassword() {
        try {
            LoggerUtils.info("Starting test: Login with invalid password");
            String invalidPassword = "WrongPass123";
            loginPage.login(validUsername, invalidPassword);

            String expectedError = "Password must be 8+ characters, with at least one uppercase, " +
                    "one lowercase, one number, and one special character. No spaces.";
            String actualError = loginPage.getInvalidPasswordErrorMessage();

            Assert.assertEquals(actualError, expectedError,
                    "Error message mismatch for invalid password. Expected: '" + expectedError +
                            "', Actual: '" + actualError + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests login with empty username.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 4, groups = { "regression" })
    @Description("Verify login fails with empty username")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithEmptyUsername() {
        try {
            LoggerUtils.info("Starting test: Login with empty username");
            loginPage.login("", validPassword);

            String expectedError = "Invalid email address";
            String actualError = loginPage.getInvalidUsernameErrorMessage();

            Assert.assertEquals(actualError, expectedError,
                    "Error message mismatch for empty username. Expected: '" + expectedError +
                            "', Actual: '" + actualError + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Tests login with empty password.
     * 
     * @throws RuntimeException if test fails
     */
    @Test(priority = 5, groups = { "regression" })
    @Description("Verify login fails with empty password")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithEmptyPassword() {
        try {
            LoggerUtils.info("Starting test: Login with empty password");
            loginPage.login(validUsername, "");

            String expectedError = "Password must be 8+ characters, with at least one uppercase, " +
                    "one lowercase, one number, and one special character. No spaces.";
            String actualError = loginPage.getInvalidPasswordErrorMessage();

            Assert.assertEquals(actualError, expectedError,
                    "Error message mismatch for empty password. Expected: '" + expectedError +
                            "', Actual: '" + actualError + "'");
            LoggerUtils.info("Test completed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Test failed: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }
}
