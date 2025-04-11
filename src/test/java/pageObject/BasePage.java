package pageObject;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import testBase.BaseClass;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class BasePage {

    private static final String PROGRESS_BAR_XPATH = "//div[span[@role='progressbar']][contains(@style, 'visibility: hidden')]";
    private final int defaultTimeout = 10;

    public BasePage() {
        PageFactory.initElements(BaseClass.getDriver(), this);
    }

    public WebDriver getDriver() {
        return BaseClass.getDriver();
    }

    public WebElement waitForElementToBeClickable(WebElement element) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(defaultTimeout))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForElementToBeVisible(WebElement element) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(defaultTimeout))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void setInputField(WebElement element, String value) {
        WebElement inputField = waitForElementToBeVisible(element);
        inputField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        inputField.sendKeys(value);
    }

    public void clickButton(WebElement element) {
        try {
            waitForElementToBeClickable(element).click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();", element);
        }
    }


    public void selectDropdownByVisibleText(WebElement dropdownElement, String visibleText, String listItemsXPath) {
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

    public void waitForProgressBarToAppear() {
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                    .withTimeout(Duration.ofSeconds(5))
                    .pollingEvery(Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

            wait.until(d -> isProgressBarDisplayed());
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for progress bar to appear.");
        }
    }

    public boolean isProgressBarDisplayed() {
        try {
            WebElement progressBar = getDriver().findElement(By.xpath(PROGRESS_BAR_XPATH));
            return progressBar.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public WebElement fluentWait(By locator, Duration timeout, Duration polling) {
        return new FluentWait<>(getDriver())
                .withTimeout(timeout)
                .pollingEvery(polling)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> d.findElement(locator));
    }

    // Upload file
    public void uploadFile(WebElement element, String filePath) {
        element.sendKeys(new File(filePath).getAbsolutePath());
    }

    // Switch to new tab/window
    public void switchToNewWindow() {
        String originalWindow = getDriver().getWindowHandle();
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfWindowsToBe(2));
        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }
    }

    // Accept/dismiss alert
    public void acceptAlert() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.alertIsPresent()).accept();
    }

    //Drag and Drop
    public void dragAndDrop(WebElement source, WebElement target) {
        new Actions(getDriver()).dragAndDrop(source, target).build().perform();
    }

    public void hoverOverElement(WebElement element) {
        new Actions(getDriver()).moveToElement(element).build().perform();
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                element
        );
    }
}
