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


public class BasePage {
    private static final String PROGRESS_BAR_XPATH = "//div[span[@role='progressbar']]";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration PROGRESS_BAR_TIMEOUT = Duration.ofSeconds(60);
    private static final Duration POLLING_INTERVAL = Duration.ofMillis(500);

    public BasePage() {
        PageFactory.initElements(BaseClass.getDriver(), this);
    }

    protected WebDriver getDriver() {
        return BaseClass.getDriver();
    }

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

    @Step("Waiting for progress bar to disappear")
    protected void waitForProgressBarToAppear() {
            FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                    .withTimeout(PROGRESS_BAR_TIMEOUT)
                    .pollingEvery(POLLING_INTERVAL)
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

            // Wait for progress bar to disappear
            wait.until(d -> {
                    return isProgressBarDisplayed();
            });

            LoggerUtils.debug("Progress bar operation completed");

    }

    public boolean isProgressBarDisplayed() {
            WebElement progressBar = getDriver().findElement(By.xpath(PROGRESS_BAR_XPATH));
            return progressBar.isDisplayed();
    }

    @Step("Checking if element is displayed")
    protected boolean isElementDisplayed(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

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

    // Drag and Drop
    public void dragAndDrop(WebElement source, WebElement target) {
        new Actions(getDriver()).dragAndDrop(source, target).build().perform();
    }

    public void hoverOverElement(WebElement element) {
        new Actions(getDriver()).moveToElement(element).build().perform();
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                element);
    }
}