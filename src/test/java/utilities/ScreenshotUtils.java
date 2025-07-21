package utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import testBase.BaseClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for handling screenshots
 * @deprecated Use {@link ReportUtils} for new screenshot functionality
 */
@Deprecated
public class ScreenshotUtils {
    
    private ScreenshotUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Take a screenshot and save it to the screenshots directory
     * @param testName Name of the test
     * @return Path to the screenshot file
     * @throws IOException If there's an error saving the screenshot
     * @deprecated Use {@link ReportUtils#captureScreenshot(WebDriver, String)} instead
     */
    @Deprecated
    public static String captureScreenshot(String testName) throws IOException {
        WebDriver driver = BaseClass.getDriver();
        return ReportUtils.captureScreenshot(driver, testName);
    }
    
    /**
     * Take a screenshot and return it as a byte array for Allure reporting
     * @return Screenshot as byte array
     * @deprecated Use {@link ReportUtils#captureScreenshotAsBytes(WebDriver)} instead
     */
    @Deprecated
    public static byte[] captureScreenshotAsBytes() {
        WebDriver driver = BaseClass.getDriver();
        return ReportUtils.captureScreenshotAsBytes(driver);
    }
    
    /**
     * Take a screenshot and return it as a base64 string
     * @return Screenshot as base64 string
     * @deprecated Use WebDriver's getScreenshotAs(OutputType.BASE64) directly
     */
    @Deprecated
    public static String captureScreenshotAsBase64() {
        WebDriver driver = BaseClass.getDriver();
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        return takesScreenshot.getScreenshotAs(OutputType.BASE64);
    }
} 