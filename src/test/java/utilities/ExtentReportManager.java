package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import testBase.BaseClass;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExtentReportManager implements ITestListener {

    // ThreadLocal for thread-safe test logging in parallel execution
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private ExtentSparkReporter sparkReporter;
    private ExtentReports extent;
    private String repName;

    @Override
    public void onStart(ITestContext testContext) {
        // Timestamp for unique report name
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
        repName = "Test-Report-" + timestamp + ".html";

        // Create reports directory if it doesn't exist
        File reportsDir = new File(".\\reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);
        sparkReporter.config().setDocumentTitle("OMR Automation Report");
        sparkReporter.config().setReportName("OMR Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Add system info (optional)
        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test node in ExtentReports for each test method and store in ThreadLocal
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        test.assignCategory(result.getMethod().getGroups());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, result.getName() + " executed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().log(Status.FAIL, result.getName() + " failed");
        extentTest.get().log(Status.INFO, result.getThrowable());

        try {
            String imgPath = new BaseClass().captureScreen(result.getName());
            extentTest.get().addScreenCaptureFromPath(imgPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, result.getName() + " skipped");
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.INFO, result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
        extent.flush();

        // Automatically open the generated report in the default browser
        String pathOfExtentReport = System.getProperty("user.dir") + "\\reports\\" + repName;
        File extentReport = new File(pathOfExtentReport);

        try {
            if (extentReport.exists()) {
                Desktop.getDesktop().browse(extentReport.toURI());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
