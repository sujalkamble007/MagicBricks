package com.magicbricks.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Single-responsibility utility for capturing browser screenshots.
 * Supports:
 * - Base64 capture for direct inline embedding in ExtentReports
 * - File-based screenshot capture saved to screenshots/ directory
 */
public class ScreenshotUtil {

    /**
     * Captures screenshot as a Base64 string for direct embedding into ExtentReports.
     */
    public static String getBase64Screenshot(WebDriver driver) {
        if (driver == null) {
            return "";
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Captures and saves screenshot to disk under screenshots/ directory.
     *
     * @param driver Current WebDriver
     * @param testName Name of the failing test
     * @return Path to the saved image file
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            return "";
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String destPath = "screenshots/" + testName + "_T" + Thread.currentThread().getId() + "_" + timestamp + ".png";
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(destPath);
        try {
            FileUtils.copyFile(srcFile, destFile);
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
            return "";
        }
    }
}
