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

public class StaffPage extends BasePage {

    // Constants
    private static final String GENDER_LIST_XPATH = "//ul[@role='listbox']/li";
    private static final String ROLE_LIST_XPATH = "//ul[@role='listbox']/li";

    // Navigation Elements
    @FindBy(xpath = "//tbody/tr[1]/td[1]/div[1]/a[1]")
    private WebElement providerGroupLink;

    @FindBy(xpath = "//button[text()='Staff']")
    private WebElement staffTabButton;

    @FindBy(xpath = "//span[text()='Add Staff']")
    private WebElement addStaffButton;

    // Input Fields
    @FindBy(xpath = "//input[@placeholder='Enter First Name']")
    private WebElement firstNameInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Last Name']")
    private WebElement lastNameInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Email']")
    private WebElement emailInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Phone Number']")
    private WebElement phoneNumberInputField;

    // Dropdown Elements
    @FindBy(xpath = "//button[@role='combobox']/span[text()='Select Staff Role']")
    private WebElement roleDropdownButton;

    @FindBy(xpath = "//button[@role='combobox']/span[text()='Select Gender']")
    private WebElement genderDropdownButton;

    // Action Buttons
    @FindBy(xpath = "//div[div[p[text()='Add Staff']]]//button[text()='Add Staff']")
    private WebElement saveButton;

    // Success Messages
    @FindBy(xpath = "//span[text()='User added successfully!']")
    private WebElement staffVerificationText;

    // Validation Messages
    @FindBy(xpath = "//label[text()='First Name is required']")
    private WebElement firstNameRequiredError;

    @FindBy(xpath = "//label[text()='Last Name is required']")
    private WebElement lastNameRequiredError;

    @FindBy(xpath = "//label[text()='Invalid phone number. It must be 10 digits.']")
    private WebElement invalidPhoneNumberError;

    @FindBy(xpath = "//label[normalize-space()='Phone is required']")
    private WebElement phoneRequiredError;

    @FindBy(xpath = "//label[text()='Email is required']")
    private WebElement emailRequiredError;

    public StaffPage(WebDriver driver) {
        super();
        PageFactory.initElements(driver, this);
        LoggerUtils.debug("Initialized StaffPage");
    }

    // Navigation Methods
    public void navigateToProviderGroup() {
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeVisible(providerGroupLink));
    }

    public void navigateToStaffTab() {
        waitForProgressBarToAppear();
        ((JavascriptExecutor) BaseClass.getDriver()).executeScript("arguments[0].click();", staffTabButton);
    }

    public void clickAddStaff() {
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeClickable(addStaffButton));
    }

    // Input Field Methods
    private void fillStaffInformation(String firstName, String lastName, String email, String phoneNumber, String role,
                                      String gender) {
        setInputField(firstNameInputField, firstName);
        setInputField(lastNameInputField, lastName);
        setInputField(emailInputField, email);
        setInputField(phoneNumberInputField, phoneNumber);
        selectDropdownByVisibleText(roleDropdownButton, role, ROLE_LIST_XPATH);
        selectDropdownByVisibleText(genderDropdownButton, gender, GENDER_LIST_XPATH);
    }

    // Combined Action Methods
    public void addStaff(String firstName, String lastName, String email, String phoneNumber, String role,
                         String gender, String addressLine1, String addressLine2, String city, String zipCode, String state) {
        navigateToProviderGroup();
        navigateToStaffTab();
        clickAddStaff();
        fillStaffInformation(firstName, lastName, email, phoneNumber, role, gender);
        new Address(getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
        saveStaff();
    }

    // Form Submission Methods
    public void saveStaff() {
        clickButton(waitForElementToBeVisible(saveButton));
    }

    // Success Message Methods
    public String getStaffVerificationText() {
        return waitForElementToBeVisible(staffVerificationText).getText();
    }

    // Validation Message Methods
    public String getFirstNameRequiredError() {
        return waitForElementToBeVisible(firstNameRequiredError).getText();
    }

    public String getLastNameRequiredError() {
        return waitForElementToBeVisible(lastNameRequiredError).getText();
    }

    public String getInvalidPhoneNumberError() {
        return waitForElementToBeVisible(invalidPhoneNumberError).getText();
    }

    public String getPhoneRequiredError() {
        return waitForElementToBeVisible(phoneRequiredError).getText();
    }

    public String getEmailRequiredError() {
        return waitForElementToBeVisible(emailRequiredError).getText();
    }

    // Status Management Methods
    public void editStaff() {
        try {
            // Future implementation
            LoggerUtils.info("Staff edit functionality not yet implemented");
        } catch (Exception e) {
            LoggerUtils.error("Failed to edit staff: " + e.getMessage());
            throw new RuntimeException("Failed to edit staff", e);
        }
    }

    @Step("Archiving staff")
    public void archiveStaff() {
        try {
            // Future implementation
            LoggerUtils.info("Staff archive functionality not yet implemented");
        } catch (Exception e) {
            LoggerUtils.error("Failed to archive staff: " + e.getMessage());
            throw new RuntimeException("Failed to archive staff", e);
        }
    }

    public void toggleStaffStatus() {
        // Future implementation
    }
}
