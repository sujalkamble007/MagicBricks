package com.magicbricks.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Manages WebDriver lifecycle — initialization, configuration, and teardown.
 * Always launches in visible (non-headless) mode so the tester can observe
 * every action in real time.
 */
public class DriverManager {

    private static WebDriver driver;

    /**
     * Initializes ChromeDriver with visible window, maximized, notifications disabled.
     * Does NOT use headless mode — the browser must be visible to the tester.
     */
    public static WebDriver initDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--disable-notifications");
//        options.addArguments("--disable-infobars");
//        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        driver = new ChromeDriver(options);
//        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getImplicitWait()));

        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
