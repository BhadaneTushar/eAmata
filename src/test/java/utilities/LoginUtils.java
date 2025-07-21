package utilities;

import io.qameta.allure.Step;
import org.testng.Assert;
import pageObject.SuperAdminLogin;
import testBase.BaseClass;

/**
 * Utility class for handling login operations
 */
public class LoginUtils {

    /**
     * Login as super admin using credentials from configuration
     */
    @Step("Logging in as Super Admin")
    public static void loginAsSuperAdmin() {
        LoggerUtils.info("Logging in as Super Admin");
        SuperAdminLogin loginPage = new SuperAdminLogin();
        
        // Get credentials from ConfigManager
        String validUsername = BaseClass.getConfigManager().getUsername();
        String validPassword = BaseClass.getConfigManager().getPassword();
        
        // Verify credentials are available
        AssertionUtils.assertNotNull(validUsername, "Username is not set in the properties file.");
        AssertionUtils.assertNotNull(validPassword, "Password is not set in the properties file.");
        
        // Perform login
        loginPage.login(validUsername, validPassword);
        LoggerUtils.info("Successfully logged in as Super Admin");
    }
}