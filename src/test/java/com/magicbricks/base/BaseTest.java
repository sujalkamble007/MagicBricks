package com.magicbricks.base;

import com.magicbricks.listeners.ExtentReportListener;
import com.magicbricks.utils.ConfigReader;
import com.magicbricks.utils.DriverManager;
import com.magicbricks.utils.PopupHandler;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base class for all Test classes. Provides:
 * - Thread-safe multi-browser WebDriver setup via DriverManager
 * - Parameterized browser injection supporting:
 *     1. CLI Flag (-Dbrowser=...)
 *     2. TestNG XML Parameter (<parameter name="browser" value="..."/>)
 *     3. config.properties default (browser=chrome)
 * - 100% Eclipse IDE portability (graceful @Optional fallback for direct class/method execution)
 * - Automatic popup/cookie/overlay banner dismissal via PopupHandler
 * - Leak-free WebDriver teardown with ThreadLocal cleanup
 */
@Listeners(ExtentReportListener.class)
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("") String browserFromXml) {
        String cliBrowser = System.getProperty("browser");
        String targetBrowser;

        if (cliBrowser != null && !cliBrowser.trim().isEmpty()) {
            targetBrowser = cliBrowser.trim();
        } else if (browserFromXml != null && !browserFromXml.trim().isEmpty()) {
            targetBrowser = browserFromXml.trim();
        } else {
            targetBrowser = ConfigReader.getBrowser();
        }

        driver = DriverManager.initDriver(targetBrowser);
        driver.get(ConfigReader.getBaseUrl());

        // Handle any cookie banners or overlays before test execution begins
        PopupHandler.handleInitialPopups(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
