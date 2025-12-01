package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;
import java.util.Properties;

public class DriverManager {
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    public static void initializeDriver(Properties properties) {
        WebDriver driver;
        String browser = properties.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(properties.getProperty("Headless"));

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOpts = new ChromeOptions();
                if (headless) {
                    chromeOpts.addArguments("--headless=new");
                    chromeOpts.addArguments("--window-size=1920,1080");
                    chromeOpts.addArguments("--disable-gpu");
                    chromeOpts.addArguments("--no-sandbox");
                    chromeOpts.addArguments("--disable-dev-shm-usage");
                }
                driver = new ChromeDriver(chromeOpts);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOpts = new FirefoxOptions();
                if (headless) {
                    firefoxOpts.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOpts);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOpts = new EdgeOptions();
                if (headless) {
                    edgeOpts.addArguments("--headless=new");
                }
                driver = new EdgeDriver(edgeOpts);
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Invalid Browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        threadLocalDriver.set(driver);
    }

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    public static void quitDriver() {
        if (threadLocalDriver.get() != null) {
            threadLocalDriver.get().quit();
            threadLocalDriver.remove();
        }
    }
}
