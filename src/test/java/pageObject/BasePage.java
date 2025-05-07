package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
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
    private static final Duration UI_LOAD_WAIT = Duration.ofMillis(1000);
    

    public BasePage() {
        PageFactory.initElements(BaseClass.getDriver(), this);
    }

    @Step("Waiting for UI to load")
    protected void  waitForUILoad() {
        try {
            Thread.sleep(UI_LOAD_WAIT.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        new WebDriverWait(getDriver(), DEFAULT_TIMEOUT).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    protected WebDriver getDriver() {
        return BaseClass.getDriver();
    }

    @Step("Waiting for element to be clickable")
    protected WebElement waitForElementToBeClickable(WebElement element) {
        LoggerUtils.debug("Waiting for element to be clickable");
        waitForUILoad();
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(StaleElementReferenceException.class, ElementClickInterceptedException.class);

        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Waiting for element to be visible")
    protected WebElement waitForElementToBeVisible(WebElement element) {
        LoggerUtils.debug("Waiting for element to be visible");
        waitForUILoad();
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    @Step("Setting input field value: {1}")
    protected void setInputField(WebElement element, String value) {
        LoggerUtils.debug("Setting input field value: " + value);
        WebElement inputField = waitForElementToBeVisible(element);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value = '';", inputField);
        inputField.sendKeys(value);
        new WebDriverWait(getDriver(), DEFAULT_TIMEOUT)
                .until(ExpectedConditions.attributeToBe(inputField, "value", value));
    }

    @Step("Clicking element")
    protected void clickButton(WebElement element) {
        LoggerUtils.debug("Attempting to click element");
        try {
            waitForUILoad();
            waitForElementToBeClickable(element).click();
        } catch (Exception e) {
            LoggerUtils.warn("Regular click failed, attempting JavaScript click");
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();", element);
        }
    }

    @Step("Selecting dropdown option: {1}")
    protected void selectDropdownByVisibleText(WebElement dropdownElement, String visibleText, String listItemsXPath) {
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
    }

    @Step("Waiting for progress bar to disappear")
    protected void waitForProgressBarToAppear() {
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(PROGRESS_BAR_TIMEOUT)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

        wait.until(d -> isProgressBarDisplayed());
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
        LoggerUtils.debug("Using fluent wait for element: " + locator);
        return new FluentWait<>(getDriver())
                .withTimeout(timeout)
                .pollingEvery(polling)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> d.findElement(locator));
    }

    @Step("Uploading file: {1}")
    protected void uploadFile(WebElement element, String filePath) {
        LoggerUtils.debug("Uploading file: " + filePath);
        element.sendKeys(new File(filePath).getAbsolutePath());
    }

    @Step("Switching to new window")
    protected void switchToNewWindow() {
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
    }

    @Step("Accepting alert")
    protected void acceptAlert() {
        LoggerUtils.debug("Accepting alert");
        new WebDriverWait(getDriver(), DEFAULT_TIMEOUT)
                .until(ExpectedConditions.alertIsPresent()).accept();
    }
}