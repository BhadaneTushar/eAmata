package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.DatePicker;
import utilities.LoggerUtils;

public class eAmataNursePage extends BasePage {

    private static final String GENDER_LIST_XPATH = "//ul[@role='listbox']/li";

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

    // Address Fields
    @FindBy(xpath = "//input[@placeholder='Enter Address Line 1']")
    private WebElement addressLine1Field;

    @FindBy(xpath = "//input[@placeholder='Enter Address Line 2']")
    private WebElement addressLine2Field;

    @FindBy(xpath = "//input[@placeholder='Enter City']")
    private WebElement cityField;

    @FindBy(xpath = "//input[@placeholder='Enter Zip Code']")
    private WebElement zipCodeField;

    @FindBy(xpath = "(//input[@placeholder='Select State'])[1]")
    private WebElement addressStateDropdown;

    @FindBy(xpath = "//ul[@role='listbox']//li[contains(text(), 'Arizona (AZ)')]")
    private WebElement addressStateName;

    // Error message locators
    @FindBy(xpath = "//input[@placeholder='Enter First Name']/following-sibling::div[contains(@class, 'error')]")
    private WebElement firstNameError;

    @FindBy(xpath = "//input[@placeholder='Enter Last Name']/following-sibling::div[contains(@class, 'error')]")
    private WebElement lastNameError;

    @FindBy(xpath = "//input[@placeholder='Enter Email']/following-sibling::div[contains(@class, 'error')]")
    private WebElement emailError;

    @FindBy(xpath = "//input[@placeholder='Enter Phone Number']/following-sibling::div[contains(@class, 'error')]")
    private WebElement phoneNumberError;

    @FindBy(xpath = "//input[@placeholder='Enter NPI Number']/following-sibling::div[contains(@class, 'error')]")
    private WebElement NPIError;

    @FindBy(xpath = "//input[@placeholder='Enter License Number']/following-sibling::div[contains(@class, 'error')]")
    private WebElement licenseError;

    public eAmataNursePage(WebDriver driver) {
        super();
        PageFactory.initElements(driver, this);
        LoggerUtils.debug("Initialized eAmataNursePage");
    }

    @Step("Navigating to Add Nurse form")
    public void navigateToAddNurseForm() {
        clickButton(settingsLink);
        clickButton(adminUsersTab);
        clickButton(nurseButton);
        clickButton(addNurseButton);

    }

    @Step("Entering nurse details")
    public void enterNurseDetails(String firstName, String lastName, String email, String phoneNumber, String NPI,
            String gender) {
        try {
            Thread.sleep(1000);
            setInputField(firstNameField, firstName);
            setInputField(lastNameField, lastName);
            setInputField(emailField, email);
            setInputField(phoneNumberField, phoneNumber);
            setInputField(NPIField, NPI);
            selectDropdownByVisibleText(genderDropdown, gender, GENDER_LIST_XPATH);
            LoggerUtils.info("Successfully entered nurse details");
        } catch (Exception e) {
            LoggerUtils.error("Failed to enter nurse details: " + e.getMessage());
            throw new RuntimeException("Failed to enter nurse details", e);
        }
    }

    @Step("Entering license details")
    public void enterLicenseDetails(String licenseNumber, String licensedState, String expiryDate) {
        try {
            setInputField(licensedNumberField, licenseNumber);
            WebElement stateList = licensedStateDropdown;
            stateList.click();
            stateList.sendKeys(licensedState);
            waitForElementToBeVisible(stateName).click();

            // Create DatePicker instance and select the date
            DatePicker datePicker = new DatePicker(BaseClass.getDriver(), expiryDate, "MM/dd/yyyy");
            datePicker.selectDate();

            LoggerUtils.info("Successfully entered license details");
        } catch (Exception e) {
            LoggerUtils.error("Failed to enter license details: " + e.getMessage());
            throw new RuntimeException("Failed to enter license details", e);
        }
    }

    @Step("Entering address details")
    public void enterAddressDetails(String addressLine1, String addressLine2, String city, String zipCode,
            String state) {
        try {
            setInputField(addressLine1Field, addressLine1);
            setInputField(addressLine2Field, addressLine2);
            setInputField(cityField, city);
            setInputField(zipCodeField, zipCode);

            WebElement stateList = addressStateDropdown;
            stateList.click();
            stateList.sendKeys(state);
            waitForElementToBeVisible(addressStateName).click();

            LoggerUtils.info("Successfully entered address details");
        } catch (Exception e) {
            LoggerUtils.error("Failed to enter address details: " + e.getMessage());
            throw new RuntimeException("Failed to enter address details", e);
        }
    }

    @Step("Saving nurse details")
    public void saveNurse() {
        try {
            clickButton(saveNurseButton);
            LoggerUtils.info("Successfully saved nurse details");
        } catch (Exception e) {
            LoggerUtils.error("Failed to save nurse details: " + e.getMessage());
            throw new RuntimeException("Failed to save nurse details", e);
        }
    }

    @Step("Verifying nurse creation")
    public String verifyNurseCreation() {
        try {
            waitForProgressBarToAppear(); // Wait for any loading operations
            return waitForElementToBeVisible(nurseVerification).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to verify nurse creation: " + e.getMessage());
            throw new RuntimeException("Failed to verify nurse creation", e);
        }
    }

    @Step("Getting first name error message")
    public String getFirstNameError() {
        try {
            return waitForElementToBeVisible(firstNameError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get first name error message: " + e.getMessage());
            throw new RuntimeException("Failed to get first name error message", e);
        }
    }

    @Step("Getting last name error message")
    public String getLastNameError() {
        try {
            return waitForElementToBeVisible(lastNameError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get last name error message: " + e.getMessage());
            throw new RuntimeException("Failed to get last name error message", e);
        }
    }

    @Step("Getting email error message")
    public String getEmailError() {
        try {
            return waitForElementToBeVisible(emailError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get email error message: " + e.getMessage());
            throw new RuntimeException("Failed to get email error message", e);
        }
    }

    @Step("Getting phone number error message")
    public String getPhoneNumberError() {
        try {
            return waitForElementToBeVisible(phoneNumberError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get phone number error message: " + e.getMessage());
            throw new RuntimeException("Failed to get phone number error message", e);
        }
    }

    @Step("Getting NPI error message")
    public String getNPIError() {
        try {
            return waitForElementToBeVisible(NPIError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get NPI error message: " + e.getMessage());
            throw new RuntimeException("Failed to get NPI error message", e);
        }
    }

    @Step("Getting license error message")
    public String getLicenseError() {
        try {
            return waitForElementToBeVisible(licenseError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get license error message: " + e.getMessage());
            throw new RuntimeException("Failed to get license error message", e);
        }
    }
}
