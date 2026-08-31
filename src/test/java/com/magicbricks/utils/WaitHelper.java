package com.magicbricks.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Encapsulates explicit wait operations using WebDriverWait and ExpectedConditions.
 * Provides strict explicit wait abstractions across page objects and test classes
 * ensuring 100% elimination of arbitrary Thread.sleep calls (DIP & SRP compliance).
 */
public class WaitHelper {

    private final WebDriverWait wait;
    private final WebDriver driver;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        int timeout = ConfigReader.getExplicitWait();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public WaitHelper(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    /**
     * Waits until the specified WebElement is visible.
     */
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the element located by By is visible.
     */
    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the specified WebElement is clickable.
     */
    public WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the element located by By is clickable.
     */
    public WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the element is present in the DOM.
     */
    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits until the page title contains the expected text.
     */
    public boolean waitForTitleContains(String titleFragment) {
        return wait.until(ExpectedConditions.titleContains(titleFragment));
    }

    /**
     * Waits until the URL contains the expected fraction.
     */
    public boolean waitForUrlContains(String urlFraction) {
        return wait.until(ExpectedConditions.urlContains(urlFraction));
    }

    /**
     * Waits until a specific number of windows/tabs are open.
     */
    public boolean waitForNumberOfWindows(int count) {
        return wait.until(ExpectedConditions.numberOfWindowsToBe(count));
    }

    /**
     * Waits until the element becomes invisible.
     */
    public boolean waitForInvisibility(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Waits for an iframe to be available and switches to it.
     */
    public void waitForFrameAndSwitch(By frameLocator) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    /**
     * Waits for at least one element matching any of the given locators to become visible.
     */
    public boolean waitForAnyElementVisible(By[] locators) {
        return wait.until(d -> {
            for (By loc : locators) {
                List<WebElement> elements = d.findElements(loc);
                for (WebElement el : elements) {
                    try {
                        if (el.isDisplayed()) {
                            return true;
                        }
                    } catch (Exception ignored) {}
                }
            }
            return false;
        });
    }
}
