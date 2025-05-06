package pageObject;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.Address;

public class LocationPage extends BasePage {

    // Constants
    private static final String STATE_LIST_XPATH = "//ul[@role='listbox']/li";

    // Navigation Elements
    @FindBy(xpath = "//tbody/tr[1]/td[1]/div[1]/a[1]")
    private WebElement providerGroupLink;

    @FindBy(xpath = "//button[text()='Locations']")
    private WebElement locationTabButton;

    @FindBy(xpath = "//span[text()='Add Location']")
    private WebElement addLocationButton;

    // Input Fields
    @FindBy(xpath = "//input[@placeholder='Enter Name']")
    private WebElement nameInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Phone Number']")
    private WebElement phoneNumberInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Email']")
    private WebElement emailInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Address Line 1']")
    private WebElement addressLine1InputField;

    @FindBy(xpath = "//input[@placeholder='Enter Address Line 2']")
    private WebElement addressLine2InputField;

    @FindBy(xpath = "//input[@placeholder='Enter City']")
    private WebElement cityInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Zip Code']")
    private WebElement zipCodeInputField;

    // Dropdown Elements
    @FindBy(xpath = "//input[@placeholder='Select State']")
    private WebElement stateDropdownButton;

    @FindBy(xpath = "//li[text()='Arizona']")
    private WebElement stateName;

    // Action Buttons
    @FindBy(xpath = "(//span[text()='Add Location'])[2]")
    private WebElement saveButton;

    // Success Messages
    @FindBy(xpath = "//span[text()='Location added successfully!']")
    private WebElement successMessage;

    // Validation Messages
    @FindBy(xpath = "//span[text()='Name is mandatory']")
    private WebElement nameRequiredError;

    @FindBy(xpath = "//span[contains(text(),'Invalid phone number. Please use +91, +1, or +61 f')]")
    private WebElement invalidPhoneNumberError;

    @FindBy(xpath = "//span[text()='Invalid phone number']")
    private WebElement invalidPhoneNumberFormatError;

    @FindBy(xpath = "//span[text()='Email id is mandatory']")
    private WebElement emailRequiredError;

    @FindBy(xpath = "//span[text()='Invalid email format']")
    private WebElement invalidEmailFormatError;

    @FindBy(xpath = "//label[text()='Zip code is required']")
    private WebElement zipCodeRequiredError;

    public LocationPage(WebDriver driver) {
        super();
        PageFactory.initElements(driver, this);
    }

    // Navigation Methods
    public void navigateToProviderGroup() {
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeVisible(providerGroupLink));
    }

    public void navigateToLocationTab() {
        waitForProgressBarToAppear();
        ((JavascriptExecutor) BaseClass.getDriver()).executeScript("arguments[0].click();", locationTabButton);
    }

    public void clickAddLocation() {
        ((JavascriptExecutor) BaseClass.getDriver()).executeScript("arguments[0].click();", addLocationButton);
    }

    // Input Field Methods
    private void fillLocationDetails(String name, String phoneNumber, String email,
            String addressLine1, String addressLine2, String city, String zipCode, String state) {
        setInputField(nameInputField, name);
        setInputField(phoneNumberInputField, phoneNumber);
        setInputField(emailInputField, email);
        setInputField(addressLine1InputField, addressLine1);
        setInputField(addressLine2InputField, addressLine2);
        setInputField(cityInputField, city);
        setInputField(zipCodeInputField, zipCode);
        selectDropdownByVisibleText(stateDropdownButton, state, STATE_LIST_XPATH);
    }

    // Combined Action Methods
    public void addLocation(String name, String phoneNumber, String email,
            String addressLine1, String addressLine2, String city, String zipCode, String state) {
        navigateToProviderGroup();
        navigateToLocationTab();
        clickAddLocation();
        fillLocationDetails(name, phoneNumber, email, addressLine1, addressLine2, city, zipCode, state);
        saveLocation();
    }

    // Form Submission Methods
    public void saveLocation() {
        ((JavascriptExecutor) BaseClass.getDriver()).executeScript("arguments[0].click();", saveButton);
    }

    // Success Message Methods
    public String getSuccessMessage() {
        return successMessage.getText();
    }

    // Validation Message Methods
    public String getNameRequiredError() {
        return nameRequiredError.getText();
    }

    public String getInvalidPhoneNumberError() {
        return invalidPhoneNumberError.getText();
    }

    public String getInvalidPhoneNumberFormatError() {
        return invalidPhoneNumberFormatError.getText();
    }

    public String getEmailRequiredError() {
        return emailRequiredError.getText();
    }

    public String getInvalidEmailFormatError() {
        return invalidEmailFormatError.getText();
    }

    public String getZipCodeRequiredError() {
        return zipCodeRequiredError.getText();
    }
}
