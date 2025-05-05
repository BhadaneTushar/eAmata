package testBase;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.SuperAdminLogin;
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

/**
 * Base class for all test classes.
 * Handles WebDriver initialization, configuration loading, and common test
 * setup.
 */
public class BaseClass {
    private static final String CONFIG_FILE_PATH = "./src/test/resources/config.properties";
    private static final String SCREENSHOTS_DIR = "screenshots";
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);

    protected static Properties properties;
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    /**
     * Gets the WebDriver instance for the current thread.
     * 
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    /**
     * Loads configuration properties before test suite execution.
     * 
     * @throws RuntimeException if configuration loading fails
     */
    @BeforeSuite
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

    /**
     * Initializes WebDriver based on the specified browser.
     * 
     * @param browser The browser to use (chrome, firefox, edge, safari)
     * @throws RuntimeException if WebDriver initialization fails
     */
    @BeforeMethod
    @Parameters("browser")
    @Step("Initializing WebDriver for browser: {0}")
    public void initializeDriver(@Optional("chrome") String browser) {
        try {
            WebDriver driver = createDriver(browser);
            threadLocalDriver.set(driver);
            configureDriver();
            navigateToBaseUrl();
            LoggerUtils.info("WebDriver initialized successfully for browser: " + browser);
        } catch (Exception e) {
            LoggerUtils.error("Failed to initialize WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    /**
     * Creates a WebDriver instance for the specified browser.
     * 
     * @param browser The browser to create driver for
     * @return WebDriver instance
     * @throws RuntimeException if driver creation fails
     */
    private WebDriver createDriver(String browser) {
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    return new ChromeDriver(new ChromeOptions());
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    return new FirefoxDriver(new FirefoxOptions());
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    return new EdgeDriver();
                case "safari":
                    return new SafariDriver();
                default:
                    throw new IllegalArgumentException("Invalid Browser: " + browser);
            }
        } catch (Exception e) {
            LoggerUtils.error("Failed to create WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to create WebDriver", e);
        }
    }

    /**
     * Configures the WebDriver with common settings.
     * 
     * @throws RuntimeException if driver configuration fails
     */
    private void configureDriver() {
        try {
            getDriver().manage().window().maximize();
            getDriver().manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
        } catch (Exception e) {
            LoggerUtils.error("Failed to configure WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to configure WebDriver", e);
        }
    }

    /**
     * Navigates to the base URL specified in properties.
     * 
     * @throws RuntimeException if navigation fails
     */
    private void navigateToBaseUrl() {
        try {
            String url = properties.getProperty("url");
            if (url == null || url.isEmpty()) {
                throw new IllegalStateException("Base URL is not configured in properties file");
            }
            getDriver().get(url);
            LoggerUtils.info("Navigated to URL: " + url);
        } catch (Exception e) {
            LoggerUtils.error("Failed to navigate to base URL: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to base URL", e);
        }
    }

    /**
     * Performs login with super admin credentials.
     * 
     * @throws RuntimeException if login fails
     */
    @Step("Performing super admin login")
    public void performSuperAdminLogin() {
        try {
            SuperAdminLogin loginPage = new SuperAdminLogin();
            String username = properties.getProperty("Username");
            String password = properties.getProperty("Password");

            Assert.assertNotNull(username, "Username is not set in the properties file");
            Assert.assertNotNull(password, "Password is not set in the properties file");

            loginPage.login(username, password);
            LoggerUtils.info("Super Admin login performed successfully");
        } catch (Exception e) {
            LoggerUtils.error("Failed to perform super admin login: " + e.getMessage());
            throw new RuntimeException("Failed to perform super admin login", e);
        }
    }

    /**
     * Cleans up WebDriver resources after test execution.
     * 
     * @throws RuntimeException if cleanup fails
     */
    @AfterMethod
    @Step("Cleaning up WebDriver resources")
    public void tearDown() {
        try {
            if (getDriver() != null) {
                getDriver().quit();
                threadLocalDriver.remove();
                LoggerUtils.info("WebDriver resources cleaned up");
            }
        } catch (Exception e) {
            LoggerUtils.error("Failed to clean up WebDriver resources: " + e.getMessage());
            throw new RuntimeException("Failed to clean up WebDriver resources", e);
        }
    }

    /**
     * Captures screenshot and saves it to the screenshots directory.
     * 
     * @param testName Name of the test for which screenshot is being taken
     * @return Path to the saved screenshot
     * @throws RuntimeException if screenshot capture or save fails
     */
    @Step("Capturing screenshot for test: {0}")
    public String captureScreen(String testName) {
        try {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
            String screenshotsDir = System.getProperty("user.dir") + "/" + SCREENSHOTS_DIR + "/";

            File dir = new File(screenshotsDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Failed to create screenshots directory");
            }

            String screenshotPath = screenshotsDir + testName + "_" + timestamp + ".png";
            TakesScreenshot takesScreenshot = (TakesScreenshot) getDriver();
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File targetFile = new File(screenshotPath);

            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            LoggerUtils.info("Screenshot captured: " + screenshotPath);

            return screenshotPath;
        } catch (Exception e) {
            LoggerUtils.error("Failed to capture screenshot: " + e.getMessage());
            throw new RuntimeException("Failed to capture screenshot", e);
        }
    }
}