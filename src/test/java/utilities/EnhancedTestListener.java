package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.*;

/**
 * Enhanced TestNG Listener with retry mechanism, error handling, and cleanup
 * This is an enhanced version that includes all the new features
 */
public class EnhancedTestListener implements ITestListener, ISuiteListener {
    
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final ThreadLocal<RetryAnalyzer> retryAnalyzer = new ThreadLocal<>();
    
    static {
        // Initialize extent reports using existing method
        try {
            extent = new com.aventstack.extentreports.ExtentReports();
            com.aventstack.extentreports.reporter.ExtentSparkReporter sparkReporter = 
                new com.aventstack.extentreports.reporter.ExtentSparkReporter("reports/EnhancedReport.html");
            extent.attachReporter(sparkReporter);
        } catch (Exception e) {
            LoggerUtils.error("Failed to initialize ExtentReports: " + e.getMessage());
        }
    }
    
    @Override
    public void onStart(ISuite suite) {
        LoggerUtils.info("🎬 Test suite started: " + suite.getName());
        
        // Initialize suite-level performance monitoring
        PerformanceMonitor.startTest("Suite_" + suite.getName());
        
        // Log suite start information
        if (extent != null) {
            ExtentTest suiteTest = extent.createTest("📋 Suite: " + suite.getName());
            suiteTest.log(Status.INFO, "Test suite started with enhanced features:");
            suiteTest.log(Status.INFO, "✅ Automatic retry mechanism enabled");
            suiteTest.log(Status.INFO, "✅ Enhanced error handling enabled");
            suiteTest.log(Status.INFO, "✅ Automatic test data cleanup enabled");
            suiteTest.log(Status.INFO, "✅ Performance monitoring enabled");
        }
    }
    
    @Override
    public void onFinish(ISuite suite) {
        LoggerUtils.info("🏁 Test suite finished: " + suite.getName());
        
        // Perform final cleanup of any remaining test data
        TestDataCleanup.cleanupAllTestData();
        
        // Generate cleanup summary
        String cleanupSummary = TestDataCleanup.getCleanupSummary();
        LoggerUtils.info(cleanupSummary);
        
        // End suite performance monitoring
        PerformanceMonitor.endTest("Suite_" + suite.getName());
        
        // Flush extent reports
        if (extent != null) {
            // Add final suite summary
            ExtentTest summaryTest = extent.createTest("📊 Final Suite Summary");
            summaryTest.log(Status.INFO, "Suite execution completed");
            summaryTest.log(Status.INFO, cleanupSummary.replace("\n", "<br>"));
            
            extent.flush();
        }
        
        LoggerUtils.info("📋 Enhanced test execution completed - All reports generated");
    }
    
    @Override
    public void onStart(ITestContext context) {
        LoggerUtils.info("🚀 Test context started: " + context.getName());
        LoggerUtils.info("📊 Total tests to run: " + context.getAllTestMethods().length);
    }
    
    @Override
    public void onFinish(ITestContext context) {
        LoggerUtils.info("✅ Test context finished: " + context.getName());
        
        // Generate comprehensive test summary
        generateTestSummary(context);
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        String fullTestName = className + "." + testName;
        
        // Initialize retry analyzer for this test
        RetryAnalyzer analyzer = new RetryAnalyzer();
        retryAnalyzer.set(analyzer);
        
        ExtentTest test = extent.createTest(fullTestName);
        test.assignCategory(result.getMethod().getGroups());
        extentTest.set(test);
        
        LoggerUtils.info("🚀 Starting enhanced test: " + testName);
        test.log(Status.INFO, "🚀 Test started with enhanced features");
        test.log(Status.INFO, "🔄 Retry mechanism: ENABLED");
        test.log(Status.INFO, "🛡️ Error handling: ENHANCED");
        test.log(Status.INFO, "🧹 Auto cleanup: ENABLED");
        
        // Log test start with performance monitoring
        PerformanceMonitor.startTest(fullTestName);
        
        // Initialize test data cleanup registry
        LoggerUtils.info("Initializing test data cleanup for: " + testName);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        String fullTestName = className + "." + testName;
        
        LoggerUtils.info("✅ Test passed: " + testName);
        
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS, "✅ Test passed successfully");
            
            // Add performance metrics to report
            long executionTime = PerformanceMonitor.endTest(fullTestName);
            test.log(Status.INFO, "📊 Performance Metrics: Execution time: " + executionTime + "ms");
            
            // Add retry information if test was retried
            RetryAnalyzer analyzer = retryAnalyzer.get();
            if (analyzer != null && analyzer.getCurrentRetryCount() > 0) {
                test.log(Status.INFO, "🔄 Test passed after " + analyzer.getCurrentRetryCount() + " retries");
            }
        }
        
        // Perform test data cleanup
        TestDataCleanup.cleanupTestData(testName, result);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        String fullTestName = className + "." + testName;
        
        LoggerUtils.error("❌ Test failed: " + testName);
        
        // Enhanced error handling
        Exception testException = (Exception) result.getThrowable();
        String enhancedErrorMessage = ErrorHandler.formatErrorMessage(testException, testName, "Test execution");
        LoggerUtils.error(enhancedErrorMessage);
        
        // Capture comprehensive error context
        ErrorHandler.captureErrorContext(testName, testException);
        
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.FAIL, "❌ Test failed");
            test.log(Status.FAIL, "🚨 Enhanced Error Details:");
            test.log(Status.FAIL, enhancedErrorMessage.replace("\n", "<br>"));
            
            // Capture screenshot on failure
            try {
                String screenshotPath = ScreenshotUtils.captureScreenshot(testName + "_failure");
                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath);
                    test.log(Status.INFO, "📸 Screenshot captured: " + screenshotPath);
                }
            } catch (Exception e) {
                test.log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
            }
            
            // Add retry information if applicable
            RetryAnalyzer analyzer = retryAnalyzer.get();
            if (analyzer != null && analyzer.getCurrentRetryCount() > 0) {
                test.log(Status.WARNING, "🔄 Test was retried " + analyzer.getCurrentRetryCount() + " times before final failure");
            }
        }
        
        // Attempt error recovery if possible
        boolean recovered = ErrorHandler.handleWebDriverException(testException, "test_failure");
        if (recovered) {
            LoggerUtils.info("🔧 Error recovery attempted for test: " + testName);
            if (test != null) {
                test.log(Status.WARNING, "🔧 Error recovery was attempted");
            }
        }
        
        // Perform test data cleanup even on failure
        TestDataCleanup.cleanupTestData(testName, result);
        
        // End performance monitoring
        PerformanceMonitor.endTest(fullTestName);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LoggerUtils.warn("⏭️ Test skipped: " + testName);
        
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.SKIP, "⏭️ Test skipped");
            
            // Log skip reason if available
            if (result.getThrowable() != null) {
                test.log(Status.SKIP, "Skip reason: " + result.getThrowable().getMessage());
            }
        }
        
        // Clean up any test data that might have been created before skip
        TestDataCleanup.cleanupTestData(testName, result);
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LoggerUtils.warn("⚠️ Test failed but within success percentage: " + testName);
        
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.WARNING, "⚠️ Test failed but within success percentage");
        }
    }
    
    /**
     * Generate comprehensive test summary
     */
    private void generateTestSummary(ITestContext context) {
        int totalTests = context.getAllTestMethods().length;
        int passedTests = context.getPassedTests().size();
        int failedTests = context.getFailedTests().size();
        int skippedTests = context.getSkippedTests().size();
        
        StringBuilder summary = new StringBuilder();
        summary.append("\n🎯 ENHANCED TEST EXECUTION SUMMARY\n");
        summary.append("═══════════════════════════════════════\n");
        summary.append("Context: ").append(context.getName()).append("\n");
        summary.append("Total Tests: ").append(totalTests).append("\n");
        summary.append("✅ Passed: ").append(passedTests).append("\n");
        summary.append("❌ Failed: ").append(failedTests).append("\n");
        summary.append("⏭️ Skipped: ").append(skippedTests).append("\n");
        summary.append("📊 Success Rate: ").append(String.format("%.1f%%", (passedTests * 100.0) / totalTests)).append("\n");
        summary.append("⏱️ Duration: ").append(formatDuration(context.getEndDate().getTime() - context.getStartDate().getTime())).append("\n");
        summary.append("\n🚀 Enhanced Features Used:\n");
        summary.append("• Automatic retry mechanism\n");
        summary.append("• Enhanced error handling\n");
        summary.append("• Automatic test data cleanup\n");
        summary.append("• Performance monitoring\n");
        summary.append("═══════════════════════════════════════\n");
        
        LoggerUtils.info(summary.toString());
        
        // Add summary to extent report
        if (extent != null) {
            ExtentTest summaryTest = extent.createTest("📊 Enhanced Test Context Summary");
            summaryTest.log(Status.INFO, summary.toString().replace("\n", "<br>"));
        }
    }
    
    /**
     * Format duration in human-readable format
     */
    private String formatDuration(long durationMs) {
        long seconds = durationMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return String.format("%d min %d sec", minutes, seconds);
        } else {
            return String.format("%d sec", seconds);
        }
    }
    
    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }
    
    public static RetryAnalyzer getRetryAnalyzer() {
        return retryAnalyzer.get();
    }
}