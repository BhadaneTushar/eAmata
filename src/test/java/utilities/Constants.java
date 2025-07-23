package utilities;

import java.time.Duration;

/**
 * Centralized constants for the entire framework
 * Helps eliminate hard-coded values throughout the codebase
 */
public final class Constants {
    
    // Private constructor to prevent instantiation
    private Constants() {
    }
    
    // File paths
    public static final String CONFIG_FILE_PATH = "./src/test/resources/config.properties";
    public static final String SCREENSHOTS_DIR = "screenshots";
    public static final String REPORTS_DIR = "reports";
    public static final String TESTDATA_DIR = "testdata";
    
    // Optimized Timeouts for better performance
    public static final Duration IMPLICIT_WAIT = Duration.ofSeconds(5);
    public static final Duration EXPLICIT_WAIT = Duration.ofSeconds(15);
    public static final Duration POLLING_INTERVAL = Duration.ofMillis(250);
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(20);
    public static final Duration SCRIPT_TIMEOUT = Duration.ofSeconds(15);
    public static final Duration FAST_WAIT = Duration.ofSeconds(5);
    public static final Duration MEDIUM_WAIT = Duration.ofSeconds(10);
    public static final Duration SLOW_WAIT = Duration.ofSeconds(30);
    
    // Common XPaths
    public static final String PROGRESS_BAR_XPATH = "//div[span[@role='progressbar']]";
    public static final String DROPDOWN_OPTIONS_XPATH = "//ul[@role='listbox']/li";
    
    // Browser options
    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String EDGE = "edge";
    public static final String SAFARI = "safari";
    
    // Configuration keys
    public static final String BROWSER_KEY = "browser";
    public static final String URL_KEY = "url";
    public static final String HEADLESS_KEY = "Headless";
    public static final String USERNAME_KEY = "Username";
    public static final String PASSWORD_KEY = "Password";
    
    // Common test data
    public static final String DEFAULT_STATE = "Arizona";
    public static final String DEFAULT_GENDER = "Male";
    
    // Error messages
    public static final String DRIVER_INIT_ERROR = "Failed to initialize WebDriver";
    public static final String CONFIG_LOAD_ERROR = "Failed to load configuration file";
    public static final String ELEMENT_NOT_FOUND_ERROR = "Element not found: ";
    public static final String ELEMENT_NOT_CLICKABLE_ERROR = "Element not clickable: ";
    public static final String DROPDOWN_OPTION_NOT_FOUND_ERROR = "Dropdown option not found: ";
    
    // Validation error messages
    public static final String FIRST_NAME_REQUIRED = "First Name is required";
    public static final String LAST_NAME_REQUIRED = "Last Name is required";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String PHONE_REQUIRED = "Phone is required";
    public static final String INVALID_PHONE_NUMBER = "Invalid phone number. It must be 10 digits.";
    
    // Assertion messages
    public static final String LOGIN_SUCCESS_MESSAGE = "Login successful";
    public static final String ELEMENT_VISIBLE_MESSAGE = "Element is visible";
    public static final String ELEMENT_NOT_VISIBLE_MESSAGE = "Element is not visible";
} 