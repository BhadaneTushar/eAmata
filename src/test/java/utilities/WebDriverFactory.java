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

/**
 * Factory class for creating WebDriver instances
 * Centralizes browser initialization logic
 */
public class WebDriverFactory {
    
    private WebDriverFactory() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Create a new WebDriver instance based on browser type
     * @param browser Browser type (chrome, firefox, edge, safari)
     * @param headless Whether to run in headless mode
     * @return WebDriver instance
     */
    public static WebDriver createDriver(String browser, boolean headless) {
        WebDriver driver;
        
        switch (browser.toLowerCase()) {
            case Constants.CHROME:
                driver = createChromeDriver(headless);
                break;
            case Constants.FIREFOX:
                driver = createFirefoxDriver(headless);
                break;
            case Constants.EDGE:
                driver = createEdgeDriver(headless);
                break;
            case Constants.SAFARI:
                driver = createSafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Invalid Browser: " + browser);
        }
        
        LoggerUtils.info("Created " + browser + " driver" + (headless ? " in headless mode" : ""));
        return driver;
    }
    
    /**
     * Create a Chrome WebDriver instance
     * @param headless Whether to run in headless mode
     * @return Chrome WebDriver instance
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }
        
        return new ChromeDriver(options);
    }
    
    /**
     * Create a Firefox WebDriver instance
     * @param headless Whether to run in headless mode
     * @return Firefox WebDriver instance
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Create an Edge WebDriver instance
     * @param headless Whether to run in headless mode
     * @return Edge WebDriver instance
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        return new EdgeDriver(options);
    }
    
    /**
     * Create a Safari WebDriver instance
     * @return Safari WebDriver instance
     */
    private static WebDriver createSafariDriver() {
        return new SafariDriver();
    }
} 