package testBase;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utilities.DriverManager;
import utilities.LoggerUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class BaseClass {
    private static final String CONFIG_FILE_PATH = "./src/test/resources/config.properties";
    private static final String SCREENSHOTS_DIR = "screenshots";
    private static final Duration EXPLICIT_WAIT = Duration.ofSeconds(20);
    private static final ThreadLocal<WebDriverWait> threadLocalWait = new ThreadLocal<>();
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    public static Properties properties;

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    public static WebDriverWait getWait() {
        return threadLocalWait.get();
    }

    @BeforeSuite(alwaysRun = true)
    @Step("Loading configuration")
    public void loadConfig() {
        try {
            properties = new Properties();
            try (FileReader file = new FileReader(CONFIG_FILE_PATH)) {
                properties.load(file);
                LoggerUtils.info("Configuration loaded successfully");
            }
        } catch (IOException e) {
            LoggerUtils.error("Failed to load configuration file: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void initializeDriver() {
        try {
            DriverManager.initializeDriver(properties);
            threadLocalDriver.set(DriverManager.getDriver());
            threadLocalWait.set(new WebDriverWait(DriverManager.getDriver(), EXPLICIT_WAIT));

            getDriver().get(properties.getProperty("url"));
            waitForPageLoad();
        } catch (Exception e) {
            LoggerUtils.error("Failed to initialize WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    @AfterMethod(alwaysRun = true)
    @Step("Cleaning up WebDriver resources")
    public void tearDown() {
        try {
            DriverManager.quitDriver();
            threadLocalDriver.remove();
            threadLocalWait.remove();
            LoggerUtils.info("WebDriver resources cleaned up successfully");
        } catch (Exception e) {
            LoggerUtils.error("Failed to clean up WebDriver resources: " + e.getMessage());
        }
    }

    // Helper methods for waiting
    public void waitForPageLoad() {
        try {
            getWait().until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            LoggerUtils.info("Page load completed");
        } catch (TimeoutException e) {
            LoggerUtils.error("Timeout waiting for page load: " + e.getMessage());
        }
    }

    public void waitForElementToBeClickable(By locator) {
        try {
            getWait().until(ExpectedConditions.elementToBeClickable(locator));
            LoggerUtils.info("Element is clickable: " + locator);
        } catch (TimeoutException e) {
            LoggerUtils.error("Timeout waiting for element to be clickable: " + locator);
            throw e;
        }
    }

    public void waitForElementToBeVisible(By locator) {
        try {
            getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
            LoggerUtils.info("Element is visible: " + locator);
        } catch (TimeoutException e) {
            LoggerUtils.error("Timeout waiting for element to be visible: " + locator);
            throw e;
        }
    }

    public void waitForElementToBePresent(By locator) {
        try {
            getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
            LoggerUtils.info("Element is present: " + locator);
        } catch (TimeoutException e) {
            LoggerUtils.error("Timeout waiting for element to be present: " + locator);
            throw e;
        }
    }

    public String captureScreen(String testName) throws IOException {
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
        String screenshotPath = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + timestamp + ".png";

        File screenshotsDir = new File(System.getProperty("user.dir") + "/screenshots");
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }

        TakesScreenshot takesScreenshot = (TakesScreenshot) getDriver();
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File targetFile = new File(screenshotPath);
        Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return screenshotPath;
    }
}