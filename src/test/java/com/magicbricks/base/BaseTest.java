package com.magicbricks.base;

import com.magicbricks.utils.ConfigReader;
import com.magicbricks.utils.DriverManager;
import com.magicbricks.utils.PopupHandler;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for all Test classes. Provides:
 * - WebDriver setup before each test method
 * - Automatic popup/cookie/overlay banner dismissal via PopupHandler
 * - WebDriver teardown after each test method
 *
 * All test classes (HomePageTest, LoginTest, etc.) extend this.
 * Follows Open/Closed Principle (OCP).
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverManager.initDriver();
        driver.get(ConfigReader.getBaseUrl());
        // Handle any cookie banners or overlays before test execution begins
        PopupHandler.handleInitialPopups(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
