package utilities;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testBase.BaseClass;

import java.io.File;
import java.time.Duration;
import java.util.List;

/**
 * Utility class that centralizes all element interactions
 * Implements the DRY principle by providing reusable methods for common Selenium actions
 *
 * Flow:
 * - Callers: Page object base/helpers and concrete page classes call these static methods.
 * - This class fetches the ThreadLocal WebDriver via `BaseClass.getDriver()` and performs waits and actions.
 * - Return values (text, booleans, WebElements) are passed back up to page objects -> tests.
 *
 * Data:
 * - Inputs: WebElements/locators and values (text to type, option labels) passed from page objects/tests.
 * - Outputs: Element states, text content, or side-effects (clicks, typing) in the browser session.
 */
public class ElementActions {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration POLLING_INTERVAL = Duration.ofMillis(500);
    private static final Duration UI_LOAD_WAIT = Duration.ofMillis(1000);

    private ElementActions() {
        // Private constructor to prevent instantiation
    }

    /**
     * Get the WebDriver instance from BaseClass
     * @return WebDriver instance
     */
    private static WebDriver getDriver() {
        return BaseClass.getDriver();
    }

    /**
     * Optimized wait for page to load completely with smart detection
     */
    @Step("Waiting for page to load completely")
    public static void waitForPageLoad() {
        LoggerUtils.info("Step: Waiting for page to load completely");
        long startTime = System.currentTimeMillis();
        
        // First check if page is already loaded
        if (isPageLoaded()) {
            LoggerUtils.debug("Page already loaded, skipping wait");
            return;
        }
        
        // Smart wait with multiple conditions
        WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_TIMEOUT);
        wait.until(webDriver -> {
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            
            // Check document ready state
            boolean documentReady = js.executeScript("return document.readyState").equals("complete");
            
            // Check jQuery if present
            boolean jQueryReady = true;
            try {
                jQueryReady = (Boolean) js.executeScript("return typeof jQuery === 'undefined' || jQuery.active === 0");
            } catch (Exception e) {
                // jQuery not present, ignore
            }
            
            // Check for common loading indicators
            boolean noLoadingIndicators = true;
            try {
                noLoadingIndicators = ((Long) js.executeScript(
                    "return document.querySelectorAll('.loading, .spinner, [class*=\"load\"]').length")).equals(0L);
            } catch (Exception e) {
                // Ignore if script fails
            }
            
            return documentReady && jQueryReady && noLoadingIndicators;
        });
        
        long endTime = System.currentTimeMillis();
        LoggerUtils.debug("Page loaded completely in " + (endTime - startTime) + "ms");
    }
    
    /**
     * Quick check if page is already loaded
     * @return true if page is loaded
     */
    private static boolean isPageLoaded() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            return js.executeScript("return document.readyState").equals("complete");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for an element to be clickable
     * @param element WebElement to wait for
     * @return The clickable WebElement
     */
    @Step("Waiting for element to be clickable")
    public static WebElement waitForElementToBeClickable(WebElement element) {
        LoggerUtils.info("Step: Waiting for element to be clickable: " + safeLocator(element));
        LoggerUtils.debug("Waiting for element to be clickable");
        waitForPageLoad();
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(StaleElementReferenceException.class, ElementClickInterceptedException.class);

        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for an element to be clickable using By locator
     * @param locator By locator to find the element
     * @return The clickable WebElement
     */
    @Step("Waiting for element to be clickable by locator")
    public static WebElement waitForElementToBeClickable(By locator) {
        LoggerUtils.info("Step: Waiting for element to be clickable by locator: " + locator);
        LoggerUtils.debug("Waiting for element to be clickable: " + locator);
        waitForPageLoad();
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(StaleElementReferenceException.class, ElementClickInterceptedException.class);

        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for an element to be visible
     * @param element WebElement to wait for
     * @return The visible WebElement
     */
    @Step("Waiting for element to be visible")
    public static WebElement waitForElementToBeVisible(WebElement element) {
        LoggerUtils.info("Step: Waiting for element to be visible: " + safeLocator(element));
        LoggerUtils.debug("Waiting for element to be visible");
        waitForPageLoad();
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for an element to be visible using By locator
     * @param locator By locator to find the element
     * @return The visible WebElement
     */
    @Step("Waiting for element to be visible by locator")
    public static WebElement waitForElementToBeVisible(By locator) {
        LoggerUtils.info("Step: Waiting for element to be visible by locator: " + locator);
        LoggerUtils.debug("Waiting for element to be visible: " + locator);
        waitForPageLoad();
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for an element to be visible using By locator with custom timeout
     * @param locator By locator to find the element
     * @param timeoutInSeconds Custom timeout in seconds
     * @return The visible WebElement
     */
    @Step("Waiting for element to be visible by locator with custom timeout: {1}s")
    public static WebElement waitForElementToBeVisibleWithTimeout(By locator, int timeoutInSeconds) {
        LoggerUtils.info("Step: Waiting for element to be visible with custom timeout " + timeoutInSeconds + "s: " + locator);
        LoggerUtils.debug("Waiting for element to be visible with custom timeout " + timeoutInSeconds + "s: " + locator);
        waitForPageLoad();
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for an element to be present in the DOM
     * @param locator By locator to find the element
     * @return The present WebElement
     */
    @Step("Waiting for element to be present")
    public static WebElement waitForElementToBePresent(By locator) {
        LoggerUtils.info("Step: Waiting for element to be present: " + locator);
        LoggerUtils.debug("Waiting for element to be present: " + locator);
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(NoSuchElementException.class);

        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Click on an element with proper waits and fallback to JavaScript click if needed
     * @param element WebElement to click
     */
    @Step("Clicking element")
    public static void click(WebElement element) {
        LoggerUtils.info("Step: Clicking element: " + safeLocator(element));
        LoggerUtils.debug("Attempting to click element");
        try {
            waitForPageLoad();
            waitForElementToBeClickable(element).click();
        } catch (Exception e) {
            LoggerUtils.warn("Regular click failed, attempting JavaScript click: " + e.getMessage());
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Click on an element using By locator
     * @param locator By locator to find the element
     */
    @Step("Clicking element by locator")
    public static void click(By locator) {
        LoggerUtils.info("Step: Clicking element by locator: " + locator);
        LoggerUtils.debug("Attempting to click element: " + locator);
        WebElement element = waitForElementToBeClickable(locator);
        click(element);
    }

    /**
     * Type text into an input field with proper waits and clearing
     * @param element WebElement to type into
     * @param value Text to type
     * Data flow: value originates in tests/test data -> page object -> ElementActions; the typed value goes to the browser DOM via WebDriver.
     */
    @Step("Setting input field value: {1}")
    public static void type(WebElement element, String value) {
        LoggerUtils.info("Step: Typing value '" + value + "' into element: " + safeLocator(element));
        LoggerUtils.debug("Setting input field value: " + value);
        WebElement inputField = waitForElementToBeVisible(element);
        inputField.clear();
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value = '';", inputField);
        inputField.sendKeys(value);
    }

    /**
     * Type text into an input field using By locator
     * @param locator By locator to find the element
     * @param value Text to type
     */
    @Step("Setting input field value by locator: {1}")
    public static void type(By locator, String value) {
        LoggerUtils.info("Step: Typing value by locator '" + value + "' into: " + locator);
        LoggerUtils.debug("Setting input field value: " + value);
        WebElement inputField = waitForElementToBeVisible(locator);
        type(inputField, value);
    }

    /**
     * Get text from an element with proper waits
     * @param element WebElement to get text from
     * @return The text content of the element
     * Data flow: text read from DOM -> returned to page object -> asserted in tests.
     */
    @Step("Getting text from element")
    public static String getText(WebElement element) {
        LoggerUtils.info("Step: Getting text from element: " + safeLocator(element));
        LoggerUtils.debug("Getting text from element");
        return waitForElementToBeVisible(element).getText();
    }

    /**
     * Get text from an element using By locator
     * @param locator By locator to find the element
     * @return The text content of the element
     */
    @Step("Getting text from element by locator")
    public static String getText(By locator) {
        LoggerUtils.info("Step: Getting text from element by locator: " + locator);
        LoggerUtils.debug("Getting text from element: " + locator);
        return waitForElementToBeVisible(locator).getText();
    }

    /**
     * Check if an element is displayed
     * @param element WebElement to check
     * @return true if the element is displayed, false otherwise
     */
    @Step("Checking if element is displayed")
    public static boolean isDisplayed(WebElement element) {
        LoggerUtils.info("Step: Checking if element is displayed: " + safeLocator(element));
        try {
            return waitForElementToBeVisible(element).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {
            LoggerUtils.debug("Element is not displayed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if an element is displayed using By locator
     * @param locator By locator to find the element
     * @return true if the element is displayed, false otherwise
     */
    @Step("Checking if element is displayed by locator")
    public static boolean isDisplayed(By locator) {
        LoggerUtils.info("Step: Checking if element is displayed by locator: " + locator);
        try {
            return waitForElementToBeVisible(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {
            LoggerUtils.debug("Element is not displayed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Select an option from a dropdown by visible text
     * @param element Select WebElement
     * @param visibleText Text to select
     */
    @Step("Selecting dropdown option: {1}")
    public static void selectByVisibleText(WebElement element, String visibleText) {
        LoggerUtils.info("Step: Selecting dropdown option by visible text '" + visibleText + "' on element: " + safeLocator(element));
        LoggerUtils.debug("Selecting dropdown option: " + visibleText);
        Select select = new Select(waitForElementToBeVisible(element));
        select.selectByVisibleText(visibleText);
    }

    /**
     * Select an option from a dropdown by value
     * @param element Select WebElement
     * @param value Value to select
     */
    @Step("Selecting dropdown option by value: {1}")
    public static void selectByValue(WebElement element, String value) {
        LoggerUtils.info("Step: Selecting dropdown option by value '" + value + "' on element: " + safeLocator(element));
        LoggerUtils.debug("Selecting dropdown option by value: " + value);
        Select select = new Select(waitForElementToBeVisible(element));
        select.selectByValue(value);
    }

    /**
     * Select an option from a custom dropdown (non-select element)
     * @param dropdownElement WebElement that opens the dropdown
     * @param optionLocator By locator to find the option
     */
    @Step("Selecting custom dropdown option")
    public static void selectCustomDropdownOption(WebElement dropdownElement, By optionLocator) {
        LoggerUtils.info("Step: Selecting custom dropdown option '" + optionLocator + "' from element: " + safeLocator(dropdownElement));
        click(dropdownElement);
        click(optionLocator);
    }

    /**
     * Upload a file using a file input element
     * @param element File input WebElement
     * @param filePath Path to the file to upload
     */
    @Step("Uploading file: {1}")
    public static void uploadFile(WebElement element, String filePath) {
        LoggerUtils.info("Step: Uploading file '" + filePath + "' via element: " + safeLocator(element));
        LoggerUtils.debug("Uploading file: " + filePath);
        waitForElementToBePresent(By.xpath("//input[@type='file']"));
        element.sendKeys(new File(filePath).getAbsolutePath());
    }

    /**
     * Move the mouse to hover over an element
     * @param element WebElement to hover over
     */
    @Step("Hovering over element")
    public static void hover(WebElement element) {
        LoggerUtils.info("Step: Hovering over element: " + safeLocator(element));
        LoggerUtils.debug("Hovering over element");
        Actions actions = new Actions(getDriver());
        actions.moveToElement(waitForElementToBeVisible(element)).perform();
    }

    /**
     * Drag and drop an element to a target element
     * @param sourceElement Source WebElement to drag
     * @param targetElement Target WebElement to drop onto
     */
    @Step("Performing drag and drop")
    public static void dragAndDrop(WebElement sourceElement, WebElement targetElement) {
        LoggerUtils.info("Step: Dragging from source: " + safeLocator(sourceElement) + " to target: " + safeLocator(targetElement));
        LoggerUtils.debug("Performing drag and drop operation");
        Actions actions = new Actions(getDriver());
        actions.dragAndDrop(
                waitForElementToBeVisible(sourceElement),
                waitForElementToBeVisible(targetElement)
        ).perform();
    }

    /**
     * Switch to an iframe
     * @param frameElement WebElement representing the iframe
     */
    @Step("Switching to iframe")
    public static void switchToFrame(WebElement frameElement) {
        LoggerUtils.info("Step: Switching to iframe: " + safeLocator(frameElement));
        LoggerUtils.debug("Switching to iframe");
        getDriver().switchTo().frame(waitForElementToBeVisible(frameElement));
    }

    /**
     * Switch back to the default content from an iframe
     */
    @Step("Switching back to default content")
    public static void switchToDefaultContent() {
        LoggerUtils.info("Step: Switching back to default content");
        LoggerUtils.debug("Switching back to default content");
        getDriver().switchTo().defaultContent();
    }

    /**
     * Accept an alert if present
     */
    @Step("Accepting alert")
    public static void acceptAlert() {
        LoggerUtils.info("Step: Accepting alert");
        LoggerUtils.debug("Accepting alert");
        WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_TIMEOUT);
        wait.until(ExpectedConditions.alertIsPresent());
        getDriver().switchTo().alert().accept();
    }

    /**
     * Dismiss an alert if present
     */
    @Step("Dismissing alert")
    public static void dismissAlert() {
        LoggerUtils.info("Step: Dismissing alert");
        LoggerUtils.debug("Dismissing alert");
        WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_TIMEOUT);
        wait.until(ExpectedConditions.alertIsPresent());
        getDriver().switchTo().alert().dismiss();
    }

    /**
     * Execute JavaScript on the page
     * @param script JavaScript to execute
     * @param args Arguments to pass to the script
     * @return The result of the script execution
     */
    @Step("Executing JavaScript")
    public static Object executeJavaScript(String script, Object... args) {
        LoggerUtils.info("Step: Executing JavaScript: " + truncate(script));
        LoggerUtils.debug("Executing JavaScript: " + script);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        return js.executeScript(script, args);
    }

    /**
     * Scroll to an element
     * @param element WebElement to scroll to
     */
    @Step("Scrolling to element")
    public static void scrollToElement(WebElement element) {
        LoggerUtils.info("Step: Scrolling to element: " + safeLocator(element));
        LoggerUtils.debug("Scrolling to element");
        executeJavaScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Wait for a specified condition with a custom timeout
     * @param locator By locator to find the element
     * @param timeout Custom timeout duration
     * @param polling Custom polling interval
     * @return The WebElement once the condition is met
     */
    @Step("Using custom wait for element")
    public static WebElement customWait(By locator, Duration timeout, Duration polling) {
        LoggerUtils.info("Step: Using custom wait for element: " + locator + ", timeout: " + timeout + ", polling: " + polling);
        LoggerUtils.debug("Using custom wait for element: " + locator);
        return new FluentWait<>(getDriver())
                .withTimeout(timeout)
                .pollingEvery(polling)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> d.findElement(locator));
    }

    private static String safeLocator(WebElement element) {
        try {
            return element.toString();
        } catch (Exception e) {
            return "[element]";
        }
    }

    private static String truncate(String script) {
        if (script == null) return "null";
        return script.length() > 100 ? script.substring(0, 100) + "..." : script;
    }
} 