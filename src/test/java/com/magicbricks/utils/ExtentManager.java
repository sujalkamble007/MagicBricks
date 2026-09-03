package com.magicbricks.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe ExtentReports initialization manager.
 * Generates separate, dedicated HTML reports per module:
 * - reports/TestReport_Home.html
 * - reports/TestReport_Login.html
 * - reports/TestReport_Sell.html
 */
public class ExtentManager {

    private static final ConcurrentHashMap<String, ExtentReports> moduleReports = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> modulePaths = new ConcurrentHashMap<>();

    /**
     * Retrieves or creates the dedicated ExtentReports instance for the specified module.
     *
     * @param rawName Raw module name or context name
     * @return Initialized ExtentReports instance
     */
    public static synchronized ExtentReports getExtentReports(String rawName) {
        String moduleName = normalizeModuleName(rawName);
        if (!moduleReports.containsKey(moduleName)) {
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            String reportFileName = "TestReport_" + moduleName + ".html";
            String reportPath = "reports/" + reportFileName;
            modulePaths.put(moduleName, reportPath);

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("MagicBricks " + moduleName + " Automation Report");
            sparkReporter.config().setReportName(moduleName + " Module Test Execution");
            sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

            ExtentReports extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "MagicBricks Web");
            extent.setSystemInfo("Module", moduleName);
            extent.setSystemInfo("Environment", "QA / Production Live");
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", ConfigReader.getBrowser());
            extent.setSystemInfo("Author", "SDET Automation Team");

            moduleReports.put(moduleName, extent);
        }
        return moduleReports.get(moduleName);
    }

    /**
     * Normalizes arbitrary string input to standard module names: Home, Login, Sell.
     */
    public static String normalizeModuleName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "General";
        }
        String lower = name.toLowerCase();
        if (lower.contains("home")) return "Home";
        if (lower.contains("login")) return "Login";
        if (lower.contains("sell")) return "Sell";
        return name.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    /**
     * Gets the output file path for a module's HTML report.
     */
    public static String getReportPath(String rawName) {
        String moduleName = normalizeModuleName(rawName);
        return modulePaths.getOrDefault(moduleName, "reports/TestReport_" + moduleName + ".html");
    }

    /**
     * Flushes all active module ExtentReports instances to disk.
     */
    public static synchronized void flushAllReports() {
        for (ExtentReports extent : moduleReports.values()) {
            try {
                extent.flush();
            } catch (Exception ignored) {}
        }
    }
}
