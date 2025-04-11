package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
    }

    @Step("Logging out of the application")
    public void logout() {
            //waitForProgressBarToAppear();
            waitForElementToBeClickable(profileIcon).click();
            waitForElementToBeClickable(logoutLink).click();
            waitForElementToBeClickable(confirmLogoutButton).click();
    }

    @Step("Verifying logout by checking Login button visibility")
    public boolean verifyLogout() {
            return waitForElementToBeVisible(loginButton).isDisplayed();
    }
}
