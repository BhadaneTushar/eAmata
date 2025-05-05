package testBase;

import io.github.bonigarcia.wdm.WebDriverManager;
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

    public static Properties properties;
    private static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    // Load configuration file Before run tests
    @BeforeSuite
    public void loadConfig() {
        properties = new Properties();
        try (FileReader file = new FileReader("./src/test/resources/config.properties")) {
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                chromeOpts.addArguments("--disable-gpu");
                chromeOpts.addArguments("--no-sandbox"); // Sometimes needed in specific environments
                chromeOpts.addArguments("--remote-debugging-port=9222");
                System.out.println("ChromeDriver path: " + WebDriverManager.chromedriver().getDownloadedDriverPath());
                chromeOpts.addArguments(properties.getProperty("Headless"));
                // chromeOpts.addArguments(properties.getProperty("Resolution"));
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
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            threadLocalDriver.remove();
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