package com.magicbricks.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enterprise Thread-Safe Driver Manager for Parallel & Cross-Browser Execution.
 *
 * Employs ThreadLocal<WebDriver> to ensure strict isolation between concurrent
 * execution threads, preventing session clashing, memory leaks, or race conditions.
 *
 * Supports:
 * - Chrome (Google Chrome)
 * - Firefox (Mozilla Firefox)
 * - Edge (Microsoft Edge)
 *
 * Backward-compatible with Eclipse IDE, Maven CLI, and TestNG XML suites.
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> browserNameThreadLocal = new ThreadLocal<>();

    static {
        // Suppress Selenium, DevTools, Chromium, and WebDriverManager console noise
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("webdriver.edge.silentOutput", "true");
        System.setProperty("log4j2.StatusLogger.level", "OFF");
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        Logger.getLogger("org.openqa.selenium.devtools").setLevel(Level.OFF);
        Logger.getLogger("org.openqa.selenium.chromium").setLevel(Level.OFF);
        Logger.getLogger("org.openqa.selenium.firefox").setLevel(Level.OFF);
        Logger.getLogger("org.openqa.selenium.edge").setLevel(Level.OFF);
        Logger.getLogger("io.github.bonigarcia.wdm").setLevel(Level.OFF);
    }

    /**
     * Initializes a WebDriver instance using default browser from ConfigReader.
     */
    public static WebDriver initDriver() {
        return initDriver(ConfigReader.getBrowser());
    }

    /**
     * Initializes a WebDriver instance for the specified target browser.
     * Guarantees ThreadLocal isolation for parallel test runs.
     *
     * @param targetBrowser "chrome", "firefox", or "edge"
     * @return WebDriver instance bound to the current thread
     */
    public static WebDriver initDriver(String targetBrowser) {
        String browser = (targetBrowser != null && !targetBrowser.trim().isEmpty())
                ? targetBrowser.trim().toLowerCase()
                : ConfigReader.getBrowser().toLowerCase();

        boolean headless = Boolean.parseBoolean(System.getProperty("headless",
                String.valueOf(ConfigReader.isHeadless())));

        WebDriver driver;

        try {
            switch (browser) {
                case "firefox":
                    driver = createFirefoxDriver(headless);
                    break;
                case "edge":
                    driver = createEdgeDriver(headless);
                    break;
                case "chrome":
                default:
                    browser = "chrome";
                    driver = createChromeDriver(headless);
                    break;
            }
        } catch (Exception e) {
            System.err.printf("[DriverManager ERROR] Failed to launch '%s' browser: %s%n", browser, e.getMessage());
            // Graceful fallback to Chrome if secondary browser binary is missing on host machine
            if (!browser.equals("chrome")) {
                System.out.printf("[DriverManager] Falling back to default Chrome browser...%n");
                browser = "chrome";
                driver = createChromeDriver(headless);
            } else {
                throw new RuntimeException("Could not initialize browser driver: " + browser, e);
            }
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().window().maximize();

        driverThreadLocal.set(driver);
        browserNameThreadLocal.set(browser);

        return driver;
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--log-level=3");
        options.addArguments("--silent");
        options.addArguments("--disable-logging");

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        options.addArguments("--disable-notifications");
        options.addPreference("dom.webnotifications.enabled", false);

        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--log-level=3");

        return new EdgeDriver(options);
    }

    /**
     * Retrieves the WebDriver instance associated with the calling thread.
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Retrieves the browser name associated with the calling thread.
     */
    public static String getBrowserName() {
        String name = browserNameThreadLocal.get();
        return name != null ? name : "chrome";
    }

    /**
     * Safely closes the browser session and removes ThreadLocal references
     * to eliminate memory leaks and orphan processes.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            } finally {
                driverThreadLocal.remove();
                browserNameThreadLocal.remove();
            }
        }
    }
}
