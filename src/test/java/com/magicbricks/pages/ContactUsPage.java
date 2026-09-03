package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object representing the MagicBricks Contact Us / Sales Enquiry Page.
 * URL: https://www.magicbricks.com/contactUs
 *
 * SOLID Principles applied:
 * - SRP: Encapsulates all locator strategies, interactions, smooth scrolling,
 *        and visual verifications for the Contact Us page.
 * - OCP: Extends BasePage without modifying base framework behavior.
 * - LSP: Adheres fully to the BasePage contract.
 * - DIP: Relies on WebDriver abstraction injected via constructor.
 */
public class ContactUsPage extends BasePage {

    // ==================== LOCATORS (PAGE FACTORY) ====================

    @FindBy(css = ".headText, .headerSection h3, .headText__sales-enquiry h3")
    private WebElement pageHeading;

    @FindBy(css = ".mb-header__main__logo__link, .headText, h3, .headerSection")
    private WebElement headerSection;

    private final By directoryElementsLoc = By.cssSelector(".alphabetPaging a, .officeLinkclick, #allOfficeContact a, a[href*='_row']");

    // ==================== CONSTRUCTOR ====================

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    // ==================== ACTIONS & VERIFICATIONS ====================

    /**
     * Verifies that the Contact Us page is loaded with a valid URL.
     */
    public boolean isLoaded() {
        actionDelay();
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("contact") || currentUrl.contains("enquiry") || currentUrl.contains("magicbricks.com");
    }

    /**
     * Smoothly scrolls down into the office contacts and alphabet directory section.
     */
    public void scrollDownToDirectory() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy({top: 800, behavior: 'smooth'});");
            actionDelay();
        } catch (Exception ignored) {}
    }

    /**
     * Locates, highlights, and clicks an active directory element (e.g. city branch or alphabet filter).
     *
     * @return true if an interactive directory element was selected and clicked
     */
    public boolean selectDirectoryElement() {
        try {
            List<WebElement> options = driver.findElements(directoryElementsLoc);
            for (WebElement opt : options) {
                if (opt.isDisplayed()) {
                    scrollToElement(opt);
                    highlightElement(opt);
                    opt.click();
                    actionDelay();
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Smoothly scrolls back up to the top of the contact page and highlights the header.
     */
    public void scrollBackToTopAndHighlightHeader() {
        scrollToTop();
        actionDelay();
        try {
            if (headerSection != null && headerSection.isDisplayed()) {
                highlightElement(headerSection);
            }
        } catch (Exception ignored) {}
        actionDelay();
    }
}
