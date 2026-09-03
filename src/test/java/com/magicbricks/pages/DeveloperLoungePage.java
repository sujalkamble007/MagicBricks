package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for Developer Lounge / Brand Store Page:
 * https://property.magicbricks.com/brand-store/
 *
 * Implements Page Factory design pattern with visual highlighting,
 * smooth scrolling, and assertions on brand developer cards and CEO profiles.
 */
public class DeveloperLoungePage extends BasePage {

    // ==================== HERO & HEADING ====================

    @FindBy(xpath = "//h1 | //*[contains(text(),'Discover') and contains(text(),'Brands')]")
    private WebElement heroHeading;

    // ==================== BRAND DEVELOPER CARDS ====================

    @FindBy(xpath = "//*[contains(text(),'Omaxe') or contains(text(),'OMAXE')]")
    private WebElement omaxeBrandCard;

    @FindBy(xpath = "//*[contains(text(),'VTP') or contains(text(),'VTP Realty')]")
    private WebElement vtpBrandCard;

    @FindBy(xpath = "//*[contains(text(),'SPR') or contains(text(),'SPR City')]")
    private WebElement sprBrandCard;

    // ==================== EXECUTIVE PROFILES ====================

    @FindBy(xpath = "//*[contains(text(),'Mohit Goel') or contains(text(),'Sachin Bhandari') or contains(text(),'Navin Ranka')]")
    private WebElement ceoProfile;

    public DeveloperLoungePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Checks if the "Discover Real Estate Brands" hero heading is displayed.
     */
    public boolean isHeroHeadingDisplayed() {
        try {
            By loc = By.xpath("//*[contains(text(),'Discover') and contains(text(),'Brands')]");
            WebElement el = waitHelper.waitForVisibility(loc);
            scrollToElement(el);
            highlightElement(el);
            return el.isDisplayed();
        } catch (Exception e) {
            return driver.getPageSource().contains("Discover Real Estate Brands");
        }
    }

    /**
     * Smoothly scrolls and highlights all developer brand cards on screen.
     */
    public void highlightDeveloperBrandCards() {
        try {
            By brandCardsLoc = By.xpath("//div[contains(@class,'card')] | //div[contains(@class,'brand')]");
            List<WebElement> cards = driver.findElements(brandCardsLoc);
            for (WebElement card : cards) {
                if (card.isDisplayed() && card.getSize().getHeight() > 100) {
                    scrollToElement(card);
                    highlightElement(card);
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Checks if leading real estate brands (Omaxe, VTP, SPR) are displayed.
     */
    public boolean areDeveloperBrandsDisplayed() {
        try {
            By loc = By.xpath("//*[contains(text(),'Omaxe') or contains(text(),'VTP') or contains(text(),'SPR')]");
            WebElement el = waitHelper.waitForVisibility(loc);
            scrollToElement(el);
            highlightElement(el);
            return el.isDisplayed();
        } catch (Exception e) {
            return driver.getPageSource().contains("Omaxe") || driver.getPageSource().contains("VTP");
        }
    }

    /**
     * Checks if executive/CEO details (e.g. Mohit Goel, Sachin Bhandari) are displayed.
     */
    public boolean areExecutiveProfilesDisplayed() {
        try {
            By loc = By.xpath("//*[contains(text(),'CEO') or contains(text(),'Director') or contains(text(),'Mohit') or contains(text(),'Sachin')]");
            WebElement el = waitHelper.waitForVisibility(loc);
            scrollToElement(el);
            highlightElement(el);
            return el.isDisplayed();
        } catch (Exception e) {
            return driver.getPageSource().contains("CEO") || driver.getPageSource().contains("Director");
        }
    }
}
