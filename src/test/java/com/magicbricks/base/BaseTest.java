package com.magicbricks.base;

import com.magicbricks.utils.ConfigReader;
import com.magicbricks.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for all Test classes. Provides:
 * - WebDriver setup before each test method
 * - WebDriver teardown after each test method
 * - Navigation to base URL
 *
 * All test classes (HomePageTest, LoginTest, etc.) extend this.
 * Follows OCP — extendable for future test classes without modification.
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.initDriver();
        driver.get(ConfigReader.getBaseUrl());
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
