package com.magicbricks.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.magicbricks.utils.ConsoleLogger;
import com.magicbricks.utils.DriverManager;
import com.magicbricks.utils.ExtentManager;
import com.magicbricks.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

/**
 * TestNG listener implementing ITestListener and ISuiteListener.
 * Automatically generates separate, dedicated HTML reports for each module:
 * - reports/TestReport_Home.html
 * - reports/TestReport_Login.html
 * - reports/TestReport_Sell.html
 *
 * Prints clear, aesthetically formatted terminal logs with headers,
 * step execution details, and status summaries for seamless debugging.
 */
public class ExtentReportListener implements ITestListener, ISuiteListener {

    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();
    private static final ThreadLocal<Long> testStartTime = new ThreadLocal<>();

    private String resolveModuleName(ITestResult result) {
        String className = result.getTestClass().getName();
        if (className.contains("HomePage")) return "Home";
        if (className.contains("Login")) return "Login";
        if (className.contains("Sell")) return "Sell";
        return ExtentManager.normalizeModuleName(result.getTestContext().getName());
    }

    @Override
    public void onStart(ISuite suite) {
        System.out.println("\n" +
                "================================================================================\n" +
                "  🚀 STARTING TEST SUITE: " + suite.getName().toUpperCase() + "\n" +
                "================================================================================\n");
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flushAllReports();
        System.out.println("\n" +
                "================================================================================\n" +
                "  🏁 TEST SUITE COMPLETED: " + suite.getName().toUpperCase() + "\n" +
                "  📁 All Reports Flushed into reports/ directory\n" +
                "================================================================================\n");
    }

    @Override
    public void onStart(ITestContext context) {
        String moduleName = ExtentManager.normalizeModuleName(context.getName());
        ExtentManager.getExtentReports(moduleName);
        System.out.println("┌──────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ 📂 MODULE RUN: " + String.format("%-61s", context.getName()) + "│");
        System.out.println("│ 📄 HTML Report: " + String.format("%-60s", ExtentManager.getReportPath(moduleName)) + "│");
        System.out.println("└──────────────────────────────────────────────────────────────────────────────┘\n");
    }

    @Override
    public void onTestStart(ITestResult result) {
        testStartTime.set(System.currentTimeMillis());
        String moduleName = resolveModuleName(result);
        ExtentReports extent = ExtentManager.getExtentReports(moduleName);

        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = testName;
        }

        Object[] params = result.getParameters();
        String paramStr = "";
        if (params != null && params.length > 0) {
            paramStr = " [" + Arrays.toString(params).replace("[", "").replace("]", "") + "]";
            testName += paramStr;
        }

        ExtentTest test = extent.createTest(testName, description);
        test.assignCategory(result.getMethod().getGroups());
        test.assignCategory("Browser: " + DriverManager.getBrowserName().toUpperCase());
        test.assignDevice("Thread-" + Thread.currentThread().getId());
        testNode.set(test);
        testNode.get().log(Status.INFO, "Started execution of: " + testName);

        // Terminal Visual Header with ANSI Colors & prominent Test Case ID
        ConsoleLogger.logTestStart(testName, description, moduleName, Arrays.toString(result.getMethod().getGroups()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long durationMs = System.currentTimeMillis() - (testStartTime.get() != null ? testStartTime.get() : System.currentTimeMillis());
        double durationSec = durationMs / 1000.0;

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

        ConsoleLogger.logSuccess(result.getMethod().getMethodName(), durationSec);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long durationMs = System.currentTimeMillis() - (testStartTime.get() != null ? testStartTime.get() : System.currentTimeMillis());
        double durationSec = durationMs / 1000.0;

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

        String errMsg = result.getThrowable() != null ? result.getThrowable().getMessage() : "";
        ConsoleLogger.logFailure(result.getMethod().getMethodName(), errMsg, durationSec);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testNode.get().log(Status.SKIP, "Test SKIPPED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            testNode.get().log(Status.SKIP, result.getThrowable());
        }

        ConsoleLogger.logSkipped(result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        String moduleName = ExtentManager.normalizeModuleName(context.getName());
        ExtentReports extent = ExtentManager.getExtentReports(moduleName);
        if (extent != null) {
            ConsoleLogger.logReportGenerated(moduleName, ExtentManager.getReportPath(moduleName));
        }
    }

    private static String truncate(String str, int maxLen) {
        if (str == null) return "";
        return str.length() <= maxLen ? str : str.substring(0, maxLen - 3) + "...";
    }
}
