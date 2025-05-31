package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import testBase.BaseClass;

import java.util.List;

public class AllureListener implements ITestListener {

    private static String getTestMethodName(ITestResult result) {
        return result.getMethod().getMethodName();
    }

    @Attachment(value = "Error Details", type = "text/plain")
    public static String saveErrorDetails(String message) {
        return message;
    }

    @Attachment(value = "Screenshot: {testName}", type = "image/png")
    public byte[] captureScreenshot(String testName, WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void onStart(ITestContext testContext) {
        String os = testContext.getCurrentXmlTest().getParameter("os");
        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();

        if (os != null) {
            Allure.addAttachment("Operating System", "text/plain", os);
        }
        if (!includedGroups.isEmpty()) {
            Allure.addAttachment("Test Groups", "text/plain", includedGroups.toString());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("Test Passed: " + getTestMethodName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = getTestMethodName(result);
        Allure.step("Test Failed: " + methodName);

        if (result.getThrowable() != null) {
            saveErrorDetails(result.getThrowable().getMessage());
        }

        WebDriver driver = BaseClass.getDriver();
        if (driver != null) {
            captureScreenshot(methodName, driver);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String methodName = getTestMethodName(result);
        Allure.step("Test Skipped: " + methodName);

        if (result.getThrowable() != null) {
            saveErrorDetails("Skipped Reason: " + result.getThrowable().getMessage());
        }
    }
}