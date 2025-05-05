package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.Address;
import utilities.LoggerUtils;

/**
 * Page Object for the Location page.
 * Contains all elements and actions related to location management.
 */
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
        LoggerUtils.debug("Initialized LocationPage");
    }

    /**
     * Adds a new location with the provided details.
     * 
     * @param locationName The name of the location
     * @param phoneNumber  The phone number of the location
     * @param email        The email of the location
     * @param addressLine1 The first line of the address
     * @param addressLine2 The second line of the address
     * @param city         The city of the location
     * @param zipCode      The ZIP code of the location
     * @param state        The state of the location
     * @throws RuntimeException if adding location fails
     */
    @Step("Adding new location")
    public void addLocation(String locationName, String phoneNumber, String email, String addressLine1,
            String addressLine2, String city, String zipCode, String state) {
        try {
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
            LoggerUtils.info("Successfully added location");
        } catch (Exception e) {
            LoggerUtils.error("Failed to add location: " + e.getMessage());
            throw new RuntimeException("Failed to add location", e);
        }
    }

    /**
     * Gets the verification message after location creation.
     * 
     * @return The verification message
     * @throws RuntimeException if getting verification message fails
     */
    @Step("Getting verification message")
    public String verificationMessage() {
        try {
            return locationCreationSuccessMessage.getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get verification message: " + e.getMessage());
            throw new RuntimeException("Failed to get verification message", e);
        }
    }

    /**
     * Gets the error message for location name.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting location name error message")
    public String getLocationNameError() {
        try {
            return locationNameError.getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get location name error message: " + e.getMessage());
            throw new RuntimeException("Failed to get location name error message", e);
        }
    }

    /**
     * Gets the error message for phone number.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting phone number error message")
    public String getPhoneNumberError() {
        try {
            return phoneNumberError.getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get phone number error message: " + e.getMessage());
            throw new RuntimeException("Failed to get phone number error message", e);
        }
    }

    /**
     * Gets the error message for invalid phone number.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting invalid phone number error message")
    public String getInvalidPhoneNumberError() {
        try {
            return invalidPhoneNumberError.getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get invalid phone number error message: " + e.getMessage());
            throw new RuntimeException("Failed to get invalid phone number error message", e);
        }
    }

    /**
     * Gets the error message for empty email.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting empty email error message")
    public String getEmptyEmailError() {
        try {
            return emptyEmailError.getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get empty email error message: " + e.getMessage());
            throw new RuntimeException("Failed to get empty email error message", e);
        }
    }

    /**
     * Gets the error message for invalid email.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting invalid email error message")
    public String getInvalidEmailError() {
        try {
            return invalidEmailError.getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get invalid email error message: " + e.getMessage());
            throw new RuntimeException("Failed to get invalid email error message", e);
        }
    }

    /**
     * Gets the error message for empty ZIP code.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting empty ZIP code error message")
    public String getZipCodeError() {
        try {
            return emptyZipCode.getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get empty ZIP code error message: " + e.getMessage());
            throw new RuntimeException("Failed to get empty ZIP code error message", e);
        }
    }
}
