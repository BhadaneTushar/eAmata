package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
    @FindBy(xpath = "//span[text()='Invalid email address']")
    private WebElement invalidEmailErrorMessage;

    @FindBy(xpath = "//span[contains(text(),'Password must be 8+ characters, with at least one ')]")
    private WebElement invalidPasswordErrorMessage;

    public SuperAdminLogin() {
        super();
    }

    // Input Field Methods
    @Step("Entering email: {0}")
    public void enterEmail(String email) {
        setInputField(emailInputField, email);
    }

    @Step("Entering password: {0}")
    public void enterPassword(String password) {
        setInputField(passwordInputField, password);
    }

    // Button Methods
    @Step("Clicking login button")
    public void clickLoginButton() {
        clickButton(loginButton);
    }

    // Combined Action Methods
    @Step("Logging in with email: {0} and password: {1}")
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    // Success Message Methods
    @Step("Verifying login success message")
    public String getProviderGroupsText() {
        return waitForElementToBeVisible(providerGroupsText).getText();
    }

    // Validation Message Methods
    @Step("Checking error message for invalid email")
    public String getInvalidEmailErrorMessage() {
        return waitForElementToBeVisible(invalidEmailErrorMessage).getText();
    }

    @Step("Checking error message for invalid password")
    public String getInvalidPasswordErrorMessage() {
        return waitForElementToBeVisible(invalidPasswordErrorMessage).getText();
    }
}
