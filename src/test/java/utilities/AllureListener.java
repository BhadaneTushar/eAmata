package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import testBase.BaseClass;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Listener for Allure reporting
 * Captures screenshots and logs for test failures
 * @deprecated Use {@link TestListener} instead which provides enhanced reporting capabilities
 */
@Deprecated
public class AllureListener implements ITestListener {

    /**
     * Get the test method name from the test result
     * @param result Test result
     * @return Test method name
     */
    private static String getTestMethodName(ITestResult result) {
        return result.getMethod().getMethodName();
    }

    /**
     * Save error details as an attachment
     * @param message Error message
     * @return Error message
     * @deprecated Use {@link ReportUtils#saveTextLog(String)} instead
     */
    @Deprecated
    @Attachment(value = "Error Details", type = "text/plain")
    public static String saveErrorDetails(String message) {
        return ReportUtils.saveTextLog(message);
    }

    /**
     * Capture screenshot as an attachment
     * @param testName Test name
     * @return Screenshot as byte array
     * @deprecated Use {@link ReportUtils#captureScreenshotAsBytes(WebDriver)} instead
     */
    @Deprecated
    @Attachment(value = "Screenshot: {0}", type = "image/png")
    public byte[] captureScreenshot(String testName) {
        return ReportUtils.captureScreenshotAsBytes(BaseClass.getDriver());
    }

    @Override
    public void onStart(ITestContext testContext) {
        LoggerUtils.info("Starting test suite: " + testContext.getName());
        
        String os = System.getProperty("os.name") + " " + System.getProperty("os.version");
        String browser = ConfigManager.getInstance().getBrowser();
        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();

        Allure.addAttachment("Operating System", "text/plain", os);
        Allure.addAttachment("Browser", "text/plain", browser);
        
        if (!includedGroups.isEmpty()) {
            Allure.addAttachment("Test Groups", "text/plain", includedGroups.toString());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtils.info("Starting test: " + getTestMethodName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String methodName = getTestMethodName(result);
        LoggerUtils.info("Test Passed: " + methodName);
        Allure.step("Test Passed: " + methodName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = getTestMethodName(result);
        LoggerUtils.error("Test Failed: " + methodName);
        Allure.step("Test Failed: " + methodName);

        if (result.getThrowable() != null) {
            LoggerUtils.error("Failure Reason: " + result.getThrowable().getMessage());
            saveErrorDetails(result.getThrowable().getMessage());
        }

        WebDriver driver = BaseClass.getDriver();
        if (driver != null) {
            ReportUtils.attachScreenshotToAllure(driver, "Screenshot on Failure: " + methodName);
            captureScreenshot(methodName);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String methodName = getTestMethodName(result);
        LoggerUtils.warn("Test Skipped: " + methodName);
        Allure.step("Test Skipped: " + methodName);

        if (result.getThrowable() != null) {
            LoggerUtils.warn("Skipped Reason: " + result.getThrowable().getMessage());
            saveErrorDetails("Skipped Reason: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
        LoggerUtils.info("Finished test suite: " + testContext.getName());
        LoggerUtils.info("Passed tests: " + testContext.getPassedTests().size());
        LoggerUtils.info("Failed tests: " + testContext.getFailedTests().size());
        LoggerUtils.info("Skipped tests: " + testContext.getSkippedTests().size());
    }
}