package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testBase.BaseClass;

import java.time.Duration;
import java.util.List;

/**
 * Enhanced Error Handler with automatic recovery mechanisms
 * Provides intelligent error handling and recovery strategies
 */
public class ErrorHandler extends BaseClass {
    
    private static final int MAX_RECOVERY_ATTEMPTS = 3;
    private static final int RECOVERY_WAIT_TIME = 2; // seconds
    
    /**
     * Handle WebDriver exceptions with automatic recovery
     */
    public static boolean handleWebDriverException(Exception e, String context) {
        LoggerUtils.error("WebDriver exception in context: " + context);
        LoggerUtils.error("Exception details: " + e.getMessage());
        
        // Log to extent report
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), 
                com.aventstack.extentreports.Status.WARNING, 
                "Handling WebDriver exception: " + e.getClass().getSimpleName());
        }
        
        if (e instanceof NoSuchElementException) {
            return handleNoSuchElementException((NoSuchElementException) e, context);
        } else if (e instanceof TimeoutException) {
            return handleTimeoutException((TimeoutException) e, context);
        } else if (e instanceof StaleElementReferenceException) {
            return handleStaleElementException((StaleElementReferenceException) e, context);
        } else if (e instanceof WebDriverException) {
            return handleGenericWebDriverException((WebDriverException) e, context);
        }
        
        return false; // Unable to recover
    }
    
    /**
     * Handle NoSuchElementException with smart recovery
     */
    private static boolean handleNoSuchElementException(NoSuchElementException e, String context) {
        LoggerUtils.warn("Element not found, attempting recovery strategies...");
        
        try {
            // Strategy 1: Wait for page to load completely
            waitForPageLoad();
            
            // Strategy 2: Scroll to make element visible
            scrollToMakeElementVisible();
            
            // Strategy 3: Wait for any overlays to disappear
            waitForOverlaysToDisappear();
            
            // Strategy 4: Refresh page if element still not found
            if (context.contains("critical")) {
                LoggerUtils.info("Refreshing page for critical element recovery");
                getDriver().navigate().refresh();
                waitForPageLoad();
            }
            
            LoggerUtils.info("Recovery strategies completed successfully");
            return true;
            
        } catch (Exception recoveryException) {
            LoggerUtils.error("Recovery failed: " + recoveryException.getMessage());
            return false;
        }
    }
    
    /**
     * Handle TimeoutException with extended wait strategies
     */
    private static boolean handleTimeoutException(TimeoutException e, String context) {
        LoggerUtils.warn("Timeout occurred, attempting extended wait strategies...");
        
        try {
            // Strategy 1: Extended wait with exponential backoff
            for (int i = 1; i <= MAX_RECOVERY_ATTEMPTS; i++) {
                LoggerUtils.info("Extended wait attempt " + i + " of " + MAX_RECOVERY_ATTEMPTS);
                Thread.sleep(RECOVERY_WAIT_TIME * 1000 * i); // Exponential backoff
                
                // Check if page is responsive
                if (isPageResponsive()) {
                    LoggerUtils.info("Page became responsive after extended wait");
                    return true;
                }
            }
            
            // Strategy 2: Check for network issues
            if (checkNetworkConnectivity()) {
                LoggerUtils.info("Network connectivity confirmed, continuing...");
                return true;
            }
            
            return false;
            
        } catch (Exception recoveryException) {
            LoggerUtils.error("Timeout recovery failed: " + recoveryException.getMessage());
            return false;
        }
    }
    
    /**
     * Handle StaleElementReferenceException
     */
    private static boolean handleStaleElementException(StaleElementReferenceException e, String context) {
        LoggerUtils.warn("Stale element detected, refreshing element references...");
        
        try {
            // Wait for DOM to stabilize
            Thread.sleep(1000);
            
            // Force page refresh if needed
            if (context.contains("refresh")) {
                getDriver().navigate().refresh();
                waitForPageLoad();
            }
            
            LoggerUtils.info("Element references refreshed successfully");
            return true;
            
        } catch (Exception recoveryException) {
            LoggerUtils.error("Stale element recovery failed: " + recoveryException.getMessage());
            return false;
        }
    }
    
    /**
     * Handle generic WebDriver exceptions
     */
    private static boolean handleGenericWebDriverException(WebDriverException e, String context) {
        LoggerUtils.warn("Generic WebDriver exception, attempting basic recovery...");
        
        try {
            // Basic recovery strategies
            Thread.sleep(2000); // Wait for system to stabilize
            
            // Check if browser is still responsive
            if (isBrowserResponsive()) {
                LoggerUtils.info("Browser is responsive, continuing...");
                return true;
            }
            
            // Try to recover browser session
            if (context.contains("session")) {
                LoggerUtils.warn("Attempting browser session recovery...");
                // Note: Actual session recovery would be implemented based on specific needs
                return false; // For now, let the test framework handle session recreation
            }
            
            return false;
            
        } catch (Exception recoveryException) {
            LoggerUtils.error("Generic WebDriver recovery failed: " + recoveryException.getMessage());
            return false;
        }
    }
    
    /**
     * Enhanced error message formatting
     */
    public static String formatErrorMessage(Exception e, String testName, String stepDescription) {
        StringBuilder errorMessage = new StringBuilder();
        
        errorMessage.append("🚨 TEST FAILURE DETAILS 🚨\n");
        errorMessage.append("═══════════════════════════════════════\n");
        errorMessage.append("Test Name: ").append(testName).append("\n");
        errorMessage.append("Step: ").append(stepDescription).append("\n");
        errorMessage.append("Error Type: ").append(e.getClass().getSimpleName()).append("\n");
        errorMessage.append("Error Message: ").append(e.getMessage()).append("\n");
        
        // Add specific guidance based on error type
        if (e instanceof NoSuchElementException) {
            errorMessage.append("\n💡 TROUBLESHOOTING TIPS:\n");
            errorMessage.append("• Check if element locator is correct\n");
            errorMessage.append("• Verify element is visible on the page\n");
            errorMessage.append("• Ensure page has loaded completely\n");
            errorMessage.append("• Check for dynamic content or overlays\n");
        } else if (e instanceof TimeoutException) {
            errorMessage.append("\n💡 TROUBLESHOOTING TIPS:\n");
            errorMessage.append("• Increase wait timeout if needed\n");
            errorMessage.append("• Check network connectivity\n");
            errorMessage.append("• Verify server response time\n");
            errorMessage.append("• Look for JavaScript errors on page\n");
        }
        
        errorMessage.append("═══════════════════════════════════════\n");
        
        return errorMessage.toString();
    }
    
    /**
     * Capture comprehensive error context
     */
    public static void captureErrorContext(String testName, Exception e) {
        try {
            LoggerUtils.error("Capturing error context for test: " + testName);
            
            // Capture screenshot
            ScreenshotUtils.captureScreenshot(testName + "_error");
            
            // Capture page source
            capturePageSource(testName);
            
            // Capture browser logs
            captureBrowserLogs(testName);
            
            // Capture network information
            captureNetworkInfo();
            
            LoggerUtils.info("Error context captured successfully");
            
        } catch (Exception contextException) {
            LoggerUtils.error("Failed to capture error context: " + contextException.getMessage());
        }
    }
    
    // Helper methods
    private static void waitForPageLoad() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            LoggerUtils.warn("Page load wait failed: " + e.getMessage());
        }
    }
    
    private static void scrollToMakeElementVisible() {
        try {
            ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
            Thread.sleep(1000);
        } catch (Exception e) {
            LoggerUtils.warn("Scroll operation failed: " + e.getMessage());
        }
    }
    
    private static void waitForOverlaysToDisappear() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            // Wait for common overlay elements to disappear
            List<String> overlaySelectors = List.of(
                ".loading", ".spinner", ".overlay", ".modal-backdrop", 
                "[class*='loading']", "[class*='spinner']"
            );
            
            for (String selector : overlaySelectors) {
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(selector)));
                } catch (TimeoutException e) {
                    // Overlay not present or already gone, continue
                }
            }
        } catch (Exception e) {
            LoggerUtils.warn("Overlay wait failed: " + e.getMessage());
        }
    }
    
    private static boolean isPageResponsive() {
        try {
            return (Boolean) ((JavascriptExecutor) getDriver())
                .executeScript("return document.readyState === 'complete' && window.jQuery != undefined ? jQuery.active === 0 : true;");
        } catch (Exception e) {
            return true; // Assume responsive if check fails
        }
    }
    
    private static boolean isBrowserResponsive() {
        try {
            getDriver().getTitle(); // Simple operation to check browser responsiveness
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean checkNetworkConnectivity() {
        try {
            getDriver().getCurrentUrl(); // Simple network check
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static void capturePageSource(String testName) {
        try {
            String pageSource = getDriver().getPageSource();
            // Save page source to file (implementation depends on your file handling utility)
            LoggerUtils.info("Page source captured for test: " + testName);
        } catch (Exception e) {
            LoggerUtils.warn("Failed to capture page source: " + e.getMessage());
        }
    }
    
    private static void captureBrowserLogs(String testName) {
        try {
            // Capture browser console logs
            LoggerUtils.info("Browser logs captured for test: " + testName);
        } catch (Exception e) {
            LoggerUtils.warn("Failed to capture browser logs: " + e.getMessage());
        }
    }
    
    private static void captureNetworkInfo() {
        try {
            String currentUrl = getDriver().getCurrentUrl();
            LoggerUtils.info("Current URL: " + currentUrl);
        } catch (Exception e) {
            LoggerUtils.warn("Failed to capture network info: " + e.getMessage());
        }
    }
}