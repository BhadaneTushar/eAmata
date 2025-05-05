package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utilities.LoggerUtils;

/**
 * Page Object for the Super Admin Login page.
 * Contains all elements and actions related to the login functionality.
 */
public class SuperAdminLogin extends BasePage {

    @FindBy(xpath = "//input[@placeholder='Enter Your Email']")
    private WebElement txtUsername;

    @FindBy(xpath = "//input[@placeholder='Enter your Password']")
    private WebElement txtPassword;

    @FindBy(xpath = "//button[text()='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//span[contains(@class, 'MuiTypography-bodySmall') and normalize-space()='Provider Groups']")
    private WebElement verificationText;

    // ToDO Need to Update xpath
    @FindBy(xpath = "//span[text()='Invalid email address']")
    private WebElement invalidUsernameText;

    @FindBy(xpath = "//span[contains(text(),'Password must be 8+ characters, with at least one ')]")
    private WebElement invalidPasswordText;

    public SuperAdminLogin() {
        super();
        LoggerUtils.debug("Initialized SuperAdminLogin page");
    }

    /**
     * Performs login with the provided credentials.
     * 
     * @param username The username to login with
     * @param password The password to login with
     * @throws RuntimeException if login fails
     */
    @Step("Logging in with username: {0}")
    public void login(String username, String password) {
        try {
            LoggerUtils.info("Attempting login with username: " + username);
            setInputField(txtUsername, username);
            setInputField(txtPassword, password);
            clickButton(loginButton);
            waitForProgressBarToAppear();
            LoggerUtils.info("Successfully logged in");
        } catch (Exception e) {
            LoggerUtils.error("Failed to login: " + e.getMessage());
            throw new RuntimeException("Failed to login", e);
        }
    }

    /**
     * Gets the verification text after successful login.
     * 
     * @return The verification text
     * @throws RuntimeException if getting verification text fails
     */
    @Step("Verifying login success message")
    public String getVerificationText() {
        try {
            String text = waitForElementToBeVisible(verificationText).getText();
            LoggerUtils.debug("Verification text found: " + text);
            return text;
        } catch (Exception e) {
            LoggerUtils.error("Failed to get verification text: " + e.getMessage());
            throw new RuntimeException("Failed to get verification text", e);
        }
    }

    /**
     * Gets the error message for invalid username.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Checking error message for invalid username")
    public String getInvalidUsernameErrorMessage() {
        try {
            String error = waitForElementToBeVisible(invalidUsernameText).getText();
            LoggerUtils.debug("Invalid username error message: " + error);
            return error;
        } catch (Exception e) {
            LoggerUtils.error("Failed to get invalid username error message: " + e.getMessage());
            throw new RuntimeException("Failed to get invalid username error message", e);
        }
    }

    /**
     * Gets the error message for invalid password.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Checking error message for invalid password")
    public String getInvalidPasswordErrorMessage() {
        try {
            String error = waitForElementToBeVisible(invalidPasswordText).getText();
            LoggerUtils.debug("Invalid password error message: " + error);
            return error;
        } catch (Exception e) {
            LoggerUtils.error("Failed to get invalid password error message: " + e.getMessage());
            throw new RuntimeException("Failed to get invalid password error message", e);
        }
    }
}
