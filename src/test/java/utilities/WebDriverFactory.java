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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Optimized Factory class for creating WebDriver instances
 * Centralizes browser initialization logic with performance optimizations
 *
 * Flow:
 * - Caller: `testBase.BaseClass.initializeDriver()` reads browser/headless from `ConfigManager` and calls `createDriver(browser, headless)`.
 * - Callee: This class sets up the proper WebDriverManager once and returns a concrete WebDriver (Chrome/Firefox/Edge/Safari).
 * - The returned driver is stored in `BaseClass` ThreadLocal and consumed by page objects and utilities.
 *
 * Data:
 * - Inputs: browser name, headless flag.
 * - Output: a configured `WebDriver` instance.
 */
public class WebDriverFactory {
    
    // Cache for WebDriverManager setup to avoid repeated downloads
    private static final ConcurrentHashMap<String, Boolean> driverSetupCache = new ConcurrentHashMap<>();
    private static final ReentrantLock setupLock = new ReentrantLock();
    
    private WebDriverFactory() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Create a new WebDriver instance based on browser type with optimizations
     * @param browser Browser type (chrome, firefox, edge, safari)
     * @param headless Whether to run in headless mode
     * @return WebDriver instance
     */
    public static WebDriver createDriver(String browser, boolean headless) {
        long startTime = System.currentTimeMillis();
        WebDriver driver;
        
        // Setup WebDriverManager only once per browser type
        setupWebDriverManager(browser.toLowerCase());
        
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
        
        long endTime = System.currentTimeMillis();
        LoggerUtils.info("Created " + browser + " driver" + (headless ? " in headless mode" : "") + 
                        " in " + (endTime - startTime) + "ms");
        return driver;
    }
    
    /**
     * Setup WebDriverManager only once per browser type for performance
     * @param browser Browser type
     */
    private static void setupWebDriverManager(String browser) {
        if (!driverSetupCache.containsKey(browser)) {
            setupLock.lock();
            try {
                if (!driverSetupCache.containsKey(browser)) {
                    switch (browser) {
                        case Constants.CHROME:
                            WebDriverManager.chromedriver().setup();
                            break;
                        case Constants.FIREFOX:
                            WebDriverManager.firefoxdriver().setup();
                            break;
                        case Constants.EDGE:
                            WebDriverManager.edgedriver().setup();
                            break;
                        default:
                            // Safari doesn't need WebDriverManager
                            break;
                    }
                    driverSetupCache.put(browser, true);
                    LoggerUtils.debug("WebDriverManager setup completed for: " + browser);
                }
            } finally {
                setupLock.unlock();
            }
        }
    }
    
    /**
     * Create an optimized Chrome WebDriver instance
     * @param headless Whether to run in headless mode
     * @return Chrome WebDriver instance
     */
    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        
        // Performance optimizations
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        options.addArguments("--disable-javascript");
        options.addArguments("--disable-css");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        options.addArguments("--ignore-certificate-errors-spki-list");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-features=TranslateUI");
        options.addArguments("--disable-ipc-flooding-protection");
        
        // Memory optimizations
        options.addArguments("--memory-pressure-off");
        options.addArguments("--max_old_space_size=4096");
        
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }
        
        // Disable logging for performance
        options.addArguments("--log-level=3");
        options.addArguments("--silent");
        
        return new ChromeDriver(options);
    }
    
    /**
     * Create an optimized Firefox WebDriver instance
     * @param headless Whether to run in headless mode
     * @return Firefox WebDriver instance
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        
        // Performance optimizations
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("media.volume_scale", "0.0");
        options.addPreference("dom.push.enabled", false);
        options.addPreference("dom.disable_beforeunload", true);
        options.addPreference("browser.tabs.animate", false);
        options.addPreference("browser.fullscreen.animate", false);
        
        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Create an optimized Edge WebDriver instance
     * @param headless Whether to run in headless mode
     * @return Edge WebDriver instance
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        
        // Performance optimizations similar to Chrome
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-web-security");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--log-level=3");
        
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
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