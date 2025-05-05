package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utilities.LoggerUtils;

/**
 * Page Object for the Logout functionality.
 * Contains all elements and actions related to user logout.
 */
public class Logout extends BasePage {

    @FindBy(xpath = "//div[@class='MuiAvatar-root MuiAvatar-circular MuiAvatar-colorDefault css-mln75l']//*[name()='svg']")
    private WebElement profileIcon;

    @FindBy(xpath = "//p[text()='Logout']")
    private WebElement logoutLink;

    @FindBy(xpath = "//p[text()='Yes']")
    private WebElement confirmLogoutButton;

    @FindBy(xpath = "//button[text()='Login']")
    private WebElement loginButton;

    public Logout() {
        super();
        LoggerUtils.debug("Initialized Logout page");
    }

    /**
     * Performs logout operation.
     * 
     * @throws RuntimeException if logout fails
     */
    @Step("Logging out of the application")
    public void logout() {
        try {
            waitForElementToBeClickable(profileIcon).click();
            waitForElementToBeClickable(logoutLink).click();
            waitForElementToBeClickable(confirmLogoutButton).click();
            LoggerUtils.info("Successfully logged out");
        } catch (Exception e) {
            LoggerUtils.error("Failed to logout: " + e.getMessage());
            throw new RuntimeException("Failed to logout", e);
        }
    }

    /**
     * Verifies logout by checking Login button visibility.
     * 
     * @return true if logout is successful
     * @throws RuntimeException if verification fails
     */
    @Step("Verifying logout by checking Login button visibility")
    public boolean verifyLogout() {
        try {
            return waitForElementToBeVisible(loginButton).isDisplayed();
        } catch (Exception e) {
            LoggerUtils.error("Failed to verify logout: " + e.getMessage());
            throw new RuntimeException("Failed to verify logout", e);
        }
    }
}
