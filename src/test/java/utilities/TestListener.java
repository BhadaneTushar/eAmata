package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import testBase.BaseClass;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Advanced TestNG listener for both Allure and Extent reporting
 */
public class TestListener implements ITestListener, ISuiteListener, IInvokedMethodListener {
    private static final String REPORTS_DIR = "reports";
    private static final String SCREENSHOTS_DIR = Constants.SCREENSHOTS_DIR;
    
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();
    private static ExtentReports extentReports;
    
    // Store test class and method start times
    private final Map<String, Long> testClassStartTime = new HashMap<>();
    private final Map<String, Long> testMethodStartTime = new HashMap<>();
    
    /**
     * Get the current ExtentTest for the running thread
     * @return ExtentTest instance
     */
    public static ExtentTest getExtentTest() {
        return extentTestThreadLocal.get();
    }
    
    /**
     * Initialize ExtentReports
     * @return ExtentReports instance
     */
    private synchronized static ExtentReports initializeExtentReports() {
        if (extentReports == null) {
            try {
                // Create reports directory if it doesn't exist
                createDirectory(REPORTS_DIR);
                createDirectory(SCREENSHOTS_DIR);
                
                // Create a unique report filename with timestamp
                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                String reportName = "ExtentReport_" + timestamp + ".html";
                
                // Use the reports directory for reports
                File reportsDir = new File(System.getProperty("user.dir"), REPORTS_DIR);
                if (!reportsDir.exists()) {
                    boolean created = reportsDir.mkdirs();
                    LoggerUtils.info("Reports directory created: " + created + " - " + reportsDir.getAbsolutePath());
                }
                
                String reportPath = reportsDir.getAbsolutePath() + File.separator + reportName;
                
                LoggerUtils.info("Creating report at: " + reportPath);
                
                // Initialize ExtentReports with the Spark reporter
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
                
                // Try to load custom configuration if exists
                try {
                    Path configPath = Paths.get(System.getProperty("user.dir"), "src/test/resources/extent-config.xml");
                    if (Files.exists(configPath)) {
                        try (InputStream is = Files.newInputStream(configPath)) {
                            sparkReporter.loadXMLConfig(configPath.toFile());
                            LoggerUtils.info("Loaded custom Extent Report configuration from: " + configPath);
                        }
                    } else {
                        // Apply default configuration
                        sparkReporter.config().setTheme(Theme.STANDARD);
                        sparkReporter.config().setDocumentTitle("eAmata Automation Report");
                        sparkReporter.config().setReportName("Test Execution Report");
                        sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
                        sparkReporter.config().setOfflineMode(true);
                    }
                } catch (Exception e) {
                    LoggerUtils.error("Failed to load Extent Report configuration: " + e.getMessage(), e);
                    
                    // Apply default configuration
                    sparkReporter.config().setTheme(Theme.STANDARD);
                    sparkReporter.config().setDocumentTitle("eAmata Automation Report");
                    sparkReporter.config().setReportName("Test Execution Report");
                }
                
                // Create ExtentReports instance
                extentReports = new ExtentReports();
                extentReports.attachReporter(sparkReporter);
                
                // Add system information
                extentReports.setSystemInfo("OS", System.getProperty("os.name"));
                extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
                extentReports.setSystemInfo("Browser", ConfigManager.getInstance().getBrowser());
                extentReports.setSystemInfo("Environment", ConfigManager.getInstance().getProperty("environment", "QA"));
                extentReports.setSystemInfo("User", System.getProperty("user.name"));
                
                LoggerUtils.info("Initialized ExtentReports: " + reportPath);
            } catch (Exception e) {
                LoggerUtils.error("Failed to initialize ExtentReports: " + e.getMessage(), e);
            }
        }
        
        return extentReports;
    }
    
    /**
     * Create directory if it doesn't exist
     * @param dirPath Directory path
     */
    private static void createDirectory(String dirPath) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                LoggerUtils.info("Created directory: " + path.toAbsolutePath());
            } else {
                LoggerUtils.info("Directory already exists: " + path.toAbsolutePath());
            }
        } catch (Exception e) {
            LoggerUtils.error("Failed to create directory " + dirPath + ": " + e.getMessage(), e);
        }
    }
    
    // ISuiteListener methods
    
    @Override
    public void onStart(ISuite suite) {
        LoggerUtils.info("Starting test suite: " + suite.getName());
        
        // Initialize ExtentReports
        initializeExtentReports();
        
        // Add suite information to Allure
        Allure.addAttachment("Suite Info", "text/plain", 
                "Suite: " + suite.getName() + "\n" +
                "Start Time: " + new Date());
    }
    
    @Override
    public void onFinish(ISuite suite) {
        LoggerUtils.info("Finished test suite: " + suite.getName());
        
        // Flush ExtentReports
        if (extentReports != null) {
            extentReports.flush();
            LoggerUtils.info("ExtentReports flushed successfully");
        }
        
        // Add suite summary to Allure
        Allure.addAttachment("Suite Summary", "text/plain", 
                "Suite: " + suite.getName() + "\n" +
                "End Time: " + new Date() + "\n" +
                "Results: " + suite.getResults());
    }
    
    // ITestListener methods
    
    @Override
    public void onStart(ITestContext context) {
        String testClassName = context.getName();
        LoggerUtils.info("Starting test class: " + testClassName);
        
        // Record start time
        testClassStartTime.put(testClassName, System.currentTimeMillis());
        
        // Add test class information to Allure
        Allure.addAttachment("Test Class Info", "text/plain", 
                "Test Class: " + testClassName + "\n" +
                "Start Time: " + new Date());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        String testClassName = context.getName();
        LoggerUtils.info("Finished test class: " + testClassName);
        
        // Calculate and log execution time
        Long startTime = testClassStartTime.get(testClassName);
        if (startTime != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            LoggerUtils.info("Test class " + testClassName + " execution time: " + executionTime + " ms");
            
            // Add execution time to Allure
            Allure.addAttachment("Test Class Summary", "text/plain", 
                    "Test Class: " + testClassName + "\n" +
                    "End Time: " + new Date() + "\n" +
                    "Execution Time: " + executionTime + " ms\n" +
                    "Passed: " + context.getPassedTests().size() + "\n" +
                    "Failed: " + context.getFailedTests().size() + "\n" +
                    "Skipped: " + context.getSkippedTests().size());
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        String testName = className + "." + methodName;
        
        LoggerUtils.info("Starting test method: " + testName);
        
        // Record start time
        testMethodStartTime.put(testName, System.currentTimeMillis());
        
        // Create ExtentTest and store in ThreadLocal
        ExtentTest test = initializeExtentReports().createTest(testName);
        test.assignCategory(result.getMethod().getGroups());
        extentTestThreadLocal.set(test);
        
        // Add test method parameters if any
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            test.log(Status.INFO, "Test Parameters: " + Arrays.toString(params));
            Allure.parameter("Test Parameters", Arrays.toString(params));
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        String testName = className + "." + methodName;
        
        LoggerUtils.info("Test passed: " + testName);
        
        // Log execution time
        logExecutionTime(testName);
        
        // Log to ExtentReports
        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully");
            
            // Capture screenshot on success too
            try {
                WebDriver driver = BaseClass.getDriver();
                if (driver != null) {
                    String screenshotPath = ReportUtils.captureScreenshot(driver, methodName + "_success");
                    test.addScreenCaptureFromPath(screenshotPath);
                }
            } catch (Exception e) {
                LoggerUtils.error("Failed to capture success screenshot: " + e.getMessage(), e);
            }
        }
        
        // Log to Allure
        Allure.step("Test passed successfully");
        
        // Force flush reports after each test
        if (extentReports != null) {
            extentReports.flush();
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        String testName = className + "." + methodName;
        
        LoggerUtils.error("Test failed: " + testName);
        LoggerUtils.error("Failure cause: " + result.getThrowable());
        
        // Log execution time
        logExecutionTime(testName);
        
        // Get ExtentTest for the current thread
        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            // Log failure details using the utility method
            ReportUtils.logFailureDetails(result, test);
        }
        
        // Try to capture additional debug information
        try {
            WebDriver driver = BaseClass.getDriver();
            if (driver != null) {
                // Capture current URL
                String currentUrl = driver.getCurrentUrl();
                LoggerUtils.info("Current URL at failure: " + currentUrl);
                
                // Log to Allure
                Allure.addAttachment("URL at Failure", "text/plain", currentUrl);
                
                // Log to Extent
                if (test != null) {
                    test.log(Status.INFO, "URL at Failure: " + currentUrl);
                }
            }
        } catch (Exception e) {
            LoggerUtils.error("Failed to capture additional debug information: " + e.getMessage(), e);
        }
        
        // Force flush reports after each test failure
        if (extentReports != null) {
            extentReports.flush();
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        String testName = className + "." + methodName;
        
        LoggerUtils.warn("Test skipped: " + testName);
        
        // Get skip reason
        String skipReason = "Unknown reason";
        if (result.getThrowable() != null) {
            skipReason = result.getThrowable().getMessage();
            LoggerUtils.warn("Skip reason: " + skipReason);
        }
        
        // Log to ExtentReports
        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + skipReason);
            if (result.getThrowable() != null) {
                test.log(Status.SKIP, result.getThrowable());
            }
        }
        
        // Log to Allure
        Allure.step("Test skipped: " + skipReason);
        if (result.getThrowable() != null) {
            ReportUtils.saveTextLog(result.getThrowable().toString());
        }
        
        // Force flush reports after each skipped test
        if (extentReports != null) {
            extentReports.flush();
        }
    }
    
    // IInvokedMethodListener methods
    
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String methodName = method.getTestMethod().getMethodName();
            LoggerUtils.info("Before test method invocation: " + methodName);
            
            // Log to Allure
            Allure.step("Starting test method: " + methodName);
        }
    }
    
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String methodName = method.getTestMethod().getMethodName();
            LoggerUtils.info("After test method invocation: " + methodName);
            
            // Log to Allure
            Allure.step("Completed test method: " + methodName);
        }
    }
    
    /**
     * Log test method execution time
     * @param testName Test name (className.methodName)
     */
    private void logExecutionTime(String testName) {
        Long startTime = testMethodStartTime.get(testName);
        if (startTime != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            LoggerUtils.info("Test method " + testName + " execution time: " + executionTime + " ms");
            
            // Log to ExtentReports
            ExtentTest test = extentTestThreadLocal.get();
            if (test != null) {
                test.log(Status.INFO, "Execution time: " + executionTime + " ms");
            }
            
            // Log to Allure
            Allure.addAttachment("Execution Info", "text/plain", 
                    "Test: " + testName + "\n" +
                    "Execution Time: " + executionTime + " ms");
        }
    }
} 