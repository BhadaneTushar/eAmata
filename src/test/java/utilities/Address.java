package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObject.BasePage;

public class Address extends BasePage {

    // WebElements for address input fields
    @FindBy(xpath = "//input[@placeholder='Enter Address Line 1']")
    private WebElement addressLine1Input;

    @FindBy(xpath = "//input[@placeholder='Enter Address Line 2']")
    private WebElement addressLine2Input;

    @FindBy(xpath = "//input[@placeholder='Enter Address City']")
    private WebElement cityInput;

    @FindBy(xpath = "//input[@placeholder='Enter Address Zipcode']")
    private WebElement zipCodeInput;

    @FindBy(xpath = "//input[@placeholder='Select State']")
    private WebElement stateDropdownButton;

    @FindBy(xpath = "//li[text()='Arizona']")
    private WebElement stateName;


    public static final String STATE_LIST_XPATH = "//ul[contains(@class, 'css-18lh1r')]";


    public Address(WebDriver driver) {
        super();
        PageFactory.initElements(driver, this);
    }

    public void enterAddressDetails(String addressLine1, String addressLine2, String city, String zip, String state) {
        try {
            setInputField(addressLine1Input, addressLine1);
            setInputField(addressLine2Input, addressLine2);
            setInputField(cityInput, city);
            setInputField(zipCodeInput, zip);
            //State Selection is hardcoded
            WebElement stateList =stateDropdownButton;
            stateList.click();
            stateList.sendKeys(state);
            stateName.click();
            //selectDropdownByVisibleText(stateDropdownButton, state, STATE_LIST_XPATH);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter address details: " + e.getMessage(), e);
        }
    }
    //ToDo Improve state selection
            /*stateDropdownButton.click(); // Open dropdown
            WebElement dynamicState = driver.findElement(By.xpath("//li[text()='" + state + "']"));
            dynamicState.click(); // Select the state dynamically*/


}