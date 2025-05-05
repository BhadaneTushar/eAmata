package pageObject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.Address;

public class ProviderGroupPage extends BasePage {

    // Navigation Elements
    @FindBy(xpath = "//span[text()='New Provider Group']")
    private WebElement newProviderGroupButton;

    @FindBy(xpath = "//input[@value='manualEntry']")
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

    public ProviderGroupPage() {
        super();
        PageFactory.initElements(getDriver(), this);
    }

    // Navigation Methods
    public void clickNewProviderGroup() {
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeClickable(newProviderGroupButton));
    }

    public void selectManualEntry() {
        if (!manualEntryRadioButton.isSelected()) {
            clickButton(waitForElementToBeClickable(manualEntryRadioButton));
        }
    }

    // Input Field Methods
    private void fillProviderGroupDetails(String name, String email, String phone, String npi, String subDomain) {
        setInputField(nameInputField, name);
        setInputField(emailInputField, email);
        setInputField(phoneNumberInputField, phone);
        setInputField(npiNumberInputField, npi);
        setInputField(subDomainInputField, subDomain);
    }

    // Combined Action Methods
    public void addProviderGroup(String name, String email, String phone, String npi, String subDomain,
            String addressLine1, String addressLine2, String city, String zipCode, String state) {
        clickNewProviderGroup();
        selectManualEntry();
        fillProviderGroupDetails(name, email, phone, npi, subDomain);
        new Address(BaseClass.getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
        submitForm();
    }

    public void editProviderGroup(String newName) {
        clickButton(waitForElementToBeVisible(editProviderGroupButton));
        setInputField(nameInputField, newName);
        submitForm();
    }

    // Form Submission Methods
    public void submitForm() {
        waitForElementToBeClickable(submitButton).click();
    }

    // Success Message Methods
    public String getSuccessMessage() {
        waitForProgressBarToAppear();
        return waitForElementToBeVisible(successMessage).getText();
    }

    // Validation Message Methods
    public String getNameRequiredError() {
        return waitForElementToBeVisible(nameRequiredError).getText();
    }

    public String getInvalidEmailError() {
        return waitForElementToBeVisible(invalidEmailError).getText();
    }

    public String getInvalidPhoneNumberError() {
        return waitForElementToBeVisible(invalidPhoneNumberError).getText();
    }

    public String getInvalidNpiNumberError() {
        return waitForElementToBeVisible(invalidNpiNumberError).getText();
    }

    public String getInvalidSubDomainError() {
        return waitForElementToBeVisible(invalidSubDomainError).getText();
    }

    public String getSubDomainRequiredError() {
        return waitForElementToBeVisible(subDomainRequiredError).getText();
    }

    public String getAddressRequiredError() {
        return waitForElementToBeVisible(addressRequiredError).getText();
    }

    public String getPhoneRequiredError() {
        return waitForElementToBeVisible(phoneRequiredError).getText();
    }

    public String getNpiRequiredError() {
        return waitForElementToBeVisible(npiRequiredError).getText();
    }

    public String getEmailRequiredError() {
        return waitForElementToBeVisible(emailRequiredError).getText();
    }

    // Status Management Methods
    public void archiveProviderGroup() {
        // Implementation for archiving a provider group
    }

    public void toggleProviderGroupStatus() {
        // Implementation for activating/deactivating a provider group
    }
}
