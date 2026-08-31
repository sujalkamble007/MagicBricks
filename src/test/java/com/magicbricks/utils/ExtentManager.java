package com.magicbricks.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread-safe ExtentReports initialization manager.
 * Creates structured HTML reports conforming to the naming convention:
 * reports/TestReport_HomeLogin_<yyyy-MM-dd_HH-mm-ss>.html
 */
public class ExtentManager {

    private static ExtentReports extent;
    private static String reportPath;

    public static synchronized ExtentReports createExtentReports() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportFileName = "TestReport_HomeLogin_" + timestamp + ".html";
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }
            reportPath = "reports/" + reportFileName;

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("MagicBricks Automation Report");
            sparkReporter.config().setReportName("Home & Login Modules Execution");
            sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "MagicBricks Web");
            extent.setSystemInfo("Environment", "QA / Production Live");
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", ConfigReader.getBrowser());
            extent.setSystemInfo("Author", "SDET Automation Team");
        }
        return extent;
    }

    public static String getReportPath() {
        return reportPath;
    }
}
