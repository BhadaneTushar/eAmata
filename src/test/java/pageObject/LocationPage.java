package pageObject;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.Address;

public class LocationPage extends BasePage {

    private static final String STATE_LIST_XPATH = "//ul[@role='listbox']/li";

    @FindBy(xpath = "//tbody/tr[1]/td[1]/div[1]/a[1]")
    private WebElement providerGroupLinkButton;

    @FindBy(xpath = "//button[text()='Locations']")
    private WebElement locationTabButton;

    @FindBy(xpath = "//span[text()='Add Location']")
    private WebElement addLocationButton;

    @FindBy(xpath = "//input[@placeholder='Enter Name']")
    private WebElement locationNameInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Phone Number']")
    private WebElement locationPhoneNumberInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Email']")
    private WebElement locationEmailInputField;

    @FindBy(xpath = "(//span[text()='Add Location'])[2]")
    private WebElement saveLocationButton;

    @FindBy(xpath = "//span[text()='Location added successfully!']")
    private WebElement locationCreationSuccessMessage;

    @FindBy(xpath = "//span[text()='Name is mandatory']")
    private WebElement locationNameError;

    @FindBy(xpath = "//span[contains(text(),'Invalid phone number. Please use +91, +1, or +61 f')]")
    private WebElement phoneNumberError;

    @FindBy(xpath = "//span[text()='Invalid phone number']")
    private WebElement invalidPhoneNumberError;

    @FindBy(xpath = "//span[text()='Email id is mandatory']")
    private WebElement emptyEmailError;

    @FindBy(xpath = "//span[text()='Invalid email format']")
    private WebElement invalidEmailError;

    @FindBy(xpath = "//label[text()='Zip code is required']")
    private WebElement emptyZipCode;

    public LocationPage(WebDriver driver) {
        super();
        PageFactory.initElements(driver, this);
    }

    public void addLocation(String locationName, String phoneNumber, String email, String addressLine1, String addressLine2, String city, String zipCode, String state) {
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeVisible(providerGroupLinkButton));
        waitForProgressBarToAppear();
        ((JavascriptExecutor) BaseClass.getDriver()).executeScript("arguments[0].click();", locationTabButton);
        ((JavascriptExecutor) BaseClass.getDriver()).executeScript("arguments[0].click();", addLocationButton);

        setInputField(locationNameInputField, locationName);
        setInputField(locationPhoneNumberInputField, phoneNumber);
        setInputField(locationEmailInputField, email);
        new Address(getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
        ((JavascriptExecutor) BaseClass.getDriver()).executeScript("arguments[0].click();", saveLocationButton);
    }

    public String verificationMessage(){
        return locationCreationSuccessMessage.getText();
    }

    // Validation message methods

    public String getLocationNameError() {
        return locationNameError.getText();
    }

    public String getPhoneNumberError() {
        return phoneNumberError.getText();
    }

    public String getInvalidPhoneNumberError() {
        return invalidPhoneNumberError.getText();
    }

    public String getEmptyEmailError() {
        return emptyEmailError.getText();
    }

    public String getInvalidEmailError() {
        return invalidEmailError.getText();
    }

    public String getZipCodeError(){
        return emptyZipCode.getText();
    }
}
