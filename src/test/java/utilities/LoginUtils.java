package utilities;

import org.testng.Assert;
import pageObject.SuperAdminLogin;
import testBase.BaseClass;

public class LoginUtils {

    public static void loginAsSuperAdmin() {
        SuperAdminLogin loginPage = new SuperAdminLogin();
        String validUsername = BaseClass.properties.getProperty("Username");
        String validPassword = BaseClass.properties.getProperty("Password");
        Assert.assertNotNull(validUsername, "Username is not set in the properties file.");
        Assert.assertNotNull(validPassword, "Password is not set in the properties file.");
        loginPage.login(validUsername, validPassword);
    }
}