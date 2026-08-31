package com.magicbricks.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.magicbricks.utils.DriverManager;
import com.magicbricks.utils.ExtentManager;
import com.magicbricks.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener implementing ITestListener.
 * Captures and embeds screenshots into ExtentReports for both passed and failed tests.
 * Decouples reporting and screenshot capture from test classes (SRP & OCP).
 *
 * Attached via testng.xml <listeners> tag.
 */
public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.createExtentReports();
    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        System.out.println("=================================================");
        System.out.println("Starting Suite Execution: " + context.getName());
        System.out.println("=================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = testName;
        }

        // Include DataProvider arguments in test name for clear report granularity
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            testName += " [" + params[0].toString() + "]";
        }

        ExtentTest test = extent.createTest(testName, description);
        test.assignCategory(result.getMethod().getGroups());
        testNode.set(test);
        testNode.get().log(Status.INFO, "Started execution of: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testNode.get().log(Status.PASS, "Test PASSED: " + result.getMethod().getMethodName());

        // Capture and embed screenshot on PASS as visual proof
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                String base64Screenshot = ScreenshotUtil.getBase64Screenshot(driver);
                ScreenshotUtil.takeScreenshot(driver, result.getMethod().getMethodName() + "_PASS");
                if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                    testNode.get().pass(
                            "Execution Snapshot (PASS)",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot, "Success Snapshot").build()
                    );
                }
            }
        } catch (Exception e) {
            testNode.get().log(Status.INFO, "Could not capture success screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testNode.get().log(Status.FAIL, "Test FAILED: " + result.getMethod().getMethodName());
        testNode.get().log(Status.FAIL, result.getThrowable());

        // Capture and embed failure screenshot
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                String base64Screenshot = ScreenshotUtil.getBase64Screenshot(driver);
                ScreenshotUtil.takeScreenshot(driver, result.getMethod().getMethodName() + "_FAIL");
                if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                    testNode.get().fail(
                            "Failure Screenshot",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot, "Failure Snapshot").build()
                    );
                }
            }
        } catch (Exception e) {
            testNode.get().log(Status.WARNING, "Failed to attach screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testNode.get().log(Status.SKIP, "Test SKIPPED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            testNode.get().log(Status.SKIP, result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
        System.out.println("=================================================");
        System.out.println("Suite Execution Finished. ExtentReport generated at:");
        System.out.println(ExtentManager.getReportPath());
        System.out.println("=================================================");
    }
}
