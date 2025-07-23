package testBase;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utilities.*;

import java.io.IOException;
import java.time.Duration;

/**
 * Base class for all test classes
 * Handles WebDriver initialization and cleanup
 */
public class BaseClass {
    private static final ThreadLocal<WebDriverWait> threadLocalWait = new ThreadLocal<>();
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    private static ConfigManager configManager;

    /**
     * Get the WebDriver instance for the current thread
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    /**
     * Get the WebDriverWait instance for the current thread
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWait() {
        return threadLocalWait.get();
    }

    /**
     * Get the ConfigManager instance
     * @return ConfigManager instance
     */
    public static ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Initialize the ConfigManager before any tests run
     */
    @BeforeSuite(alwaysRun = true)
    @Step("Loading configuration")
    public void loadConfig() {
        configManager = ConfigManager.getInstance();
        LoggerUtils.info("Configuration loaded successfully");
    }

    /**
     * Initialize the WebDriver before each test method with performance monitoring
     */
    @BeforeMethod(alwaysRun = true)
    @Step("Initializing WebDriver")
    public void initializeDriver() {
        long startTime = System.currentTimeMillis();
        try {
            // Create WebDriver instance
            String browser = configManager.getBrowser();
            boolean headless = configManager.isHeadless();
            WebDriver driver = WebDriverFactory.createDriver(browser, headless);
            threadLocalDriver.set(driver);
            
            // Create WebDriverWait instance
            threadLocalWait.set(new WebDriverWait(driver, Constants.EXPLICIT_WAIT));
            
            // Configure browser window with optimized timeouts
            if (!headless) {
                driver.manage().window().maximize();
            }
            driver.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT);
            driver.manage().timeouts().pageLoadTimeout(Constants.PAGE_LOAD_TIMEOUT);
            driver.manage().timeouts().scriptTimeout(Constants.SCRIPT_TIMEOUT);
            
            // Navigate to application URL
            driver.get(configManager.getUrl());
            ElementActions.waitForPageLoad();
            
            long endTime = System.currentTimeMillis();
            LoggerUtils.info("WebDriver initialized successfully in " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            LoggerUtils.error(Constants.DRIVER_INIT_ERROR + ": " + e.getMessage());
            throw new RuntimeException(Constants.DRIVER_INIT_ERROR, e);
        }
    }

    /**
     * Clean up WebDriver resources after each test method
     */
    @AfterMethod(alwaysRun = true)
    @Step("Cleaning up WebDriver resources")
    public void tearDown() {
        try {
            WebDriver driver = getDriver();
            if (driver != null) {
                driver.quit();
                threadLocalDriver.remove();
                threadLocalWait.remove();
                LoggerUtils.info("WebDriver resources cleaned up successfully");
            }
        } catch (Exception e) {
            LoggerUtils.error("Failed to clean up WebDriver resources: " + e.getMessage());
        }
    }
}