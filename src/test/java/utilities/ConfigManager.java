package utilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Manages configuration properties for the framework
 * Provides type-safe access to configuration values
 *
 * Flow:
 * - Caller: `testBase.BaseClass.loadConfig()` initializes the singleton early (@BeforeSuite).
 * - Consumers: tests (via `BaseClass.getConfigManager()`), utilities (e.g., `LoginUtils`), and listeners may query properties.
 *
 * Data:
 * - Inputs: properties file at `Constants.CONFIG_FILE_PATH` (URL, browser, headless, credentials, defaults).
 * - Outputs: getters return typed values to callers; no mutation during runtime.
 */
public class ConfigManager {
    private static final Properties properties = new Properties();
    private static ConfigManager instance;
    
    private ConfigManager() {
        loadConfig();
    }
    
    /**
     * Get the singleton instance of ConfigManager
     * @return ConfigManager instance
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Load configuration from properties file
     */
    private void loadConfig() {
        try (FileReader file = new FileReader(Constants.CONFIG_FILE_PATH)) {
            properties.load(file);
            LoggerUtils.info("Configuration loaded successfully");
        } catch (IOException e) {
            LoggerUtils.error(Constants.CONFIG_LOAD_ERROR + ": " + e.getMessage());
            throw new RuntimeException(Constants.CONFIG_LOAD_ERROR, e);
        }
    }
    
    /**
     * Get a string property value
     * @param key Property key
     * @return Property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get a string property value with default
     * @param key Property key
     * @param defaultValue Default value if property is not found
     * @return Property value or default
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get a boolean property value
     * @param key Property key
     * @return Boolean property value
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
    
    /**
     * Get a boolean property value with default
     * @param key Property key
     * @param defaultValue Default value if property is not found
     * @return Boolean property value or default
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return (value != null) ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    /**
     * Get an integer property value
     * @param key Property key
     * @return Integer property value
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
    
    /**
     * Get an integer property value with default
     * @param key Property key
     * @param defaultValue Default value if property is not found
     * @return Integer property value or default
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return (value != null) ? Integer.parseInt(value) : defaultValue;
    }
    
    /**
     * Get the application URL
     * @return Application URL
     */
    public String getUrl() {
        return getProperty(Constants.URL_KEY);
    }
    
    /**
     * Get the browser type
     * @return Browser type
     */
    public String getBrowser() {
        return getProperty(Constants.BROWSER_KEY, Constants.CHROME);
    }
    
    /**
     * Check if headless mode is enabled
     * @return true if headless mode is enabled
     */
    public boolean isHeadless() {
        return getBooleanProperty(Constants.HEADLESS_KEY, false);
    }
    
    /**
     * Get the username for login
     * @return Username
     */
    public String getUsername() {
        return getProperty(Constants.USERNAME_KEY);
    }
    
    /**
     * Get the password for login
     * @return Password
     */
    public String getPassword() {
        return getProperty(Constants.PASSWORD_KEY);
    }
} 