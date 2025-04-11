package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SuperAdminLogin extends BasePage {

    @FindBy(xpath = "//input[@placeholder='Enter Your Email']")
    private WebElement txtUsername;

    @FindBy(xpath = "//input[@placeholder='Enter your Password']")
    private WebElement txtPassword;

    @FindBy(xpath = "//button[text()='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//span[contains(@class, 'MuiTypography-bodySmall') and normalize-space()='Provider Groups']")
    private WebElement verificationText;

    //ToDO Need to Update xpath
    @FindBy(xpath = "//span[text()='Invalid email address']")
    private WebElement invalidUsernameText;

    @FindBy(xpath = "//span[contains(text(),'Password must be 8+ characters, with at least one ')]")
    private WebElement invalidPasswordText;

    public SuperAdminLogin() {
        super();
    }

    @Step("Logging in with username: {0} and password: {1}")
    public void login(String username, String password) {
        setInputField(txtUsername, username);
        setInputField(txtPassword, password);
        clickButton(loginButton);
    }

    @Step("Verifying login success message")
    public String getVerificationText() {
        return waitForElementToBeVisible(verificationText).getText();
    }

    @Step("Checking error message for invalid username")
    public String getInvalidUsernameErrorMessage() {
        return waitForElementToBeVisible(invalidUsernameText).getText();
    }

    @Step("Checking error message for invalid password")
    public String getInvalidPasswordErrorMessage() {
        return waitForElementToBeVisible(invalidPasswordText).getText();
    }
}
