package com.magicbricks.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages WebDriver lifecycle — initialization, configuration, and teardown.
 * Always launches in visible (non-headless) mode so the tester can observe
 * every action in real time.
 *
 * Configured with suppressed verbose internal browser and driver logs
 * for clean, distraction-free terminal observability.
 */
public class DriverManager {

    private static WebDriver driver;

    static {
        // Silence Selenium, CDP, Chromium, and Log4j2 console noise
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("log4j2.StatusLogger.level", "OFF");
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        Logger.getLogger("org.openqa.selenium.devtools").setLevel(Level.OFF);
        Logger.getLogger("org.openqa.selenium.chromium").setLevel(Level.OFF);
        Logger.getLogger("io.github.bonigarcia.wdm").setLevel(Level.OFF);
    }

    /**
     * Initializes ChromeDriver with visible window, maximized, notifications disabled.
     */
    public static WebDriver initDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--log-level=3");          // Only fatal errors
        options.addArguments("--silent");
        options.addArguments("--disable-logging");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getImplicitWait()));

        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {}
            driver = null;
        }
    }
}
