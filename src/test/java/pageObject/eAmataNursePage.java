package pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.DatePicker;

public class eAmataNursePage extends BasePage {

    private static final String GENDER_LIST_XPATH = "//ul[@role='listbox']/li";
    private static final String STATE_LIST_XPATH = "//ul[@role='listbox']/li";

    // Locators
    @FindBy(xpath = "//span[text()='Settings']")
    private WebElement settingsLink;

    @FindBy(xpath = "//button[text()='Admin Users']")
    private WebElement adminUsersTab;

    @FindBy(xpath = "//span[text()='Nurse']")
    private WebElement nurseButton;

    @FindBy(xpath = "//span[normalize-space()='Add Nurse']")
    private WebElement addNurseButton;

    @FindBy(xpath = "//input[@placeholder='Enter First Name']")
    private WebElement firstNameField;

    @FindBy(xpath = "//input[@placeholder='Enter Last Name']")
    private WebElement lastNameField;

    @FindBy(xpath = "//input[@placeholder='Enter Email']")
    private WebElement emailField;

    @FindBy(xpath = "//input[@placeholder='Enter Phone Number']")
    private WebElement phoneNumberField;

    @FindBy(xpath = "//button[@role='combobox']/span[text()='Select Gender']")
    private WebElement genderDropdown;

    @FindBy(xpath = "//input[@placeholder='Enter NPI Number']")
    private WebElement NPIField;

    @FindBy(xpath = "//input[@placeholder='Enter License Number']")
    private WebElement licensedNumberField;

    @FindBy(xpath = "(//input[@placeholder='Select State'])[2]")
    private WebElement licensedStateDropdown;

    @FindBy(xpath = "//button[@aria-label='Choose date']//*[name()='svg']")
    private WebElement licenseExpiryDateField;

    @FindBy(xpath = "//input[@placeholder='MM-DD-YYYY']")
    private WebElement licenseExpiryDateTextField;

    @FindBy(xpath = "(//button[@type='submit' and contains(., 'Add Nurse')])[2]")
    private WebElement saveNurseButton;

    @FindBy(xpath = "//*[text()='Nurse added successfully!']")
    private WebElement nurseVerification;

    @FindBy(xpath = "//ul[@role='listbox']//li[contains(text(), 'Arizona (AZ)')]")
    private WebElement stateName;

    public eAmataNursePage(WebDriver driver) {
        super();
        PageFactory.initElements(driver, this);
    }

    public void navigateToAddNurseForm() {
        waitForProgressBarToAppear();
        clickButton(settingsLink);
        waitForProgressBarToAppear();
        clickButton(adminUsersTab);
        waitForProgressBarToAppear();
        clickButton(nurseButton);
        clickButton(addNurseButton);

    }

    public void enterNurseDetails(String firstName, String lastName, String email, String phoneNumber, String NPI, String gender) throws InterruptedException {
        Thread.sleep(1000);
        setInputField(firstNameField, firstName);
        setInputField(lastNameField, lastName);
        setInputField(emailField, email);
        setInputField(phoneNumberField, phoneNumber);
        setInputField(NPIField, NPI);
        selectDropdownByVisibleText(genderDropdown, gender, GENDER_LIST_XPATH);
    }

    public void enterLicenseDetails(String licenseNumber, String licensedState, String expiryDate) throws InterruptedException {
        setInputField(licensedNumberField, licenseNumber);
        WebElement stateList =licensedStateDropdown;
        stateList.click();
        stateList.sendKeys(licensedState);
        waitForElementToBeVisible(stateName).click();
        new DatePicker(BaseClass.getDriver(), expiryDate);
    }

    public void saveNurse() {
        clickButton(saveNurseButton);
    }

    public String verifyNurseCreation() {
       // waitForProgressBarToAppear();
        return waitForElementToBeVisible(nurseVerification).getText();
    }
}
