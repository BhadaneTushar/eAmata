package utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry Analyzer for handling flaky tests
 * Automatically retries failed tests up to a specified number of times
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2; // Maximum number of retries
    
    // Test-specific retry counts for different types of failures
    private static final int UI_ELEMENT_RETRY_COUNT = 3;
    private static final int NETWORK_RETRY_COUNT = 2;
    private static final int DATE_PICKER_RETRY_COUNT = 3;
    
    @Override
    public boolean retry(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String failureReason = getFailureReason(result);
        
        LoggerUtils.info("Test '" + testName + "' failed. Failure reason: " + failureReason);
        
        int maxRetries = getMaxRetriesForFailureType(failureReason);
        
        if (retryCount < maxRetries) {
            retryCount++;
            LoggerUtils.warn("Retrying test '" + testName + "' - Attempt " + retryCount + " of " + maxRetries);
            
            // Add delay before retry to allow system to stabilize
            try {
                Thread.sleep(2000); // 2 second delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Log retry attempt to reports
            if (TestListener.getExtentTest() != null) {
                ReportUtils.logStep(TestListener.getExtentTest(), 
                    com.aventstack.extentreports.Status.WARNING, 
                    "Retrying test due to: " + failureReason + " (Attempt " + retryCount + ")");
            }
            
            return true; // Retry the test
        }
        
        LoggerUtils.error("Test '" + testName + "' failed after " + maxRetries + " retries. Giving up.");
        
        // Log final failure to reports
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), 
                com.aventstack.extentreports.Status.FAIL, 
                "Test failed after " + maxRetries + " retries. Final failure: " + failureReason);
        }
        
        return false; // Don't retry anymore
    }
    
    /**
     * Analyze the failure reason from the test result
     */
    private String getFailureReason(ITestResult result) {
        if (result.getThrowable() == null) {
            return "Unknown failure";
        }
        
        String errorMessage = result.getThrowable().getMessage();
        String errorClass = result.getThrowable().getClass().getSimpleName();
        
        if (errorMessage == null) {
            return errorClass;
        }
        
        // Categorize common failure types
        if (errorMessage.contains("no such element") || errorMessage.contains("Unable to locate element")) {
            return "UI_ELEMENT_NOT_FOUND";
        } else if (errorMessage.contains("timeout") || errorMessage.contains("TimeoutException")) {
            return "TIMEOUT";
        } else if (errorMessage.contains("date") || errorMessage.contains("calendar")) {
            return "DATE_PICKER_ISSUE";
        } else if (errorMessage.contains("connection") || errorMessage.contains("network")) {
            return "NETWORK_ISSUE";
        } else if (errorMessage.contains("StaleElementReferenceException")) {
            return "STALE_ELEMENT";
        } else if (errorMessage.contains("WebDriverException")) {
            return "WEBDRIVER_ISSUE";
        }
        
        return errorClass + ": " + errorMessage.substring(0, Math.min(100, errorMessage.length()));
    }
    
    /**
     * Get maximum retry count based on failure type
     */
    private int getMaxRetriesForFailureType(String failureReason) {
        switch (failureReason) {
            case "UI_ELEMENT_NOT_FOUND":
            case "STALE_ELEMENT":
            case "DATE_PICKER_ISSUE":
                return UI_ELEMENT_RETRY_COUNT;
            case "NETWORK_ISSUE":
            case "TIMEOUT":
                return NETWORK_RETRY_COUNT;
            case "WEBDRIVER_ISSUE":
                return 1; // WebDriver issues usually need manual intervention
            default:
                return MAX_RETRY_COUNT;
        }
    }
    
    /**
     * Reset retry count for new test
     */
    public void resetRetryCount() {
        this.retryCount = 0;
    }
    
    /**
     * Get current retry count
     */
    public int getCurrentRetryCount() {
        return this.retryCount;
    }
}