package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import testBase.BaseClass;
import utilities.ElementActions;
import utilities.LoggerUtils;

import java.util.List;

/**
 * Optimized Base class for all page objects
 * Provides common functionality for page interactions with performance enhancements
 *
 * Flow:
 * - Page objects extend BasePage. The constructor calls PageFactory.initElements using BaseClass.getDriver().
 * - All interaction helpers delegate to utilities.ElementActions, which in turn pulls the ThreadLocal WebDriver from BaseClass.
 * - Tests call page object methods; those methods call BasePage helpers; helpers route to ElementActions; ElementActions acts on the WebDriver.
 *
 * Data:
 * - Inputs: WebElement locators in child pages; any method parameters passed from tests (e.g., text values).
 * - Outputs: Returns WebElement states/strings/booleans back to tests or higher-level page actions.
 */
public class BasePage {
    
    private final long pageInitStartTime;
    
    /**
     * Constructor to initialize page elements with performance tracking
     */
    public BasePage() {
        pageInitStartTime = System.currentTimeMillis();
        PageFactory.initElements(BaseClass.getDriver(), this);
        long initTime = System.currentTimeMillis() - pageInitStartTime;
        LoggerUtils.debug("Initialized " + this.getClass().getSimpleName() + " in " + initTime + "ms");
    }
    
    /**
     * Get page initialization time
     * @return Initialization time in milliseconds
     */
    protected long getPageInitTime() {
        return System.currentTimeMillis() - pageInitStartTime;
    }

    /**
     * Get the WebDriver instance
     * @return WebDriver instance
     * Note: Backed by BaseClass ThreadLocal; safe for parallel tests.
     */
    protected WebDriver getDriver() {
        return BaseClass.getDriver();
    }

    /**
     * Wait for UI to load
     */
    @Step("Waiting for UI to load")
    protected void waitForUILoad() {
        LoggerUtils.info("Step: Waiting for UI to load");
        ElementActions.waitForPageLoad();
    }

    /**
     * Wait for element to be clickable
     * @param element WebElement to wait for
     * @return The clickable WebElement
     */
    @Step("Waiting for element to be clickable")
    protected WebElement waitForElementToBeClickable(WebElement element) {
        LoggerUtils.info("Step: Waiting for element to be clickable: " + safeLocator(element));
        return ElementActions.waitForElementToBeClickable(element);
    }

    /**
     * Wait for element to be visible
     * @param element WebElement to wait for
     * @return The visible WebElement
     */
    @Step("Waiting for element to be visible")
    protected WebElement waitForElementToBeVisible(WebElement element) {
        LoggerUtils.info("Step: Waiting for element to be visible: " + safeLocator(element));
        return ElementActions.waitForElementToBeVisible(element);
    }

    /**
     * Set input field value
     * @param element WebElement to set value for
     * @param value Value to set
     */
    @Step("Setting input field value: {1}")
    protected void setInputField(WebElement element, String value) {
        LoggerUtils.info("Step: Setting input value '" + value + "' to element: " + safeLocator(element));
        ElementActions.type(element, value);
    }

    /**
     * Click a button
     * @param element WebElement to click
     */
    @Step("Clicking element")
    protected void clickButton(WebElement element) {
        LoggerUtils.info("Step: Clicking element: " + safeLocator(element));
        ElementActions.click(element);
    }

    /**
     * Select dropdown option by visible text
     * @param dropdownElement Dropdown WebElement
     * @param visibleText Text to select
     * @param listItemsXPath XPath to find dropdown items
     * Data flow: visibleText comes from test -> page method -> this helper; selection is performed via ElementActions.
     */
    @Step("Selecting dropdown option: {1}")
    protected void selectDropdownByVisibleText(WebElement dropdownElement, String visibleText, String listItemsXPath) {
        LoggerUtils.info("Step: Selecting dropdown option '" + visibleText + "' from element: " + safeLocator(dropdownElement));
        ElementActions.click(dropdownElement);
        List<WebElement> dropdownItems = getDriver().findElements(By.xpath(listItemsXPath));
        for (WebElement item : dropdownItems) {
            if (item.getText().equalsIgnoreCase(visibleText)) {
                ElementActions.click(item);
                return;
            }
        }
        throw new RuntimeException("Dropdown item not found: " + visibleText);
    }

    /**
     * Wait for progress bar to appear
     */
    @Step("Waiting for progress bar to appear")
    protected void waitForProgressBarToAppear() {
        LoggerUtils.info("Step: Waiting for progress bar to appear");
        By progressBarLocator = By.xpath("//div[span[@role='progressbar']]");
        ElementActions.waitForElementToBeVisible(progressBarLocator);
        LoggerUtils.debug("Progress bar operation completed");
    }

    /**
     * Wait for progress bar to appear with a custom timeout
     * 
     * @param timeoutInSeconds Custom timeout in seconds
     */
    @Step("Waiting for progress bar with custom timeout: {0}s")
    protected void waitForProgressBarWithTimeout(int timeoutInSeconds) {
        LoggerUtils.info("Step: Waiting for progress bar with custom timeout: " + timeoutInSeconds + "s");
        By progressBarLocator = By.xpath("//div[span[@role='progressbar']]");
        try {
            ElementActions.waitForElementToBeVisibleWithTimeout(progressBarLocator, timeoutInSeconds);
            LoggerUtils.debug("Progress bar operation completed within custom timeout");
        } catch (Exception e) {
            LoggerUtils.debug("Progress bar not found within custom timeout: " + timeoutInSeconds + "s");
            // Don't rethrow, this is an optional wait
        }
    }

    /**
     * Check if progress bar is displayed
     * @return true if progress bar is displayed, false otherwise
     */
    @Step("Checking if progress bar is displayed")
    public boolean isProgressBarDisplayed() {
        LoggerUtils.info("Step: Checking if progress bar is displayed");
        By progressBarLocator = By.xpath("//div[span[@role='progressbar']]");
        return ElementActions.isDisplayed(progressBarLocator);
    }

    /**
     * Check if element is displayed
     * @param element WebElement to check
     * @return true if element is displayed, false otherwise
     */
    @Step("Checking if element is displayed")
    protected boolean isElementDisplayed(WebElement element) {
        LoggerUtils.info("Step: Checking if element is displayed: " + safeLocator(element));
        return ElementActions.isDisplayed(element);
    }

    /**
     * Upload a file
     * @param element File input WebElement
     * @param filePath Path to the file to upload
     */
    @Step("Uploading file: {1}")
    protected void uploadFile(WebElement element, String filePath) {
        LoggerUtils.info("Step: Uploading file '" + filePath + "' to element: " + safeLocator(element));
        ElementActions.uploadFile(element, filePath);
    }

    /**
     * Switch to a new window
     */
    @Step("Switching to new window")
    protected void switchToNewWindow() {
        LoggerUtils.info("Step: Switching to new window");
        String originalWindow = getDriver().getWindowHandle();
        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }
    }

    /**
     * Accept an alert
     */
    @Step("Accepting alert")
    protected void acceptAlert() {
        LoggerUtils.info("Step: Accepting alert");
        ElementActions.acceptAlert();
    }
    
    /**
     * Get text from an element
     * @param element WebElement to get text from
     * @return Text content of the element
     */
    @Step("Getting text from element")
    protected String getText(WebElement element) {
        LoggerUtils.info("Step: Getting text from element: " + safeLocator(element));
        return ElementActions.getText(element);
    }
    
    /**
     * Hover over an element
     * @param element WebElement to hover over
     */
    @Step("Hovering over element")
    protected void hoverOverElement(WebElement element) {
        LoggerUtils.info("Step: Hovering over element: " + safeLocator(element));
        ElementActions.hover(element);
    }
    
    /**
     * Scroll to an element
     * @param element WebElement to scroll to
     */
    @Step("Scrolling to element")
    protected void scrollToElement(WebElement element) {
        LoggerUtils.info("Step: Scrolling to element: " + safeLocator(element));
        ElementActions.scrollToElement(element);
    }

    private String safeLocator(WebElement element) {
        try {
            return element.toString();
        } catch (Exception e) {
            return "[element]";
        }
    }
}