package com.magicbricks.base;

import com.magicbricks.utils.ConfigReader;
import com.magicbricks.utils.WaitHelper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for all Page Objects. Provides:
 * - WebDriver and WaitHelper references
 * - PageFactory initialization
 * - Common helpers: scroll, highlight, action delay, page title
 *
 * All page classes (HomePage, LoginPage, etc.) extend this.
 * Follows OCP — extendable for future pages without modification.
 * Follows LSP — any subclass is safely substitutable.
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
     * Scrolls the element into the center of the viewport so the tester
     * can visually see which element is about to be interacted with.
     */
    protected void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Briefly highlights an element with a red border to draw the tester's
     * attention before an interaction. Controlled by config.properties.
     */
    protected void highlightElement(WebElement element) {
        if (ConfigReader.isHighlightEnabled()) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String originalStyle = element.getAttribute("style");
            js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    element,
                    "border: 3px solid red; background: lightyellow;" + originalStyle);
            actionDelay();
            js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    element,
                    originalStyle == null ? "" : originalStyle);
        }
    }

    /**
     * Pauses briefly so the tester watching can follow the test execution.
     * Only for visual pacing — NOT a substitute for explicit waits.
     */
    protected void actionDelay() {
        try {
            Thread.sleep(ConfigReader.getActionDelay());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Combines scroll + highlight + wait for visibility before returning the element.
     * Use this before click/sendKeys actions for tester-visible interactions.
     */
    protected WebElement prepareElement(WebElement element) {
        waitHelper.waitForVisibility(element);
        scrollToElement(element);
        highlightElement(element);
        return element;
    }

    /**
     * Waits for element to be visible.
     */
    protected WebElement waitForElementVisible(WebElement element) {
        return waitHelper.waitForVisibility(element);
    }

    /**
     * Waits for element to be clickable.
     */
    protected WebElement waitForElementClickable(WebElement element) {
        return waitHelper.waitForClickability(element);
    }
}
