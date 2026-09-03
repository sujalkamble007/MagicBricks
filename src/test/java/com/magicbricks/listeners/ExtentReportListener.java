package com.magicbricks.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.magicbricks.utils.DriverManager;
import com.magicbricks.utils.ExtentManager;
import com.magicbricks.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener implementing ITestListener and ISuiteListener.
 * Automatically generates separate, dedicated HTML reports for each module:
 * - reports/TestReport_Home.html
 * - reports/TestReport_Login.html
 * - reports/TestReport_Sell.html
 *
 * Captures and embeds screenshots into the respective module report.
 */
public class ExtentReportListener implements ITestListener, ISuiteListener {

    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    private String resolveModuleName(ITestResult result) {
        String className = result.getTestClass().getName();
        if (className.contains("HomePage")) return "Home";
        if (className.contains("Login")) return "Login";
        if (className.contains("Sell")) return "Sell";
        return ExtentManager.normalizeModuleName(result.getTestContext().getName());
    }

    @Override
    public void onStart(ISuite suite) {
        System.out.println("=================================================");
        System.out.println("Starting Test Suite: " + suite.getName());
        System.out.println("=================================================");
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flushAllReports();
        System.out.println("=================================================");
        System.out.println("Suite Execution Finished. All Module Reports Flushed.");
        System.out.println("=================================================");
    }

    @Override
    public void onStart(ITestContext context) {
        String moduleName = ExtentManager.normalizeModuleName(context.getName());
        ExtentManager.getExtentReports(moduleName);
        System.out.println("Starting Module Context: " + context.getName() + " -> Report: " + ExtentManager.getReportPath(moduleName));
    }

    @Override
    public void onTestStart(ITestResult result) {
        String moduleName = resolveModuleName(result);
        ExtentReports extent = ExtentManager.getExtentReports(moduleName);

        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = testName;
        }

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
        String moduleName = ExtentManager.normalizeModuleName(context.getName());
        ExtentReports extent = ExtentManager.getExtentReports(moduleName);
        if (extent != null) {
            extent.flush();
            System.out.println("Module Report generated at: " + ExtentManager.getReportPath(moduleName));
        }
    }
}
