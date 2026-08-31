package com.magicbricks.base;

import com.magicbricks.utils.ConfigReader;
import com.magicbricks.utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Base class for all Page Objects.
 * Provides:
 * - WebDriver & WaitHelper abstractions (strict explicit wait handling)
 * - PageFactory initialization
 * - Smooth element scrolling into viewport center for tester visibility
 * - Visual border highlighting for tester observation
 * - Configurable actionDelay pacing so tester can visually follow interactions
 * - Resilient frame, multi-tab window, and modal dismissal helpers
 *
 * Follows Open/Closed Principle (OCP) and Liskov Substitution Principle (LSP).
 */
public class BasePage {

    protected WebDriver driver;
    protected WaitHelper waitHelper;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Paces UI interactions so the tester can visually observe each step
     * in the open Chrome browser. Controlled via action.delay in config.properties.
     */
    protected void actionDelay() {
        int delay = ConfigReader.getActionDelay();
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Scrolls the target element into the center of the viewport smoothly
     * so that the tester can clearly observe the element being tested.
     */
    protected void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        } catch (Exception ignored) {}
    }

    /**
     * Briefly highlights an element with a distinct border to provide visual cues to the tester.
     */
    protected void highlightElement(WebElement element) {
        if (ConfigReader.isHighlightEnabled()) {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                String originalStyle = element.getAttribute("style");
                js.executeScript(
                        "arguments[0].setAttribute('style', arguments[1]);",
                        element,
                        "border: 3px solid red; background: lightyellow;" + (originalStyle != null ? originalStyle : ""));
                actionDelay();
                js.executeScript(
                        "arguments[0].setAttribute('style', arguments[1]);",
                        element,
                        originalStyle == null ? "" : originalStyle);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Prepares an element for user-visible interaction:
     * waits for visibility, scrolls it into the center of the viewport, and highlights it.
     */
    protected WebElement prepareElement(WebElement element) {
        waitHelper.waitForVisibility(element);
        scrollToElement(element);
        highlightElement(element);
        return element;
    }

    /**
     * Waits for element to be visible via explicit wait.
     */
    protected WebElement waitForElementVisible(WebElement element) {
        return waitHelper.waitForVisibility(element);
    }

    /**
     * Waits for element to be clickable via explicit wait.
     */
    protected WebElement waitForElementClickable(WebElement element) {
        return waitHelper.waitForClickability(element);
    }

    // ==================== MODAL & DIALOG DISMISSAL ====================

    /**
     * Dismisses any active modal overlay by sending the ESCAPE key to the active element.
     */
    public void dismissModalWithEscape() {
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ESCAPE).perform();
        } catch (Exception ignored) {}
    }

    /**
     * Closes a modal dialog if a close button matching the given locator is currently displayed.
     */
    public void closeModalIfPresent(By closeButtonLocator) {
        try {
            List<WebElement> closeButtons = driver.findElements(closeButtonLocator);
            for (WebElement btn : closeButtons) {
                if (btn.isDisplayed()) {
                    btn.click();
                    break;
                }
            }
        } catch (Exception ignored) {}
    }

    // ==================== IFRAME SWITCHING HELPERS ====================

    /**
     * Switches to an iframe if one matching the given locator exists and is displayed.
     * Useful for login modals rendered within an iframe container.
     */
    public boolean switchToFrameIfPresent(By frameLocator) {
        try {
            List<WebElement> frames = driver.findElements(frameLocator);
            if (!frames.isEmpty() && frames.get(0).isDisplayed()) {
                driver.switchTo().frame(frames.get(0));
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Returns focus back to the primary page document from any iframe.
     */
    public void switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
        } catch (Exception ignored) {}
    }

    // ==================== WINDOW & TAB SWITCHING HELPERS ====================

    /**
     * Switches to a new tab/window when a link or CTA opens a secondary window.
     */
    public void switchToNewTab() {
        String originalHandle = driver.getWindowHandle();
        waitHelper.waitForNumberOfWindows(2);
        Set<String> allHandles = driver.getWindowHandles();
        for (String handle : allHandles) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    /**
     * Switches to a window whose title contains the expected fragment.
     */
    public boolean switchToWindowByTitle(String expectedTitleFragment) {
        String originalHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            driver.switchTo().window(handle);
            if (driver.getTitle().contains(expectedTitleFragment)) {
                return true;
            }
        }
        driver.switchTo().window(originalHandle);
        return false;
    }

    /**
     * Closes the current active tab and returns driver control to the original primary tab.
     */
    public void closeCurrentTabAndSwitchBack() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (tabs.size() > 1) {
            driver.close();
            driver.switchTo().window(tabs.get(0));
        }
    }

    /**
     * Returns to the primary base window (index 0).
     */
    public void switchToOriginalTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (!tabs.isEmpty()) {
            driver.switchTo().window(tabs.get(0));
        }
    }
}
