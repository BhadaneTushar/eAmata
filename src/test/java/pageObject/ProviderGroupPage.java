package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.Address;
import utilities.LoggerUtils;

/**
 * Page Object for the Provider Group management page.
 * Contains all elements and actions related to provider group operations.
 */
public class ProviderGroupPage extends BasePage {

    // Form elements
    @FindBy(xpath = "//button[.//span[text()='New Provider Group']]")
    private WebElement newProviderGroupButton;

    @FindBy(xpath = "//input[@type='radio' and @value='manualEntry']")
    private WebElement manualEntryRadioButton;

    @FindBy(xpath = "//input[@name='name' or @placeholder='Enter Name']")
    private WebElement providerGroupNameInputField;

    @FindBy(xpath = "//input[@name='email' or @placeholder='Enter Email']")
    private WebElement providerGroupEmailInputField;

    @FindBy(xpath = "//input[@name='phoneNumber' or @placeholder='Enter Phone Number']")
    private WebElement providerGroupPhoneNumberInputField;

    @FindBy(xpath = "//input[@name='npiNumber' or @placeholder='Enter NPI Number']")
    private WebElement providerGroupNpiNumberInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Sub Domain']")
    private WebElement providerGroupSubDomainInputField;

    @FindBy(xpath = "//button[@type='submit' or contains(@class, 'submit')]")
    private WebElement formSubmitButton;

    // Success message
    @FindBy(xpath = "//div[contains(@class, 'success')]//span[contains(text(), 'Provider group')]")
    private WebElement providerGroupUpdateSuccessMessage;

    // Edit button
    @FindBy(xpath = "//button[contains(@aria-label, 'edit') or .//span[text()='Edit']]")
    private WebElement editProviderGroupButton;

    // Error messages
    @FindBy(xpath = "//label[contains(text(), 'Name') and contains(text(), 'required')]")
    private WebElement providerGroupNameError;

    @FindBy(xpath = "//label[contains(text(), 'Invalid email')]")
    private WebElement invalidEmail;

    @FindBy(xpath = "//label[contains(text(), 'Invalid phone number')]")
    private WebElement invalidPhone;

    @FindBy(xpath = "//label[contains(text(), 'Must be 10 digits') or contains(text(), 'Invalid NPI')]")
    private WebElement invalidNPI;

    @FindBy(xpath = "//label[contains(text(),'Subdomain must only contain lowercase letters, num')]")
    private WebElement invalidSubdomain;

    @FindBy(xpath = "//label[contains(text(), 'Line 1') and contains(text(), 'required')]")
    private WebElement addressError;

    @FindBy(xpath = "//label[contains(text(), 'Phone') and contains(text(), 'required')]")
    private WebElement emptyPhone;

    @FindBy(xpath = "//label[contains(text(), 'NPI') and contains(text(), 'required')]")
    private WebElement emptyNPI;

    @FindBy(xpath = "//label[contains(text(), 'Email') and contains(text(), 'required')]")
    private WebElement emptyEmail;

    @FindBy(xpath = "//label[contains(text(), 'Sub domain') and contains(text(), 'required')]")
    private WebElement emptySubDomain;

    public ProviderGroupPage(WebDriver driver) {
        super();
        PageFactory.initElements(driver, this);
        LoggerUtils.debug("Initialized ProviderGroupPage");
    }

    /**
     * Fills in the provider group details form.
     * 
     * @param name      The provider group name
     * @param email     The provider group email
     * @param phone     The provider group phone number
     * @param npi       The provider group NPI number
     * @param subDomain The provider group subdomain
     */
    private void fillProviderGroupDetails(String name, String email, String phone, String npi, String subDomain) {
        LoggerUtils.debug("Filling provider group details");
        setInputField(providerGroupNameInputField, name);
        setInputField(providerGroupEmailInputField, email);
        setInputField(providerGroupPhoneNumberInputField, phone);
        setInputField(providerGroupNpiNumberInputField, npi);
        setInputField(providerGroupSubDomainInputField, subDomain);
    }

    /**
     * Adds a new provider group with the specified details.
     * 
     * @param name         The provider group name
     * @param email        The provider group email
     * @param phone        The provider group phone number
     * @param npi          The provider group NPI number
     * @param subDomain    The provider group subdomain
     * @param addressLine1 The first line of the address
     * @param addressLine2 The second line of the address
     * @param city         The city
     * @param zipCode      The ZIP code
     * @param state        The state
     */
    @Step("Adding new provider group: {0}")
    public void addProviderGroup(String name, String email, String phone, String npi, String subDomain,
            String addressLine1, String addressLine2, String city, String zipCode, String state) {
        LoggerUtils.info("Starting to add new provider group: " + name);
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeClickable(newProviderGroupButton));

        if (!manualEntryRadioButton.isSelected()) {
            clickButton(waitForElementToBeClickable(manualEntryRadioButton));
        }
        fillProviderGroupDetails(name, email, phone, npi, subDomain);
        new Address(BaseClass.getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
        clickButton(formSubmitButton);
        waitForProgressBarToAppear();
        LoggerUtils.info("Provider group form submitted");
    }

    /**
     * Edits a provider group with the specified details.
     * 
     * @param name         The provider group name
     * @param email        The provider group email
     * @param phone        The provider group phone number
     * @param npi          The provider group NPI number
     * @param subDomain    The provider group subdomain
     * @param addressLine1 The first line of the address
     * @param addressLine2 The second line of the address
     * @param city         The city
     * @param zipCode      The ZIP code
     * @param state        The state
     */
    @Step("Editing provider group: {0}")
    public void editProviderGroup(String name, String email, String phone, String npi, String subDomain,
            String addressLine1, String addressLine2, String city, String zipCode, String state) {
        LoggerUtils.info("Starting to edit provider group: " + name);
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeClickable(editProviderGroupButton));
        fillProviderGroupDetails(name, email, phone, npi, subDomain);
        new Address(BaseClass.getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
        clickButton(formSubmitButton);
        waitForProgressBarToAppear();
        LoggerUtils.info("Provider group form submitted");
    }

    /**
     * Gets the success message after provider group update.
     * 
     * @return The success message text
     */
    @Step("Getting provider group update success message")
    public String verificationMessage() {
        String message = waitForElementToBeVisible(providerGroupUpdateSuccessMessage).getText();
        LoggerUtils.debug("Success message: " + message);
        return message;
    }

    /**
     * Gets the success message after provider group creation.
     * 
     * @return The success message text
     */
    @Step("Getting provider group creation success message")
    public String getSuccessMessage() {
        String message = waitForElementToBeVisible(providerGroupUpdateSuccessMessage).getText();
        LoggerUtils.debug("Success message: " + message);
        return message;
    }

    /**
     * Gets the error message for invalid email format.
     * 
     * @return The error message text
     */
    @Step("Getting invalid email error message")
    public String getInvalidEmailError() {
        String error = waitForElementToBeVisible(invalidEmail).getText();
        LoggerUtils.debug("Invalid email error message: " + error);
        return error;
    }

    /**
     * Gets the error message for phone number validation.
     * 
     * @return The error message text
     */
    @Step("Getting phone number error message")
    public String getPhoneNumberError() {
        String error = waitForElementToBeVisible(invalidPhone).getText();
        LoggerUtils.debug("Phone number error message: " + error);
        return error;
    }

    /**
     * Gets the error message for NPI validation.
     * 
     * @return The error message text
     */
    @Step("Getting NPI error message")
    public String getNPIError() {
        String error = waitForElementToBeVisible(invalidNPI).getText();
        LoggerUtils.debug("NPI error message: " + error);
        return error;
    }

    /**
     * Gets the error message for subdomain validation.
     * 
     * @return The error message text
     */
    @Step("Getting subdomain error message")
    public String getSubdomainError() {
        String error = waitForElementToBeVisible(invalidSubdomain).getText();
        LoggerUtils.debug("Subdomain error message: " + error);
        return error;
    }

    /**
     * Gets the error message for provider group name validation.
     * 
     * @return The error message text
     */
    @Step("Getting provider group name error message")
    public String getProviderGroupNameError() {
        String error = waitForElementToBeVisible(providerGroupNameError).getText();
        LoggerUtils.debug("Provider group name error message: " + error);
        return error;
    }

    /**
     * Gets the error message for empty email.
     * 
     * @return The error message text
     */
    @Step("Getting empty email error message")
    public String getEmptyEmailError() {
        String error = waitForElementToBeVisible(emptyEmail).getText();
        LoggerUtils.debug("Empty email error message: " + error);
        return error;
    }

    /**
     * Gets the error message for empty NPI.
     * 
     * @return The error message text
     */
    @Step("Getting empty NPI error message")
    public String getEmptyNPIError() {
        String error = waitForElementToBeVisible(emptyNPI).getText();
        LoggerUtils.debug("Empty NPI error message: " + error);
        return error;
    }

    /**
     * Gets the error message for empty phone.
     * 
     * @return The error message text
     */
    @Step("Getting empty phone error message")
    public String getEmptyPhoneError() {
        String error = waitForElementToBeVisible(emptyPhone).getText();
        LoggerUtils.debug("Empty phone error message: " + error);
        return error;
    }

    /**
     * Gets the error message for address validation.
     * 
     * @return The error message text
     */
    @Step("Getting address error message")
    public String getAddressError() {
        String error = waitForElementToBeVisible(addressError).getText();
        LoggerUtils.debug("Address error message: " + error);
        return error;
    }

    /**
     * Gets the error message for empty subdomain.
     * 
     * @return The error message text
     */
    @Step("Getting empty subdomain error message")
    public String getEmptySubdomain() {
        String error = waitForElementToBeVisible(emptySubDomain).getText();
        LoggerUtils.debug("Empty subdomain error message: " + error);
        return error;
    }

    /**
     * Archives a provider group.
     * Implementation pending.
     */
    @Step("Archiving provider group")
    public void archiveProviderGroup() {
        LoggerUtils.info("Archiving provider group");
        // Implementation for archiving a provider group
    }

    /**
     * Toggles the status of a provider group (active/inactive).
     * Implementation pending.
     */
    @Step("Toggling provider group status")
    public void toggleProviderGroupStatus() {
        LoggerUtils.info("Toggling provider group status");
        // Implementation for activating/deactivating a provider group
    }
}
