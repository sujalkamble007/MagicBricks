package com.magicbricks.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Encapsulates explicit wait operations using WebDriverWait + ExpectedConditions.
 * Test classes and page classes depend on this abstraction rather than directly
 * constructing WebDriverWait instances (DIP compliance).
 */
public class WaitHelper {

    private final WebDriverWait wait;

    public WaitHelper(WebDriver driver) {
        int timeout = ConfigReader.getExplicitWait();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public WaitHelper(WebDriver driver, int timeoutInSeconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    /**
     * Waits until the element is visible on the page.
     */
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the element is clickable.
     */
    public WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the page title contains the expected text.
     */
    public boolean waitForTitleContains(String titleFragment) {
        return wait.until(ExpectedConditions.titleContains(titleFragment));
    }

    /**
     * Waits until a specific number of windows/tabs are open.
     */
    public boolean waitForNumberOfWindows(int count) {
        return wait.until(ExpectedConditions.numberOfWindowsToBe(count));
    }

    /**
     * Waits until the element becomes invisible or is removed from the DOM.
     */
    public boolean waitForInvisibility(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }
}
