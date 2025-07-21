package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utilities.LoggerUtils;

/**
 * Page object for the Super Admin Login page
 */
public class SuperAdminLogin extends BasePage {

    // Input Fields
    @FindBy(xpath = "//input[@placeholder='Enter Your Email']")
    private WebElement emailInputField;

    @FindBy(xpath = "//input[@placeholder='Enter your Password']")
    private WebElement passwordInputField;

    // Action Buttons
    @FindBy(xpath = "//button[text()='Login']")
    private WebElement loginButton;

    // Success Messages
    @FindBy(xpath = "//span[contains(@class, 'MuiTypography-bodySmall') and normalize-space()='Provider Groups']")
    private WebElement providerGroupsText;

    // Validation Messages
    @FindBy(xpath = "//label[text()='Invalid email address']")
    private WebElement invalidEmailErrorMessage;

    @FindBy(xpath = "//label[contains(text(),'Password must be 8+ characters, with at least one ')]")
    private WebElement invalidPasswordErrorMessage;

    /**
     * Constructor
     */
    public SuperAdminLogin() {
        super();
        LoggerUtils.debug("Initialized SuperAdminLogin page");
    }

    /**
     * Enter email in the email input field
     * @param email Email to enter
     */
    @Step("Entering email: {0}")
    public void enterEmail(String email) {
        LoggerUtils.info("Entering email: " + email);
        setInputField(emailInputField, email);
    }

    /**
     * Enter password in the password input field
     * @param password Password to enter
     */
    @Step("Entering password: {0}")
    public void enterPassword(String password) {
        LoggerUtils.info("Entering password: " + password);
        setInputField(passwordInputField, password);
    }

    /**
     * Click the login button
     */
    @Step("Clicking login button")
    public void clickLoginButton() {
        LoggerUtils.info("Clicking login button");
        clickButton(loginButton);
    }

    /**
     * Login with email and password
     * @param email Email to enter
     * @param password Password to enter
     */
    @Step("Logging in with email: {0} and password: {1}")
    public void login(String email, String password) {
        LoggerUtils.info("Logging in with email: " + email);
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Get the provider groups text
     * @return Provider groups text
     */
    @Step("Verifying login success message")
    public String getProviderGroupsText() {
        LoggerUtils.info("Getting provider groups text");
        return getText(waitForElementToBeVisible(providerGroupsText));
    }

    /**
     * Get the invalid email error message
     * @return Invalid email error message
     */
    @Step("Checking error message for invalid email")
    public String getInvalidEmailErrorMessage() {
        LoggerUtils.info("Getting invalid email error message");
        return getText(waitForElementToBeVisible(invalidEmailErrorMessage));
    }

    /**
     * Get the invalid password error message
     * @return Invalid password error message
     */
    @Step("Checking error message for invalid password")
    public String getInvalidPasswordErrorMessage() {
        LoggerUtils.info("Getting invalid password error message");
        return getText(waitForElementToBeVisible(invalidPasswordErrorMessage));
    }
    
    /**
     * Check if login is successful
     * @return true if login is successful, false otherwise
     */
    @Step("Checking if login is successful")
    public boolean isLoginSuccessful() {
        return isElementDisplayed(providerGroupsText);
    }
}
