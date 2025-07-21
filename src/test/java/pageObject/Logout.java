package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utilities.LoggerUtils;

/**
 * Page object for the Logout functionality
 * Handles user logout actions and verifications
 */
public class Logout extends BasePage {

    // Profile and Logout Elements
    @FindBy(xpath = "//div[@class='MuiAvatar-root MuiAvatar-circular MuiAvatar-colorDefault css-mln75l']//*[name()='svg']")
    private WebElement profileIcon;

    @FindBy(xpath = "//p[text()='Logout']")
    private WebElement logoutLink;

    @FindBy(xpath = "//p[text()='Yes']")
    private WebElement confirmLogoutButton;

    // Login Page Elements
    @FindBy(xpath = "//button[text()='Login']")
    private WebElement loginButton;

    /**
     * Constructor for Logout page
     */
    public Logout() {
        super();
        LoggerUtils.debug("Initialized Logout page");
    }

    /**
     * Perform logout action
     * Clicks on profile icon, logout link, and confirms logout
     */
    @Step("Logging out of the application")
    public void logout() {
        LoggerUtils.info("Performing logout action");
        try {
            clickButton(profileIcon);
            clickButton(logoutLink);
            clickButton(confirmLogoutButton);
            LoggerUtils.info("Successfully logged out");
        } catch (Exception e) {
            LoggerUtils.error("Failed to logout: " + e.getMessage());
            throw new RuntimeException("Failed to logout", e);
        }
    }

    /**
     * Verify successful logout by checking if login button is displayed
     * 
     * @return true if logout was successful, false otherwise
     */
    @Step("Verifying logout by checking Login button visibility")
    public boolean verifyLogout() {
        LoggerUtils.info("Verifying logout was successful");
        try {
            return isElementDisplayed(loginButton);
        } catch (Exception e) {
            LoggerUtils.error("Failed to verify logout: " + e.getMessage());
            throw new RuntimeException("Failed to verify logout", e);
        }
    }
}
