package testCases;

import com.aventstack.extentreports.Status;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.SuperAdminLogin;
import testBase.BaseClass;
import utilities.*;

/**
 * Test class for Super Admin login functionality
 */
public class TC001_SuperAdminLogin extends BaseClass {

    private final ThreadLocal<SuperAdminLogin> loginPage = new ThreadLocal<>();
    private final ThreadLocal<String> validUsername = new ThreadLocal<>();
    private final ThreadLocal<String> validPassword = new ThreadLocal<>();

    /**
     * Set up test data and page objects before each test method
     */
    @BeforeMethod
    @Description("Setup WebDriver, initialize Page Objects, and fetch credentials from properties file.")
    public void setUp() {
        loginPage.set(new SuperAdminLogin());
        validUsername.set(getConfigManager().getUsername());
        validPassword.set(getConfigManager().getPassword());
        
        LoggerUtils.info("Test setup complete with username: " + validUsername.get());
        
        // Log step to both Allure and Extent reports
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), Status.INFO, "Test setup complete with credentials");
        }
        
        AssertionUtils.assertNotNull(validUsername.get(), "Username is not set in the properties file.");
        AssertionUtils.assertNotNull(validPassword.get(), "Password is not set in the properties file.");
    }

    /**
     * Test valid login scenario - Critical smoke test
     */
    @Test(priority = 1, groups = {"smoke", "regression", "authentication", "critical"}, retryAnalyzer = utilities.RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify SuperAdmin can log in with valid credentials")
    @Step("Testing login with valid credentials")
    public void superAdminLoginWithValidCredentials() {
        String testName = "TC001_SuperAdminLogin.superAdminLoginWithValidCredentials";
        PerformanceMonitor.startTest(testName);
        LoggerUtils.info("Testing login with valid credentials");
        
        // Log step to both Allure and Extent reports
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), Status.INFO, "Attempting login with valid credentials");
        }
        
        // Perform login
        loginPage.get().login(validUsername.get(), validPassword.get());
        
        // Log step to both Allure and Extent reports
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), Status.INFO, "Login attempt completed, verifying results");
        }
        
        // Verify login success
        String expectedText = "Provider Groups";
        AssertionUtils.assertEquals(
            loginPage.get().getProviderGroupsText(), 
            expectedText,
            "Login failed: Expected text does not match."
        );
        
        // Additional verification
        AssertionUtils.assertTrue(
            loginPage.get().isLoginSuccessful(),
            "Login was not successful"
        );
        
        // Capture screenshot on success
        if (TestListener.getExtentTest() != null) {
            ReportUtils.attachScreenshotToExtent(
                TestListener.getExtentTest(), 
                getDriver(), 
                Status.PASS, 
                "Login successful screenshot"
            );
        }
    }

    /**
     * Test invalid username scenario
     */
    @Test(priority = 2, groups = {"regression", "authentication", "negative"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login fails with an incorrect username")
    @Step("Testing login with invalid username")
    public void loginWithInvalidUsername() {
        LoggerUtils.info("Testing login with invalid username");
        
        // Log step to both Allure and Extent reports
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), Status.INFO, "Attempting login with invalid username");
        }
        
        // Perform login with invalid username
        String invalidUsername = "invalidUser";
        loginPage.get().login(invalidUsername, validPassword.get());
        
        // Log step to both Allure and Extent reports
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), Status.INFO, "Verifying error message for invalid username");
        }
        
        // Verify error message
        AssertionUtils.assertEquals(
            loginPage.get().getInvalidEmailErrorMessage(), 
            ErrorMessages.LOGIN_EMAIL_INVALID,
            "Error message does not match for invalid username."
        );
        
        // Capture screenshot of error message
        if (TestListener.getExtentTest() != null) {
            ReportUtils.attachScreenshotToExtent(
                TestListener.getExtentTest(), 
                getDriver(), 
                Status.PASS, 
                "Invalid username error message"
            );
        }
    }

    /**
     * Test invalid password scenario
     */
    @Test(priority = 3, groups = {"regression", "authentication", "negative"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login fails with an incorrect password")
    @Step("Testing login with invalid password")
    public void loginWithInvalidPassword() {
        LoggerUtils.info("Testing login with invalid password");
        
        // Log step to both Allure and Extent reports
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), Status.INFO, "Attempting login with invalid password");
        }
        
        // Perform login with invalid password
        String invalidPassword = "WrongPass123";
        loginPage.get().login(validUsername.get(), invalidPassword);
        
        // Log step to both Allure and Extent reports
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), Status.INFO, "Verifying error message for invalid password");
        }
        
        // Verify error message
        AssertionUtils.assertEquals(
            loginPage.get().getInvalidPasswordErrorMessage(), 
            ErrorMessages.LOGIN_PASSWORD_INVALID,
            "Error message does not match for invalid password."
        );
        
        // Capture screenshot of error message
        if (TestListener.getExtentTest() != null) {
            ReportUtils.attachScreenshotToExtent(
                TestListener.getExtentTest(), 
                getDriver(), 
                Status.PASS, 
                "Invalid password error message"
            );
        }
    }
}
