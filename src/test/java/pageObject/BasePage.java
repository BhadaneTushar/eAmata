package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import testBase.BaseClass;
import utilities.LoggerUtils;

import java.io.File;
import java.time.Duration;
import java.util.List;

/**
 * Base class for all page objects.
 * Provides common web element interactions and wait mechanisms.
 */
public class BasePage {
    private static final String PROGRESS_BAR_XPATH = "//div[span[@role='progressbar']]";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration PROGRESS_BAR_TIMEOUT = Duration.ofSeconds(60);
    private static final Duration POLLING_INTERVAL = Duration.ofMillis(500);

    public BasePage() {
        PageFactory.initElements(BaseClass.getDriver(), this);
        LoggerUtils.debug("Initialized page object: " + this.getClass().getSimpleName());
    }

    /**
     * Gets the WebDriver instance.
     * 
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return BaseClass.getDriver();
    }

    /**
     * Waits for an element to be clickable.
     * 
     * @param element The element to wait for
     * @return The clickable element
     * @throws RuntimeException if element is not clickable within timeout
     */
    @Step("Waiting for element to be clickable")
    protected WebElement waitForElementToBeClickable(WebElement element) {
        try {
            LoggerUtils.debug("Waiting for element to be clickable");
            return new WebDriverWait(getDriver(), DEFAULT_TIMEOUT)
                    .until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            LoggerUtils.error("Failed to wait for element to be clickable: " + e.getMessage());
            throw new RuntimeException("Failed to wait for element to be clickable", e);
        }
    }

    /**
     * Waits for an element to be visible.
     * 
     * @param element The element to wait for
     * @return The visible element
     * @throws RuntimeException if element is not visible within timeout
     */
    @Step("Waiting for element to be visible")
    protected WebElement waitForElementToBeVisible(WebElement element) {
        try {
            LoggerUtils.debug("Waiting for element to be visible");
            return new WebDriverWait(getDriver(), DEFAULT_TIMEOUT)
                    .until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            LoggerUtils.error("Failed to wait for element to be visible: " + e.getMessage());
            throw new RuntimeException("Failed to wait for element to be visible", e);
        }
    }

    /**
     * Sets the value of an input field after clearing it.
     * 
     * @param element The input field element
     * @param value   The value to set
     * @throws RuntimeException if setting input field fails
     */
    @Step("Setting input field value: {1}")
    protected void setInputField(WebElement element, String value) {
        try {
            LoggerUtils.debug("Setting input field value: " + value);
            WebElement inputField = waitForElementToBeVisible(element);
            inputField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            inputField.sendKeys(value);
        } catch (Exception e) {
            LoggerUtils.error("Failed to set input field value: " + e.getMessage());
            throw new RuntimeException("Failed to set input field value", e);
        }
    }

    /**
     * Clicks an element, using JavaScript if regular click fails.
     * 
     * @param element The element to click
     * @throws RuntimeException if clicking element fails
     */
    @Step("Clicking element")
    protected void clickButton(WebElement element) {
        try {
            LoggerUtils.debug("Attempting to click element");
            waitForElementToBeClickable(element).click();
        } catch (Exception e) {
            LoggerUtils.warn("Regular click failed, attempting JavaScript click");
            try {
                JavascriptExecutor js = (JavascriptExecutor) getDriver();
                js.executeScript("arguments[0].click();", element);
            } catch (Exception ex) {
                LoggerUtils.error("Failed to click element: " + ex.getMessage());
                throw new RuntimeException("Failed to click element", ex);
            }
        }
    }

    /**
     * Selects an option from a dropdown by visible text.
     * 
     * @param dropdownElement The dropdown element
     * @param visibleText     The text to select
     * @param listItemsXPath  XPath for dropdown items
     * @throws RuntimeException if option not found
     */
    @Step("Selecting dropdown option: {1}")
    protected void selectDropdownByVisibleText(WebElement dropdownElement, String visibleText, String listItemsXPath) {
        try {
            LoggerUtils.debug("Selecting dropdown option: " + visibleText);
            dropdownElement.click();
            List<WebElement> dropdownItems = getDriver().findElements(By.xpath(listItemsXPath));
            for (WebElement item : dropdownItems) {
                if (item.getText().equalsIgnoreCase(visibleText)) {
                    item.click();
                    return;
                }
            }
            throw new RuntimeException("Dropdown item not found: " + visibleText);
        } catch (Exception e) {
            LoggerUtils.error("Failed to select dropdown option: " + e.getMessage());
            throw new RuntimeException("Failed to select dropdown option", e);
        }
    }

    /**
     * Waits for a progress bar to appear and disappear.
     * 
     * @throws RuntimeException if waiting for progress bar fails
     */
    @Step("Waiting for progress bar")
    protected void waitForProgressBarToAppear() {
        try {
            LoggerUtils.debug("Waiting for progress bar");
            FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                    .withTimeout(PROGRESS_BAR_TIMEOUT)
                    .pollingEvery(POLLING_INTERVAL)
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

            // Wait for progress bar to appear
            wait.until(d -> {
                try {
                    WebElement progressBar = d.findElement(By.xpath(PROGRESS_BAR_XPATH));
                    return progressBar.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            });

            // Wait for progress bar to disappear
            wait.until(d -> {
                try {
                    WebElement progressBar = d.findElement(By.xpath(PROGRESS_BAR_XPATH));
                    return !progressBar.isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            });

            LoggerUtils.debug("Progress bar operation completed");
        } catch (Exception e) {
            LoggerUtils.error("Failed to handle progress bar: " + e.getMessage());
            throw new RuntimeException("Failed to handle progress bar", e);
        }
    }

    /**
     * Checks if progress bar is displayed.
     * 
     * @return true if progress bar is displayed
     */
    protected boolean isProgressBarDisplayed() {
        try {
            WebElement progressBar = getDriver().findElement(By.xpath(PROGRESS_BAR_XPATH));
            return progressBar.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Checks if an element is displayed.
     * 
     * @param element The element to check
     * @return true if element is displayed
     */
    @Step("Checking if element is displayed")
    protected boolean isElementDisplayed(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Waits for an element using FluentWait.
     * 
     * @param locator The element locator
     * @param timeout The timeout duration
     * @param polling The polling interval
     * @return The found element
     * @throws RuntimeException if element not found within timeout
     */
    @Step("Using fluent wait for element")
    protected WebElement fluentWait(By locator, Duration timeout, Duration polling) {
        try {
            LoggerUtils.debug("Using fluent wait for element: " + locator);
            return new FluentWait<>(getDriver())
                    .withTimeout(timeout)
                    .pollingEvery(polling)
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> d.findElement(locator));
        } catch (Exception e) {
            LoggerUtils.error("Failed to find element using fluent wait: " + e.getMessage());
            throw new RuntimeException("Failed to find element using fluent wait", e);
        }
    }

    /**
     * Uploads a file to an input element.
     * 
     * @param element  The file input element
     * @param filePath The path to the file
     * @throws RuntimeException if file upload fails
     */
    @Step("Uploading file: {1}")
    protected void uploadFile(WebElement element, String filePath) {
        try {
            LoggerUtils.debug("Uploading file: " + filePath);
            element.sendKeys(new File(filePath).getAbsolutePath());
        } catch (Exception e) {
            LoggerUtils.error("Failed to upload file: " + e.getMessage());
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    /**
     * Switches to a new window/tab.
     * 
     * @throws RuntimeException if switching to new window fails
     */
    @Step("Switching to new window")
    protected void switchToNewWindow() {
        try {
            LoggerUtils.debug("Switching to new window");
            String originalWindow = getDriver().getWindowHandle();
            new WebDriverWait(getDriver(), DEFAULT_TIMEOUT)
                    .until(ExpectedConditions.numberOfWindowsToBe(2));
            for (String windowHandle : getDriver().getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    getDriver().switchTo().window(windowHandle);
                    break;
                }
            }
        } catch (Exception e) {
            LoggerUtils.error("Failed to switch to new window: " + e.getMessage());
            throw new RuntimeException("Failed to switch to new window", e);
        }
    }

    /**
     * Accepts an alert dialog.
     * 
     * @throws RuntimeException if accepting alert fails
     */
    @Step("Accepting alert")
    protected void acceptAlert() {
        try {
            LoggerUtils.debug("Accepting alert");
            new WebDriverWait(getDriver(), DEFAULT_TIMEOUT)
                    .until(ExpectedConditions.alertIsPresent()).accept();
        } catch (Exception e) {
            LoggerUtils.error("Failed to accept alert: " + e.getMessage());
            throw new RuntimeException("Failed to accept alert", e);
        }
    }

    /**
     * Performs drag and drop operation.
     * 
     * @param source The source element
     * @param target The target element
     * @throws RuntimeException if drag and drop fails
     */
    @Step("Performing drag and drop")
    protected void dragAndDrop(WebElement source, WebElement target) {
        try {
            LoggerUtils.debug("Performing drag and drop");
            new Actions(getDriver()).dragAndDrop(source, target).build().perform();
        } catch (Exception e) {
            LoggerUtils.error("Failed to perform drag and drop: " + e.getMessage());
            throw new RuntimeException("Failed to perform drag and drop", e);
        }
    }

    /**
     * Hovers over an element.
     * 
     * @param element The element to hover over
     * @throws RuntimeException if hovering fails
     */
    @Step("Hovering over element")
    protected void hoverOverElement(WebElement element) {
        try {
            LoggerUtils.debug("Hovering over element");
            new Actions(getDriver()).moveToElement(element).build().perform();
        } catch (Exception e) {
            LoggerUtils.error("Failed to hover over element: " + e.getMessage());
            throw new RuntimeException("Failed to hover over element", e);
        }
    }

    /**
     * Scrolls to an element.
     * 
     * @param element The element to scroll to
     * @throws RuntimeException if scrolling fails
     */
    @Step("Scrolling to element")
    protected void scrollToElement(WebElement element) {
        try {
            LoggerUtils.debug("Scrolling to element");
            ((JavascriptExecutor) getDriver()).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                    element);
        } catch (Exception e) {
            LoggerUtils.error("Failed to scroll to element: " + e.getMessage());
            throw new RuntimeException("Failed to scroll to element", e);
        }
    }
}
