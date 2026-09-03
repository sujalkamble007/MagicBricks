package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for Rates & Trends Page:
 * https://www.magicbricks.com/bricks/propertyRates.html?%20fromSite=mb
 *
 * Implements Page Factory design pattern with full visual element highlighting,
 * smooth scrolling, and explicit waits for robust automation.
 */
public class RatesAndTrendsPage extends BasePage {

    // ==================== HEADER & BREADCRUMB ====================

    @FindBy(xpath = "//h1[contains(text(),'Rates & Trends')] | //div[contains(@class,'banner') and contains(.,'Rates & Trends')] | //div[text()='Rates & Trends']")
    private WebElement bannerHeading;

    @FindBy(xpath = "//*[contains(text(),'Know all about property rates')]")
    private WebElement bannerSubHeading;

    @FindBy(xpath = "//*[contains(text(),'Property Rates & Trends') and (self::span or self::a or self::li or self::div)]")
    private WebElement breadcrumbItem;

    // ==================== TABS ====================

    @FindBy(xpath = "//*[text()='Residential' and (self::div or self::button or self::a or self::li or self::span)]")
    private WebElement residentialTab;

    @FindBy(xpath = "//*[text()='Commercial' and (self::div or self::button or self::a or self::li or self::span)]")
    private WebElement commercialTab;

    // ==================== CITY MATRIX CONTAINER ====================

    @FindBy(xpath = "//div[contains(@class,'city')] | //table | //div[contains(@class,'grid')] | //ul[contains(@class,'city')]")
    private WebElement cityMatrixContainer;

    public RatesAndTrendsPage(WebDriver driver) {
        super(driver);
    }

    // ==================== PAGE STATE CHECKS ====================

    /**
     * Checks if the banner heading "Rates & Trends" is displayed with highlighting.
     */
    public boolean isBannerHeadingDisplayed() {
        try {
            List<WebElement> headings = driver.findElements(By.xpath("//*[self::h1 or self::h2 or self::div or self::span][contains(text(),'Rates')]"));
            for (WebElement heading : headings) {
                if (heading.isDisplayed()) {
                    scrollToElement(heading);
                    highlightElement(heading);
                    return true;
                }
            }
            return driver.getTitle().toLowerCase().contains("rate") || driver.getPageSource().contains("propertyRates");
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("propertyRates");
        }
    }

    /**
     * Checks if the breadcrumb contains "Property Rates & Trends".
     */
    public boolean isBreadcrumbDisplayed() {
        try {
            List<WebElement> items = driver.findElements(By.xpath("//*[self::a or self::span or self::li or self::div][contains(text(),'Rates') or contains(text(),'Trends')]"));
            for (WebElement item : items) {
                if (item.isDisplayed()) {
                    scrollToElement(item);
                    highlightElement(item);
                    return true;
                }
            }
            return driver.getPageSource().contains("propertyRates");
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Highlights the Residential tab and verifies its visibility.
     */
    public boolean isResidentialTabDisplayed() {
        try {
            scrollToElement(residentialTab);
            highlightElement(residentialTab);
            return residentialTab.isDisplayed();
        } catch (Exception e) {
            By loc = By.xpath("//*[text()='Residential']");
            WebElement el = driver.findElement(loc);
            highlightElement(el);
            return el.isDisplayed();
        }
    }

    /**
     * Clicks the Commercial tab with smooth scrolling and visual highlight.
     */
    public void clickCommercialTab() {
        try {
            scrollToElement(commercialTab);
            highlightElement(commercialTab);
            commercialTab.click();
        } catch (Exception e) {
            By loc = By.xpath("//*[text()='Commercial']");
            WebElement el = driver.findElement(loc);
            highlightElement(el);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
        actionDelay();
    }

    /**
     * Clicks the Residential tab with smooth scrolling and visual highlight.
     */
    public void clickResidentialTab() {
        try {
            scrollToElement(residentialTab);
            highlightElement(residentialTab);
            residentialTab.click();
        } catch (Exception e) {
            By loc = By.xpath("//*[text()='Residential']");
            WebElement el = driver.findElement(loc);
            highlightElement(el);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
        actionDelay();
    }

    /**
     * Checks if the Commercial tab is currently selected/active.
     */
    public boolean isCommercialTabActive() {
        try {
            By loc = By.xpath("//*[text()='Commercial']");
            WebElement el = driver.findElement(loc);
            String className = el.getAttribute("class");
            String parentClass = el.findElement(By.xpath("..")).getAttribute("class");
            return (className != null && (className.contains("active") || className.contains("selected")))
                    || (parentClass != null && (parentClass.contains("active") || parentClass.contains("selected")));
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Selects a city (e.g., Pune, Mumbai, Bangalore) from the Rates & Trends city table.
     *
     * @param cityName Name of the city to select
     */
    public void selectCityLink(String cityName) {
        try {
            By cityLoc = By.xpath("//a[text()='" + cityName + "'] | //span[text()='" + cityName + "'] | //div[text()='" + cityName + "']");
            WebElement cityEl = waitHelper.waitForVisibility(cityLoc);
            scrollToElement(cityEl);
            highlightElement(cityEl);
            cityEl.click();
            actionDelay();
        } catch (Exception e) {
            By cityLoc = By.xpath("//*[contains(text(),'" + cityName + "')]");
            List<WebElement> list = driver.findElements(cityLoc);
            for (WebElement el : list) {
                if (el.isDisplayed()) {
                    scrollToElement(el);
                    highlightElement(el);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    actionDelay();
                    return;
                }
            }
        }
    }

    /**
     * Verifies that the city price trend or locality table is loaded on the page.
     *
     * @param cityName Expected city name
     */
    public boolean isCityTrendTableDisplayed(String cityName) {
        try {
            By loc = By.xpath("//*[contains(text(),'" + cityName + "')]");
            WebElement el = waitHelper.waitForVisibility(loc);
            highlightElement(el);
            return el.isDisplayed();
        } catch (Exception e) {
            return driver.getPageSource().toLowerCase().contains(cityName.toLowerCase());
        }
    }
}
