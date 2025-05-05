package pageObject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.Address;

public class ProviderGroupPage extends BasePage {

    @FindBy(xpath = "//span[text()='New Provider Group']")
    private WebElement newProviderGroupButton;

    @FindBy(xpath = "//input[@value='manualEntry']")
    private WebElement manualEntryRadioButton;

    @FindBy(xpath = "//input[@placeholder='Enter Name']")
    private WebElement providerGroupNameInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Email']")
    private WebElement providerGroupEmailInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Phone Number']")
    private WebElement providerGroupPhoneNumberInputField;

    @FindBy(xpath = "//input[@placeholder='Enter NPI Number']")
    private WebElement providerGroupNpiNumberInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Sub Domain']")
    private WebElement providerGroupSubDomainInputField;

    @FindBy(xpath = "//span[text()='Provider group added successfully!']")
    private WebElement providerGroupCreationSuccessMessage;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement formSubmitButton;

    @FindBy(xpath = "(//button[@type='button' and @aria-label='edit'])[1]")
    private WebElement editProviderGroupButton;

    @FindBy(xpath = "//label[text()='Name is required']")
    private WebElement providerGroupNameError;

    @FindBy(xpath = "//label[text()='Invalid email address']")
    private WebElement invalidEmail;

    @FindBy(xpath = "//label[text()='Invalid phone number. It must be 10 digits.']")
    private WebElement invalidPhone;

    @FindBy(xpath = "//label[text()='Must be 10 digits']")
    private WebElement invalidNPI;

    @FindBy(xpath = "//label[contains(text(),'Subdomain must only contain lowercase letters, num')]")
    private WebElement invalidSubdomain;

    @FindBy(xpath = "//label[text()='Line 1 is required']")
    private WebElement addressError;

    @FindBy(xpath = "//label[text()='Phone is required']")
    private WebElement emptyphone;

    @FindBy(xpath = "//label[text()='NPI is required']")
    private WebElement emptypNPI;

    @FindBy(xpath = "//label[text()='Email is required']")
    private WebElement emptyGmail;

    @FindBy(xpath = "//label[text()='Sub domain field is required']")
    private WebElement emptySubDomain;

    public ProviderGroupPage() {
        super();
        PageFactory.initElements(getDriver(), this);
    }

    private void fillProviderGroupDetails(String name, String email, String phone, String npi, String subDomain) {
        setInputField(providerGroupNameInputField, name);
        setInputField(providerGroupEmailInputField, email);
        setInputField(providerGroupPhoneNumberInputField, phone);
        setInputField(providerGroupNpiNumberInputField, npi);
        setInputField(providerGroupSubDomainInputField, subDomain);
    }

    public void addProviderGroup(String name, String email, String phone, String npi, String subDomain,
                                 String addressLine1, String addressLine2, String city, String zipCode, String state) {
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeClickable(newProviderGroupButton));

        if (!manualEntryRadioButton.isSelected()) {
            clickButton(waitForElementToBeClickable(manualEntryRadioButton));
        }
        fillProviderGroupDetails(name, email, phone, npi, subDomain);
        new Address(BaseClass.getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
        waitForElementToBeClickable(formSubmitButton).click();
    }

    // Method to get success message
    public String getSuccessMessage() {
        waitForProgressBarToAppear();
        return waitForElementToBeVisible(providerGroupCreationSuccessMessage).getText();
    }

    // Methods to get individual error messages
    public String getProviderGroupNameError() {
        return waitForElementToBeVisible(providerGroupNameError).getText();
    }

    public String getInvalidEmail() {
        return waitForElementToBeVisible(invalidEmail).getText();
    }

    public String getPhoneError() {
        return waitForElementToBeVisible(invalidPhone).getText();
    }

    public String getInvalidNPI() {
        return waitForElementToBeVisible(invalidNPI).getText();
    }

    public String getInvalidSubdomain() {
        return waitForElementToBeVisible(invalidSubdomain).getText();
    }

    public String getEmptySubdomain() {
        return waitForElementToBeVisible(emptySubDomain).getText();
    }

    public String getAddressError() {
        return waitForElementToBeVisible(addressError).getText();
    }

    public String getEmptyPhoneError() {
        return waitForElementToBeVisible(emptyphone).getText();
    }

    public String getEmptyNPIError() {
        return waitForElementToBeVisible(emptypNPI).getText();
    }

    public String getEmptyGmailError() {
        return waitForElementToBeVisible(emptyGmail).getText();
    }

    public void editProviderGroup(String newName) {
        clickButton(waitForElementToBeVisible(editProviderGroupButton));
        setInputField(providerGroupNameInputField, newName);
        clickButton(formSubmitButton);
    }

    public void archiveProviderGroup() {
        // Implementation for archiving a provider group
    }

    public void toggleProviderGroupStatus() {
        // Implementation for activating/deactivating a provider group
    }
}
