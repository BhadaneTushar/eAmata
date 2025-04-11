package pageObject;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.Address;

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
    }

    private void fillStaffInformation(String firstName, String lastName, String email, String phoneNumber, String role, String gender) {
        setInputField(firstNameInput, firstName);
        setInputField(lastNameInput, lastName);
        setInputField(emailInput, email);
        setInputField(phoneNumberInput, phoneNumber);
        selectDropdownByVisibleText(roleDropdown, role, ROLE_LIST_XPATH);
        selectDropdownByVisibleText(genderDropdown, gender, GENDER_LIST_XPATH);
    }

    public void addStaff(String firstName, String lastName, String email, String phoneNumber, String role, String gender, String addressLine1, String addressLine2, String city, String zipCode, String state) {
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeVisible(providerGroupLink));
        waitForProgressBarToAppear();
        ((JavascriptExecutor) BaseClass.getDriver()).executeScript("arguments[0].click();", staffTabButton);
        waitForProgressBarToAppear();
        clickButton(waitForElementToBeClickable(addStaffButton));
        fillStaffInformation(firstName, lastName, email, phoneNumber, role, gender);
        new Address(getDriver()).enterAddressDetails(addressLine1, addressLine2, city, zipCode, state);
        clickButton(waitForElementToBeVisible(saveButton));
    }

    public String verifySuccessMessage() {
        waitForProgressBarToAppear();
        return waitForElementToBeVisible(verifyStaff).getText();
    }

    public String getFirstNameError() {
        return waitForElementToBeVisible(firstNameError).getText();
    }

    public String getLastNameError() {
        return waitForElementToBeVisible(lastNameError).getText();
    }

    public String getPhoneNumberError() {
        return waitForElementToBeVisible(phoneNumberError).getText();
    }

    public String getEmptyPhoneError() {
        return waitForElementToBeVisible(emptyphone).getText();
    }
    public String getEmptyGmailError() {
        return waitForElementToBeVisible(emptyGmail).getText();
    }

    public void editStaff() {
        // Future implementation
    }

    public void archiveStaff() {
        // Future implementation
    }

    public void activeInactiveStaff() {
        // Future implementation
    }
}
