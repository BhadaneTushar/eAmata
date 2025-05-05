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
 * Page Object for the Staff page.
 * Contains all elements and actions related to staff management.
 */
public class StaffPage extends BasePage {

    private static final String GENDER_LIST_XPATH = "//ul[@role='listbox']/li";
    public static final String ROLE_LIST_XPATH = "//ul[@role='listbox']/li";

    @FindBy(xpath = "//tbody/tr[1]/td[1]/div[1]/a[1]")
    private WebElement providerGroupLink;

    @FindBy(xpath = "//button[text()='Staff']")
    private WebElement staffTabButton;

    @FindBy(xpath = "//span[text()='Add Staff']")
    private WebElement addStaffButton;

    @FindBy(xpath = "//input[@placeholder='Enter First Name']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[@placeholder='Enter Last Name']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@placeholder='Enter Email']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@placeholder='Enter Phone Number']")
    private WebElement phoneNumberInput;

    @FindBy(xpath = "//button[@role='combobox']/span[text()='Select Staff Role']")
    private WebElement roleDropdown;

    @FindBy(xpath = "//button[@role='combobox']/span[text()='Select Gender']")
    private WebElement genderDropdown;

    @FindBy(xpath = "//div[div[p[text()='Add Staff']]]//button[text()='Add Staff']")
    private WebElement saveButton;

    @FindBy(xpath = "//tbody/tr[1]/td[1]/div[1]/a[1]")
    private WebElement verifyStaff;

    @FindBy(xpath = "//label[text()='First Name is required']")
    private WebElement firstNameError;

    @FindBy(xpath = "//label[text()='Last Name is required']")
    private WebElement lastNameError;

    @FindBy(xpath = "//label[text()='Invalid phone number. It must be 10 digits.']")
    private WebElement phoneNumberError;

    @FindBy(xpath = "//label[normalize-space()='Phone is required']")
    private WebElement emptyphone;

    @FindBy(xpath = "//label[text()='Email is required']")
    private WebElement emptyGmail;

    public StaffPage(WebDriver driver) {
        super();
        PageFactory.initElements(driver, this);
        LoggerUtils.debug("Initialized StaffPage");
    }

    /**
     * Fills staff information.
     * 
     * @param firstName   The staff's first name
     * @param lastName    The staff's last name
     * @param email       The staff's email
     * @param phoneNumber The staff's phone number
     * @param role        The staff's role
     * @param gender      The staff's gender
     * @throws RuntimeException if filling staff information fails
     */
    @Step("Filling staff information")
    private void fillStaffInformation(String firstName, String lastName, String email, String phoneNumber, String role,
            String gender) {
        try {
            setInputField(firstNameInput, firstName);
            setInputField(lastNameInput, lastName);
            setInputField(emailInput, email);
            setInputField(phoneNumberInput, phoneNumber);
            selectDropdownByVisibleText(roleDropdown, role, ROLE_LIST_XPATH);
            selectDropdownByVisibleText(genderDropdown, gender, GENDER_LIST_XPATH);
            LoggerUtils.info("Successfully filled staff information");
        } catch (Exception e) {
            LoggerUtils.error("Failed to fill staff information: " + e.getMessage());
            throw new RuntimeException("Failed to fill staff information", e);
        }
    }

    /**
     * Adds a new staff member.
     * 
     * @param firstName    The staff's first name
     * @param lastName     The staff's last name
     * @param email        The staff's email
     * @param phoneNumber  The staff's phone number
     * @param role         The staff's role
     * @param gender       The staff's gender
     * @param addressLine1 The first line of the address
     * @param addressLine2 The second line of the address
     * @param city         The city
     * @param zipCode      The ZIP code
     * @param state        The state
     * @throws RuntimeException if adding staff fails
     */
    @Step("Adding new staff member")
    public void addStaff(String firstName, String lastName, String email, String phoneNumber, String role,
            String gender, String addressLine1, String addressLine2, String city, String zipCode, String state) {
        try {
            waitForProgressBarToAppear();
            clickButton(waitForElementToBeVisible(providerGroupLink));
            waitForProgressBarToAppear();
            ((JavascriptExecutor) BaseClass.getDriver()).executeScript("arguments[0].click();", staffTabButton);
            waitForProgressBarToAppear();
            clickButton(waitForElementToBeClickable(addStaffButton));
            fillStaffInformation(firstName, lastName, email, phoneNumber, role, gender);
            new Address(getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
            clickButton(waitForElementToBeVisible(saveButton));
            LoggerUtils.info("Successfully added staff member");
        } catch (Exception e) {
            LoggerUtils.error("Failed to add staff member: " + e.getMessage());
            throw new RuntimeException("Failed to add staff member", e);
        }
    }

    /**
     * Verifies staff creation.
     * 
     * @return The verification message
     * @throws RuntimeException if verification fails
     */
    @Step("Verifying staff creation")
    public String verifySuccessMessage() {
        try {
            waitForProgressBarToAppear();
            return waitForElementToBeVisible(verifyStaff).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to verify staff creation: " + e.getMessage());
            throw new RuntimeException("Failed to verify staff creation", e);
        }
    }

    /**
     * Gets the error message for first name.
     * 
     * @return The error message
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
     * Gets the error message for last name.
     * 
     * @return The error message
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
     * Gets the error message for phone number.
     * 
     * @return The error message
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
     * Gets the error message for empty phone.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting empty phone error message")
    public String getEmptyPhoneError() {
        try {
            return waitForElementToBeVisible(emptyphone).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get empty phone error message: " + e.getMessage());
            throw new RuntimeException("Failed to get empty phone error message", e);
        }
    }

    /**
     * Gets the error message for empty email.
     * 
     * @return The error message
     * @throws RuntimeException if getting error message fails
     */
    @Step("Getting empty email error message")
    public String getEmptyGmailError() {
        try {
            return waitForElementToBeVisible(emptyGmail).getText();
        } catch (Exception e) {
            LoggerUtils.error("Failed to get empty email error message: " + e.getMessage());
            throw new RuntimeException("Failed to get empty email error message", e);
        }
    }

    /**
     * Edits staff details.
     * 
     * @throws RuntimeException if editing staff fails
     */
    @Step("Editing staff details")
    public void editStaff() {
        try {
            // Future implementation
            LoggerUtils.info("Staff edit functionality not yet implemented");
        } catch (Exception e) {
            LoggerUtils.error("Failed to edit staff: " + e.getMessage());
            throw new RuntimeException("Failed to edit staff", e);
        }
    }

    /**
     * Archives staff.
     * 
     * @throws RuntimeException if archiving staff fails
     */
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

    /**
     * Activates or deactivates staff.
     * 
     * @throws RuntimeException if activating/deactivating staff fails
     */
    @Step("Activating/deactivating staff")
    public void activeInactiveStaff() {
        try {
            // Future implementation
            LoggerUtils.info("Staff activation/deactivation functionality not yet implemented");
        } catch (Exception e) {
            LoggerUtils.error("Failed to activate/deactivate staff: " + e.getMessage());
            throw new RuntimeException("Failed to activate/deactivate staff", e);
        }
    }
}
