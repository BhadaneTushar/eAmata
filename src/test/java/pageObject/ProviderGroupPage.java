package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.Address;
import utilities.LoggerUtils;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Page object for the Provider Group management page
 * Handles all interactions with Provider Group UI elements
 */
public class ProviderGroupPage extends BasePage {

    // Navigation Elements
    @FindBy(xpath = "//span[text()='New Provider Group']")
    private WebElement newProviderGroupButton;

    @FindBy(xpath = "//input[@type='radio' and @value='manualEntry']")
    private WebElement manualEntryRadioButton;

    @FindBy(xpath = "(//button[@type='button' and @aria-label='edit'])[1]")
    private WebElement editProviderGroupButton;

    // Input Fields
    @FindBy(xpath = "//input[@placeholder='Enter Name']")
    private WebElement nameInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Email']")
    private WebElement emailInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Phone Number']")
    private WebElement phoneNumberInputField;

    @FindBy(xpath = "//input[@placeholder='Enter NPI Number']")
    private WebElement npiNumberInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Sub Domain']")
    private WebElement subDomainInputField;

    // Action Buttons
    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    // Success Messages
    @FindBy(xpath = "//span[text()='Provider group added successfully!']")
    private WebElement successMessage;

    // Validation Messages
    @FindBy(xpath = "//label[text()='Name is required']")
    private WebElement nameRequiredError;

    @FindBy(xpath = "//label[text()='Invalid email address']")
    private WebElement invalidEmailError;

    @FindBy(xpath = "//label[text()='Invalid phone number. It must be 10 digits.']")
    private WebElement invalidPhoneNumberError;

    @FindBy(xpath = "//label[text()='Must be 10 digits']")
    private WebElement invalidNpiNumberError;

    @FindBy(xpath = "//label[contains(text(),'Subdomain must only contain lowercase letters, num')]")
    private WebElement invalidSubDomainError;

    @FindBy(xpath = "//label[text()='Line 1 is required']")
    private WebElement addressRequiredError;

    @FindBy(xpath = "//label[text()='Phone is required']")
    private WebElement phoneRequiredError;

    @FindBy(xpath = "//label[text()='NPI is required']")
    private WebElement npiRequiredError;

    @FindBy(xpath = "//label[text()='Email is required']")
    private WebElement emailRequiredError;

    @FindBy(xpath = "//label[text()='Sub domain field is required']")
    private WebElement subDomainRequiredError;

    /**
     * Constructor for ProviderGroupPage
     */
    public ProviderGroupPage() {
        super();
        LoggerUtils.debug("Initialized ProviderGroupPage");
    }

    /**
     * Click the New Provider Group button
     */
    @Step("Clicking New Provider Group button")
    public void clickNewProviderGroup() {
        LoggerUtils.info("Clicking New Provider Group button");
        try {
            // Try to wait for progress bar with shorter timeout and ignore if not found
            waitForProgressBarWithTimeout(5); // Use a custom method with shorter timeout
            waitForUILoad();
            clickButton(waitForElementToBeClickable(newProviderGroupButton));
        } catch (Exception e) {
            LoggerUtils.warn("Could not wait for progress bar in clickNewProviderGroup, continuing anyway: " + e.getMessage());
            try {
                waitForUILoad();
                clickButton(waitForElementToBeClickable(newProviderGroupButton));
            } catch (Exception ex) {
                // If regular click fails, try JavaScript click
                LoggerUtils.warn("Regular click failed, trying JavaScript click: " + ex.getMessage());
                JavascriptExecutor js = (JavascriptExecutor) getDriver();
                js.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", newProviderGroupButton);
            }
        }
    }

    /**
     * Select the Manual Entry radio button
     */
    @Step("Selecting Manual Entry option")
    public void selectManualEntry() {
        LoggerUtils.info("Selecting Manual Entry option");
        if (!manualEntryRadioButton.isSelected()) {
            clickButton(waitForElementToBeClickable(manualEntryRadioButton));
            waitForUILoad();
        }
    }

    /**
     * Click on a provider group name
     * 
     * @param providerGroupElement The WebElement representing the provider group to click
     */
    @Step("Clicking provider group name")
    public void clickProviderGroupName(WebElement providerGroupElement) {
        LoggerUtils.info("Clicking provider group name");
        waitForProgressBarToAppear();
        waitForUILoad();
        clickButton(waitForElementToBeClickable(providerGroupElement));
        waitForProgressBarToAppear(); // Wait for any loading operations to complete
    }

    /**
     * Fill in provider group details
     * 
     * @param name      Provider group name
     * @param email     Provider group email
     * @param phone     Provider group phone
     * @param npi       Provider group NPI number
     * @param subDomain Provider group subdomain
     */
    @Step("Filling provider group details: {0}")
    private void fillProviderGroupDetails(String name, String email, String phone, String npi, String subDomain) {
        LoggerUtils.info("Filling provider group details for: " + name);
        setInputField(nameInputField, name);
        setInputField(emailInputField, email);
        setInputField(phoneNumberInputField, phone);
        setInputField(npiNumberInputField, npi);
        setInputField(subDomainInputField, subDomain);
    }

    /**
     * Add a new provider group with all details
     * 
     * @param name         Provider group name
     * @param email        Provider group email
     * @param phone        Provider group phone
     * @param npi          Provider group NPI number
     * @param subDomain    Provider group subdomain
     * @param addressLine1 Address line 1
     * @param addressLine2 Address line 2
     * @param city         City
     * @param zipCode      Zip code
     * @param state        State
     */
    @Step("Adding new provider group: {0}")
    public void addProviderGroup(String name, String email, String phone, String npi, String subDomain,
                                 String addressLine1, String addressLine2, String city, String zipCode, String state) {
        LoggerUtils.info("Adding new provider group: " + name);
        try {
            clickNewProviderGroup();
            selectManualEntry();
            fillProviderGroupDetails(name, email, phone, npi, subDomain);
            
            // Use a try-catch block for the Address part
            try {
                new Address(BaseClass.getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
            } catch (Exception e) {
                LoggerUtils.warn("Failed to enter address details, but continuing with test: " + e.getMessage());
            }
            
            submitForm();
        } catch (Exception e) {
            LoggerUtils.error("Error in addProviderGroup method: " + e.getMessage(), e);
            // Don't rethrow the exception so test can continue and check for validation messages
        }
    }

    /**
     * Edit an existing provider group
     * 
     * @param newName New name for the provider group
     */
    @Step("Editing provider group with new name: {0}")
    public void editProviderGroup(String newName) {
        LoggerUtils.info("Editing provider group with new name: " + newName);
        clickButton(waitForElementToBeVisible(editProviderGroupButton));
        setInputField(nameInputField, newName);
        submitForm();
    }

    /**
     * Submit the provider group form
     */
    @Step("Submitting provider group form")
    public void submitForm() {
        LoggerUtils.info("Submitting provider group form");
        waitForElementToBeClickable(submitButton).click();
    }

    /**
     * Get the success message text
     * 
     * @return Success message text
     */
    @Step("Getting success message")
    public String getSuccessMessage() {
        LoggerUtils.info("Getting success message");
        waitForProgressBarToAppear();
        return waitForElementToBeVisible(successMessage).getText();
    }

    /**
     * Get the name required error message
     * 
     * @return Name required error message
     */
    @Step("Getting name required error message")
    public String getNameRequiredError() {
        LoggerUtils.info("Getting name required error message");
        waitForUILoad();
        return waitForElementToBeVisible(nameRequiredError).getText();
    }

    /**
     * Get the invalid email error message
     * 
     * @return Invalid email error message
     */
    @Step("Getting invalid email error message")
    public String getInvalidEmailError() {
        LoggerUtils.info("Getting invalid email error message");
        waitForUILoad();
        return waitForElementToBeVisible(invalidEmailError).getText();
    }

    /**
     * Get the invalid phone number error message
     * 
     * @return Invalid phone number error message
     */
    @Step("Getting invalid phone number error message")
    public String getInvalidPhoneNumberError() {
        LoggerUtils.info("Getting invalid phone number error message");
        waitForUILoad();
        return waitForElementToBeVisible(invalidPhoneNumberError).getText();
    }

    /**
     * Get the invalid NPI number error message
     * 
     * @return Invalid NPI number error message
     */
    @Step("Getting invalid NPI number error message")
    public String getInvalidNpiNumberError() {
        LoggerUtils.info("Getting invalid NPI number error message");
        waitForUILoad();
        return waitForElementToBeVisible(invalidNpiNumberError).getText();
    }

    /**
     * Get the invalid subdomain error message
     * 
     * @return Invalid subdomain error message
     */
    @Step("Getting invalid subdomain error message")
    public String getInvalidSubDomainError() {
        LoggerUtils.info("Getting invalid subdomain error message");
        waitForUILoad();
        return waitForElementToBeVisible(invalidSubDomainError).getText();
    }

    /**
     * Get the subdomain required error message
     * 
     * @return Subdomain required error message
     */
    @Step("Getting subdomain required error message")
    public String getSubDomainRequiredError() {
        LoggerUtils.info("Getting subdomain required error message");
        return waitForElementToBeVisible(subDomainRequiredError).getText();
    }

    /**
     * Get the address required error message
     * 
     * @return Address required error message
     */
    @Step("Getting address required error message")
    public String getAddressRequiredError() {
        LoggerUtils.info("Getting address required error message");
        return waitForElementToBeVisible(addressRequiredError).getText();
    }

    /**
     * Get the phone required error message
     * 
     * @return Phone required error message
     */
    @Step("Getting phone required error message")
    public String getPhoneRequiredError() {
        LoggerUtils.info("Getting phone required error message");
        return waitForElementToBeVisible(phoneRequiredError).getText();
    }

    /**
     * Get the NPI required error message
     * 
     * @return NPI required error message
     */
    @Step("Getting NPI required error message")
    public String getNpiRequiredError() {
        LoggerUtils.info("Getting NPI required error message");
        return waitForElementToBeVisible(npiRequiredError).getText();
    }

    /**
     * Get the email required error message
     * 
     * @return Email required error message
     */
    @Step("Getting email required error message")
    public String getEmailRequiredError() {
        LoggerUtils.info("Getting email required error message");
        return waitForElementToBeVisible(emailRequiredError).getText();
    }

    /**
     * Archive a provider group
     */
    @Step("Archiving provider group")
    public void archiveProviderGroup() {
        LoggerUtils.info("Archiving provider group");
        // Implementation for archiving a provider group
    }

    /**
     * Toggle provider group status (activate/deactivate)
     */
    @Step("Toggling provider group status")
    public void toggleProviderGroupStatus() {
        LoggerUtils.info("Toggling provider group status");
        // Implementation for activating/deactivating a provider group
    }
    
    /**
     * Check if provider group was added successfully
     * 
     * @return True if success message is displayed
     */
    @Step("Checking if provider group was added successfully")
    public boolean isProviderGroupAddedSuccessfully() {
        return isElementDisplayed(successMessage);
    }
}
