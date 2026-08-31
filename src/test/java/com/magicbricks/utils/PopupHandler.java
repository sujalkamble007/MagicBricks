package com.magicbricks.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

/**
 * Utility class dedicated to detecting and safely dismissing initial overlay popups,
 * notification permission bars, cookie consent notices, and promotional dialogs
 * on MagicBricks.
 *
 * Temporarily sets implicit wait to 100ms during checks to avoid blocking page execution
 * when no popups are present.
 */
public class PopupHandler {

    private static final By[] POPUP_CLOSE_LOCATORS = {
            By.cssSelector("div.popup-close, span.close-icn, .mb-popup__close"),
            By.cssSelector("button.cookie-consent-close, div.cookie-accept, #cookie-btn"),
            By.cssSelector("div.cross-icn, a.cross-icon, span.close_icon"),
            By.cssSelector("div#top-down-banner .close, div#launchPackBannerTop .close"),
            By.cssSelector("div[class*='notification-dialog'] .close")
    };

    /**
     * Inspects the DOM for known initial popup/overlay banners and closes them if present.
     * Uses zero-timeout probing so tests start immediately without delay.
     *
     * @param driver Current WebDriver instance
     */
    public static void handleInitialPopups(WebDriver driver) {
        try {
            // Temporarily set implicit wait to 100ms for instant non-blocking check
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(100));
            for (By locator : POPUP_CLOSE_LOCATORS) {
                List<WebElement> closeButtons = driver.findElements(locator);
                for (WebElement btn : closeButtons) {
                    if (btn.isDisplayed()) {
                        btn.click();
                        break;
                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            // Restore configured implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        }
    }
}
