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

    // Load configuration file Before run tests
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

    // Take browser from config file as per user requirement
    @BeforeMethod
    @Parameters("browser")
    public void initializeDriver(@Optional("edge") String browser) {
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOpts = new ChromeOptions();
                System.out.println("ChromeDriver path: " + WebDriverManager.chromedriver().getDownloadedDriverPath());
                chromeOpts.addArguments(properties.getProperty("Headless"));
                driver = new ChromeDriver(chromeOpts);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOpts = new FirefoxOptions();
                System.out.println("ChromeDriver path: " + WebDriverManager.firefoxdriver().getDownloadedDriverPath());
                // firefoxOpts.addArguments(properties.getProperty("Headless"));
                driver = new FirefoxDriver(new FirefoxOptions());
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Invalid Browser: " + browser);
        }
        threadLocalDriver.set(driver);
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().get(properties.getProperty("url"));

    }

    @BeforeMethod(dependsOnMethods = "initializeDriver")
    public void setUp() {
        SuperAdminLogin loginPage = new SuperAdminLogin();
        String validUsername = properties.getProperty("Username");
        String validPassword = properties.getProperty("Password");
        Assert.assertNotNull(validUsername, "Username is not set in the properties file.");
        Assert.assertNotNull(validPassword, "Password is not set in the properties file.");
        loginPage.login(validUsername, validPassword);
    }

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

    // Screenshot method for Extent report
    public String captureScreen(String testName) throws IOException {
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
        String screenshotPath = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + timestamp + ".png";

        // Create screenshots directory if it doesn't exist
        File screenshotsDir = new File(System.getProperty("user.dir") + "/screenshots");
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }

        TakesScreenshot takesScreenshot = (TakesScreenshot) getDriver();
        File soureFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File targetFile = new File(screenshotPath);
        Files.copy(soureFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return screenshotPath;
    }
}