package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObject.BasePage;

import java.time.Duration;
import java.util.regex.Pattern;

/**
 * Utility class for handling address-related operations.
 * Provides methods for entering and validating address details with
 * international support.
 */
public class Address extends BasePage {

    // WebElements for address input fields
    @FindBy(xpath = "//input[@placeholder='Enter Line 1']")
    private WebElement addressLine1Input;

    @FindBy(xpath = "//input[@placeholder='Enter Line 2']")
    private WebElement addressLine2Input;

    @FindBy(xpath = "//input[@placeholder='Enter City']")
    private WebElement cityInput;

    @FindBy(xpath = "//input[@placeholder='Enter Zip Code']")
    private WebElement zipCodeInput;

    @FindBy(xpath = "//input[@placeholder='Select State']")
    private WebElement stateDropdownButton;

    @FindBy(xpath = "//input[@placeholder='Select Country']")
    private WebElement countryDropdownButton;

    // Constants for validation
    private static final Duration STATE_SELECTION_TIMEOUT = Duration.ofSeconds(5);
    private static final String STATE_LIST_XPATH = "//ul[contains(@class, 'css-18lh1r')]";
    private static final String COUNTRY_LIST_XPATH = "//ul[contains(@class, 'css-18lh1r')]";

    private String selectedCountry = Constants.DEFAULT_COUNTRY;

    public Address(WebDriver driver) {
        super();
        PageFactory.initElements(driver, this);
        LoggerUtils.debug("Initialized Address utility");
    }

    /**
     * Sets the country for address validation.
     *
     * @param country The country code (e.g., "US", "CA", "UK")
     */
    public void setCountry(String country) {
        if (Constants.COUNTRY_ZIP_PATTERNS.containsKey(country)) {
            this.selectedCountry = country;
            LoggerUtils.debug("Set country to: " + country);
        } else {
            throw new IllegalArgumentException("Unsupported country: " + country);
        }
    }

    /**
     * Enters address details with validation, defaulting to US as the country.
     *
     * @param addressLine1 The first line of the address
     * @param addressLine2 The second line of the address
     * @param city         The city name
     * @param zip          The ZIP code
     * @param state        The state name
     * @throws IllegalArgumentException if any input is invalid
     */
    public void enterAddressDetails(String addressLine1, String addressLine2, String city, String zip, String state) {
        enterAddressDetails(addressLine1, addressLine2, city, zip, state, Constants.DEFAULT_COUNTRY);
    }

    /**
     * Enters address details with validation.
     *
     * @param addressLine1 The first line of the address
     * @param addressLine2 The second line of the address
     * @param city         The city name
     * @param zip          The ZIP code
     * @param state        The state name
     * @param country      The country name
     * @throws IllegalArgumentException if any input is invalid
     */
    public void enterAddressDetails(String addressLine1, String addressLine2, String city, String zip, String state,
            String country) {
        LoggerUtils.info("Entering address details");
        validateAddressInputs(addressLine1, city, zip, state, country);

        try {
            // Enter addressline 1
            setInputField(addressLine1Input, addressLine1);
            LoggerUtils.debug("Entered address line 1: " + addressLine1);

            // Enter address line 2 if provided
            if (addressLine2 != null && !addressLine2.isEmpty()) {
                setInputField(addressLine2Input, addressLine2);
                LoggerUtils.debug("Entered address line 2: " + addressLine2);
            }

            // Enter city
            setInputField(cityInput, city);
            LoggerUtils.debug("Entered city: " + city);

            // Enter ZIP code
            setInputField(zipCodeInput, zip);
            LoggerUtils.debug("Entered ZIP code: " + zip);

            // Select state
            selectState(state);
            LoggerUtils.debug("Selected state: " + state);

        } catch (Exception e) {
            LoggerUtils.error("Failed to enter address details: " + e.getMessage());
            throw new RuntimeException("Failed to enter address details: " + e.getMessage(), e);
        }
    }

    /**
     * Validates address inputs.
     *
     * @param addressLine1 The first line of the address
     * @param city         The city name
     * @param zip          The ZIP code
     * @param state        The state name
     * @param country      The country name
     * @throws IllegalArgumentException if any input is invalid
     */
    private void validateAddressInputs(String addressLine1, String city, String zip, String state, String country) {
        if (addressLine1 == null || addressLine1.trim().isEmpty()) {
            throw new IllegalArgumentException("Address line 1 is required");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        if (zip == null || !isValidZipCode(zip, country)) {
            throw new IllegalArgumentException("Invalid ZIP code format for country: " + country);
        }
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State is required");
        }
        if (country == null || !Constants.COUNTRY_ZIP_PATTERNS.containsKey(country)) {
            throw new IllegalArgumentException("Unsupported country: " + country);
        }
    }

    /**
     * Validates a ZIP code for the current country.
     *
     * @param zipCode The ZIP code to validate
     * @param country The country code
     * @return true if the ZIP code is valid
     */
    private boolean isValidZipCode(String zipCode, String country) {
        String pattern = Constants.COUNTRY_ZIP_PATTERNS.get(country);
        return pattern != null && Pattern.matches(pattern, zipCode);
    }

    /**
     * Selects a country from the dropdown.
     *
     * @param country The country name to select
     * @throws RuntimeException if country selection fails
     */
    private void selectCountry(String country) {
        LoggerUtils.debug("Selecting country: " + country);
        try {
            // Click the country dropdown
            clickButton(countryDropdownButton);

            // Wait for the country list to be visible
            WebDriverWait wait = new WebDriverWait(getDriver(), STATE_SELECTION_TIMEOUT);
            WebElement countryList = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(COUNTRY_LIST_XPATH)));

            // Find and click the country
            WebElement countryElement = countryList.findElement(By.xpath(".//li[text()='" + country + "']"));
            clickButton(countryElement);

            // Verify country selection
            wait.until(ExpectedConditions.textToBePresentInElementValue(countryDropdownButton, country));
            LoggerUtils.debug("Country selected successfully: " + country);

        } catch (TimeoutException e) {
            LoggerUtils.error("Timeout while selecting country: " + country);
            throw new RuntimeException("Timeout while selecting country: " + country, e);
        } catch (NoSuchElementException e) {
            LoggerUtils.error("Country not found in dropdown: " + country);
            throw new RuntimeException("Country not found in dropdown: " + country, e);
        } catch (Exception e) {
            LoggerUtils.error("Failed to select country: " + country);
            throw new RuntimeException("Failed to select country: " + country, e);
        }
    }

    /**
     * Selects a state from the dropdown.
     *
     * @param state The state name to select
     * @throws RuntimeException if state selection fails
     */
    private void selectState(String state) {
        LoggerUtils.debug("Selecting state: " + state);
        try {
            // Click the state dropdown
            clickButton(stateDropdownButton);

            // Wait for the state list to be visible
            WebDriverWait wait = new WebDriverWait(getDriver(), STATE_SELECTION_TIMEOUT);
            WebElement stateList = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(STATE_LIST_XPATH)));

            // Find and click the state
            WebElement stateElement = stateList.findElement(By.xpath(".//li[text()='" + state + "']"));
            clickButton(stateElement);

            // Verify state selection
            wait.until(ExpectedConditions.textToBePresentInElementValue(stateDropdownButton, state));
            LoggerUtils.debug("State selected successfully: " + state);

        } catch (TimeoutException e) {
            LoggerUtils.error("Timeout while selecting state: " + state);
            throw new RuntimeException("Timeout while selecting state: " + state, e);
        } catch (NoSuchElementException e) {
            LoggerUtils.error("State not found in dropdown: " + state);
            throw new RuntimeException("State not found in dropdown: " + state, e);
        } catch (Exception e) {
            LoggerUtils.error("Failed to select state: " + state);
            throw new RuntimeException("Failed to select state: " + state, e);
        }
    }
}