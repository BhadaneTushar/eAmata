package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utilities.Constants;
import utilities.ElementActions;
import utilities.LoggerUtils;

/**
 * Page object for the Location management page
 * Handles all interactions with the Location UI elements
 *
 * Flow:
 * - Called by `TC004_AddLocation` tests.
 * - High-level actions (`addLocation`) call BasePage helpers -> ElementActions -> WebDriver; getters return values to tests.
 *
 * Data:
 * - Inputs: location name/phone/email/address/city/zip/state provided by tests/data generator.
 * - Outputs: success/error message strings and boolean flags consumed by tests.
 */
public class LocationPage extends BasePage {

    // Constants
    private static final String STATE_LIST_XPATH = Constants.DROPDOWN_OPTIONS_XPATH;

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

    /**
     * Constructor for LocationPage
     */
    public LocationPage() {
        super();
        LoggerUtils.debug("Initialized LocationPage");
    }

    /**
     * Navigate to the provider group details page
     */
    @Step("Navigating to Provider Group details")
    public void navigateToProviderGroup() {
        LoggerUtils.info("Navigating to Provider Group details");
        waitForProgressBarToAppear();
        clickButton(providerGroupLink);
    }

    /**
     * Navigate to the location tab
     */
    @Step("Navigating to Location tab")
    public void navigateToLocationTab() {
        LoggerUtils.info("Navigating to Location tab");
        waitForProgressBarToAppear();
        clickButton(locationTabButton);
    }

    /**
     * Click the Add Location button
     */
    @Step("Clicking Add Location button")
    public void clickAddLocation() {
        LoggerUtils.info("Clicking Add Location button");
        clickButton(addLocationButton);
    }

    /**
     * Fill in all location details
     * 
     * @param name         Location name
     * @param phoneNumber  Location phone number
     * @param email        Location email
     * @param addressLine1 Address line 1
     * @param addressLine2 Address line 2
     * @param city         City
     * @param zipCode      Zip code
     * @param state        State
     */
    @Step("Filling location details: {0}")
    private void fillLocationDetails(String name, String phoneNumber, String email,
                                    String addressLine1, String addressLine2, String city, String zipCode, String state) {
        LoggerUtils.info("Filling location details for: " + name);
        setInputField(nameInputField, name);
        setInputField(phoneNumberInputField, phoneNumber);
        setInputField(emailInputField, email);
        setInputField(addressLine1InputField, addressLine1);
        setInputField(addressLine2InputField, addressLine2);
        setInputField(cityInputField, city);
        setInputField(zipCodeInputField, zipCode);
        selectDropdownByVisibleText(stateDropdownButton, state, STATE_LIST_XPATH);
    }

    /**
     * Add a new location with all details
     * 
     * @param name         Location name
     * @param phoneNumber  Location phone number
     * @param email        Location email
     * @param addressLine1 Address line 1
     * @param addressLine2 Address line 2
     * @param city         City
     * @param zipCode      Zip code
     * @param state        State
     * Caller: `TC004_AddLocation` tests; Callee chain: this -> BasePage helpers -> ElementActions -> WebDriver.
     */
    @Step("Adding new location: {0}")
    public void addLocation(String name, String phoneNumber, String email,
                           String addressLine1, String addressLine2, String city, String zipCode, String state) {
        LoggerUtils.info("Adding new location: " + name);
        navigateToProviderGroup();
        navigateToLocationTab();
        clickAddLocation();
        fillLocationDetails(name, phoneNumber, email, addressLine1, addressLine2, city, zipCode, state);
        saveLocation();
    }

    /**
     * Save the location by clicking the save button
     */
    @Step("Saving location")
    public void saveLocation() {
        LoggerUtils.info("Saving location");
        clickButton(saveButton);
    }

    /**
     * Get the success message text
     * 
     * @return Success message text
     */
    @Step("Getting success message")
    public String getSuccessMessage() {
        LoggerUtils.info("Getting success message");
        return getText(waitForElementToBeVisible(successMessage));
    }

    /**
     * Get the name required error message
     * 
     * @return Name required error message
     */
    @Step("Getting name required error message")
    public String getNameRequiredError() {
        LoggerUtils.info("Getting name required error message");
        return getText(waitForElementToBeVisible(nameRequiredError));
    }

    /**
     * Get the invalid phone number error message
     * 
     * @return Invalid phone number error message
     */
    @Step("Getting invalid phone number error message")
    public String getInvalidPhoneNumberError() {
        LoggerUtils.info("Getting invalid phone number error message");
        try {
            return getText(waitForElementToBeVisible(invalidPhoneNumberError));
        } catch (Exception e) {
            LoggerUtils.error("Failed to get invalid phone number error message: " + e.getMessage());
            throw new RuntimeException("Failed to get invalid phone number error message", e);
        }
    }

    /**
     * Get the invalid phone number format error message
     * 
     * @return Invalid phone number format error message
     */
    @Step("Getting invalid phone number format error message")
    public String getInvalidPhoneNumberFormatError() {
        LoggerUtils.info("Getting invalid phone number format error message");
        return getText(waitForElementToBeVisible(invalidPhoneNumberFormatError));
    }

    /**
     * Get the email required error message
     * 
     * @return Email required error message
     */
    @Step("Getting email required error message")
    public String getEmailRequiredError() {
        LoggerUtils.info("Getting email required error message");
        return getText(waitForElementToBeVisible(emailRequiredError));
    }

    /**
     * Get the invalid email format error message
     * 
     * @return Invalid email format error message
     */
    @Step("Getting invalid email format error message")
    public String getInvalidEmailFormatError() {
        LoggerUtils.info("Getting invalid email format error message");
        return getText(waitForElementToBeVisible(invalidEmailFormatError));
    }

    /**
     * Get the zip code required error message
     * 
     * @return Zip code required error message
     */
    @Step("Getting zip code required error message")
    public String getZipCodeRequiredError() {
        LoggerUtils.info("Getting zip code required error message");
        return getText(waitForElementToBeVisible(zipCodeRequiredError));
    }
    
    /**
     * Check if location was added successfully
     * 
     * @return True if success message is displayed
     */
    @Step("Checking if location was added successfully")
    public boolean isLocationAddedSuccessfully() {
        return isElementDisplayed(successMessage);
    }
}
