package utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import testBase.BaseClass;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Utility class for reporting functions used by both Allure and Extent reports
 */
public class ReportUtils {
    private static final String SCREENSHOTS_DIR = Constants.SCREENSHOTS_DIR;
    
    private ReportUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Captures screenshot and returns the file path
     * 
     * @param driver WebDriver instance
     * @param testName Test name for the screenshot file
     * @return Path to the saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            // Ensure directory exists
            createScreenshotDirectory();
            
            // Generate unique filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + "_" + UUID.randomUUID().toString().substring(0, 5) + ".png";
            
            // Create absolute path
            File screenshotsDir = new File(System.getProperty("user.dir"), SCREENSHOTS_DIR);
            if (!screenshotsDir.exists()) {
                boolean created = screenshotsDir.mkdirs();
                LoggerUtils.info("Screenshots directory created: " + created + " - " + screenshotsDir.getAbsolutePath());
            }
            
            String filePath = screenshotsDir.getAbsolutePath() + File.separator + fileName;
            
            // Take screenshot
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File targetFile = new File(filePath);
            FileUtils.copyFile(screenshotFile, targetFile);
            
            LoggerUtils.info("Screenshot captured: " + filePath);
            return filePath;
        } catch (Exception e) {
            LoggerUtils.error("Failed to capture screenshot: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Captures screenshot as bytes for Allure reporting
     * 
     * @param driver WebDriver instance
     * @return Screenshot as byte array
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] captureScreenshotAsBytes(WebDriver driver) {
        return driver != null ? ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES) : null;
    }
    
    /**
     * Attaches screenshot to Allure report
     * 
     * @param driver WebDriver instance
     * @param name Name for the attachment
     */
    public static void attachScreenshotToAllure(WebDriver driver, String name) {
        byte[] screenshot = captureScreenshotAsBytes(driver);
        if (screenshot != null) {
            Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
        }
    }
    
    /**
     * Attaches screenshot to Extent report
     * 
     * @param test ExtentTest instance
     * @param driver WebDriver instance
     * @param status Status for the log entry
     * @param message Message for the log entry
     */
    public static void attachScreenshotToExtent(ExtentTest test, WebDriver driver, Status status, String message) {
        try {
            String screenshotPath = captureScreenshot(driver, "Extent_" + System.currentTimeMillis());
            if (screenshotPath != null) {
                test.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } else {
                test.log(status, message + " (Screenshot failed)");
            }
        } catch (Exception e) {
            test.log(status, message + " (Screenshot failed: " + e.getMessage() + ")");
            LoggerUtils.error("Failed to attach screenshot to Extent report: " + e.getMessage(), e);
        }
    }
    
    /**
     * Logs test failure details to both Allure and Extent reports
     * 
     * @param result TestNG test result
     * @param test ExtentTest instance
     */
    public static void logFailureDetails(ITestResult result, ExtentTest test) {
        // Get failure details
        Throwable throwable = result.getThrowable();
        String failureMessage = throwable != null ? throwable.getMessage() : "Test failed without exception";
        String methodName = result.getMethod().getMethodName();
        
        // Log to Extent
        test.log(Status.FAIL, "Test Failed: " + methodName);
        test.log(Status.FAIL, failureMessage);
        if (throwable != null) {
            test.log(Status.FAIL, throwable);
        }
        
        // Log to Allure
        Allure.step("Test Failed: " + methodName);
        if (throwable != null) {
            saveTextLog(throwable.toString());
        }
        
        // Capture and attach screenshot
        WebDriver driver = BaseClass.getDriver();
        if (driver != null) {
            // For Allure
            attachScreenshotToAllure(driver, "Failure Screenshot: " + methodName);
            
            // For Extent
            try {
                String screenshotPath = captureScreenshot(driver, methodName);
                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath);
                }
            } catch (Exception e) {
                LoggerUtils.error("Failed to add screenshot to Extent report: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Creates screenshots directory if it doesn't exist
     */
    private static void createScreenshotDirectory() {
        Path path = Paths.get(System.getProperty("user.dir"), SCREENSHOTS_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                LoggerUtils.info("Created screenshots directory: " + path.toAbsolutePath());
            } catch (IOException e) {
                LoggerUtils.error("Failed to create screenshots directory: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Saves text log as Allure attachment
     * 
     * @param message Message to save
     * @return The message
     */
    @Attachment(value = "Error Details", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }
    
    /**
     * Logs a step to both Allure and Extent reports
     * 
     * @param test ExtentTest instance
     * @param status Status for Extent report
     * @param stepName Step name/description
     */
    public static void logStep(ExtentTest test, Status status, String stepName) {
        // Log to Extent
        test.log(status, stepName);
        
        // Log to Allure
        Allure.step(stepName);
        
        // Log to Log4j
        LoggerUtils.info(stepName);
    }
} 