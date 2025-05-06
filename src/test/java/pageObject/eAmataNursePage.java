package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.DatePicker;
import utilities.LoggerUtils;

/**
 * Page Object for the eAmata Nurse page.
 * Contains all elements and actions related to nurse management.
 */
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

    /**
     * Navigates to the Add Nurse form.
     * 
     * @throws RuntimeException if navigation fails
     */
    @Step("Navigating to Add Nurse form")
    public void navigateToAddNurseForm() {
        try {
            waitForProgressBarToAppear();
            clickButton(settingsLink);
            waitForProgressBarToAppear();
            clickButton(adminUsersTab);
            waitForProgressBarToAppear();
            clickButton(nurseButton);
            clickButton(addNurseButton);
            LoggerUtils.info("Successfully navigated to Add Nurse form");
        } catch (Exception e) {
            LoggerUtils.error("Failed to navigate to Add Nurse form: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to Add Nurse form", e);
        }
    }

    /**
     * Enters nurse details.
     * 
     * @param firstName   The nurse's first name
     * @param lastName    The nurse's last name
     * @param email       The nurse's email
     * @param phoneNumber The nurse's phone number
     * @param NPI         The nurse's NPI number
     * @param gender      The nurse's gender
     * @throws RuntimeException if entering details fails
     */
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

    /**
     * Enters license details.
     * 
     * @param licenseNumber The license number
     * @param licensedState The licensed state
     * @param expiryDate    The license expiry date
     * @throws RuntimeException if entering license details fails
     */
    @Step("Entering license details")
    public void enterLicenseDetails(String licenseNumber, String licensedState, String expiryDate) {
        try {
            setInputField(licensedNumberField, licenseNumber);
            WebElement stateList = licensedStateDropdown;
            stateList.click();
            stateList.sendKeys(licensedState);
            waitForElementToBeVisible(stateName).click();
            new DatePicker(BaseClass.getDriver(), expiryDate);
            LoggerUtils.info("Successfully entered license details");
        } catch (Exception e) {
            LoggerUtils.error("Failed to enter license details: " + e.getMessage());
            throw new RuntimeException("Failed to enter license details", e);
        }
    }

    /**
     * Saves the nurse details.
     * 
     * @throws RuntimeException if saving fails
     */
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

    /**
     * Verifies nurse creation.
     * 
     * @return The verification message
     * @throws RuntimeException if verification fails
     */
    @Step("Verifying nurse creation")
    public String verifyNurseCreation() {
        try {
            return waitForElementToBeVisible(nurseVerification).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to verify nurse creation: " + e.getMessage());
            throw new RuntimeException("Failed to verify nurse creation", e);
        }
    }

    /**
     * Gets the first name error message.
     * 
     * @return The error message text
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting first name error message")
    public String getFirstNameError() {
        try {
            return waitForElementToBeVisible(firstNameError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get first name error message: " + e.getMessage());
            throw new RuntimeException("Failed to get first name error message", e);
        }
    }

    /**
     * Gets the last name error message.
     * 
     * @return The error message text
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting last name error message")
    public String getLastNameError() {
        try {
            return waitForElementToBeVisible(lastNameError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get last name error message: " + e.getMessage());
            throw new RuntimeException("Failed to get last name error message", e);
        }
    }

    /**
     * Gets the email error message.
     * 
     * @return The error message text
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting email error message")
    public String getEmailError() {
        try {
            return waitForElementToBeVisible(emailError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get email error message: " + e.getMessage());
            throw new RuntimeException("Failed to get email error message", e);
        }
    }

    /**
     * Gets the phone number error message.
     * 
     * @return The error message text
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting phone number error message")
    public String getPhoneNumberError() {
        try {
            return waitForElementToBeVisible(phoneNumberError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get phone number error message: " + e.getMessage());
            throw new RuntimeException("Failed to get phone number error message", e);
        }
    }

    /**
     * Gets the NPI error message.
     * 
     * @return The error message text
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting NPI error message")
    public String getNPIError() {
        try {
            return waitForElementToBeVisible(NPIError).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get NPI error message: " + e.getMessage());
            throw new RuntimeException("Failed to get NPI error message", e);
        }
    }

    /**
     * Gets the license error message.
     * 
     * @return The error message text
     * @throws RuntimeException if getting error message fails
     */
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
