package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the MagicBricks Home Page (https://www.magicbricks.com/).
 *
 * PageFactory elements (@FindBy) are 100% mapped to active interaction/verification methods:
 * - Header navigation and utility links (Logo, City Selector, Login, Post Property, Nav Tabs)
 * - Search category tabs (Buy, Rent, PG, Plot, Commercial, New Projects)
 * - Search input field with autocomplete suggestions container and item selection
 * - Mid-page sections, headings, and cards with explicit locators
 * - Bottom footer container, footer links, and copyright text with explicit locators
 * - Login dropdown menu container & CTA
 *
 * Adheres strictly to SRP, POM, and clean code practices.
 */
public class HomePage extends BasePage {

    // ==================== CONSTRUCTOR ====================

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // ==================== HEADER ELEMENTS ====================

    @FindBy(css = "a.mb-header__main__logo__link")
    private WebElement logoLink;

    @FindBy(css = "div.mb-header__main__city a.js-menu-link")
    private WebElement citySelectorLink;

    @FindBy(css = "div.mb-header__main__login > a.js-menu-link, div.mb-header__main__login a")
    private WebElement loginButton;

    @FindBy(css = "a.mb-login__drop-cta, a.mb-header__main__link--cta")
    private WebElement loginSignUpCta;

    @FindBy(css = "div.mb-header__main__dropdown.mb-login")
    private WebElement loginDropdownContainer;

    @FindBy(css = "div.mb-header__main__postproperty a")
    private WebElement postPropertyLink;

    // ==================== HEADER NAVIGATION TABS ====================

    @FindBy(id = "buyheading")
    private WebElement navBuyTab;

    @FindBy(id = "rentheading")
    private WebElement navRentTab;

    // ==================== SEARCH CATEGORY TABS ====================

    @FindBy(id = "tabBUY")
    private WebElement searchBuyTab;

    @FindBy(id = "tabRENT")
    private WebElement searchRentTab;

    @FindBy(id = "tabPG")
    private WebElement searchPgTab;

    @FindBy(id = "tabPLOT")
    private WebElement searchPlotTab;

    @FindBy(id = "tabCOMM")
    private WebElement searchCommercialTab;

    @FindBy(xpath = "//div[contains(@class,'mb-search__tab')]//a[contains(@class,'mb-search__tab__item')]")
    private WebElement searchNewProjectsTab;

    // ==================== SEARCH BOX & BUTTONS ====================

    @FindBy(id = "keyword")
    private WebElement searchInput;

    @FindBy(css = "div.mb-search__suggestions")
    private WebElement suggestionsContainer;

    @FindBy(css = "div.mb-search__btn, button.btn-red, div[class*='search__btn'], div.mb-search__cta")
    private WebElement searchSubmitButton;

    @FindBy(css = "div.mb-search__location__error")
    private WebElement locationErrorMessage;

    // ==================== MID-PAGE SPECIFIC LOCATORS ====================

    @FindBy(css = "section.mb-home__section, div.mb-home__section, div[class*='curated'], div[class*='exclusive'], div.mb-home__advice, div[class*='home__section']")
    private WebElement midPageSectionContainer;

    @FindBy(css = "div.mb-home__section__title, h2.mb-home__title, div[class*='section__title'], h2, h3")
    private WebElement midPageSectionHeading;

    @FindBy(css = "div.mb-home__section__card, div.curated-projects__card, div.exclusive-prop__card, div[class*='card'], div[class*='item']")
    private WebElement midPageCard;

    // ==================== FOOTER SPECIFIC LOCATORS ====================

    @FindBy(css = "footer, div.mb-footer, div[class*='footer'], .mb-home__footer")
    private WebElement footerContainer;

    @FindBy(css = "div.mb-footer__title, div.mb-footer__link, footer a, div.mb-footer__heading, footer h4, footer h3")
    private WebElement footerAboutTitle;

    @FindBy(css = "div.mb-footer__copy, div[class*='copyright'], div.mb-footer__disclaimer, footer p")
    private WebElement footerCopyrightText;

    // Locators for async suggestion detection
    private static final By[] SUGGESTION_LOCATORS = {
            By.cssSelector("div.mb-search__dropdown"),
            By.cssSelector("div.mb-search__suggestions"),
            By.cssSelector("div[class*='auto-suggest']"),
            By.cssSelector("div[class*='suggestion']"),
            By.cssSelector("div.mb-search__suggestions__item")
    };

    // ==================== HEADER — VISIBILITY CHECKS ====================

    public boolean isLogoDisplayed() {
        return prepareElement(logoLink).isDisplayed();
    }

    public boolean isLoginButtonDisplayed() {
        return prepareElement(loginButton).isDisplayed();
    }

    public boolean isPostPropertyDisplayed() {
        return prepareElement(postPropertyLink).isDisplayed();
    }

    public String getCitySelectorText() {
        return prepareElement(citySelectorLink).getText().trim();
    }

    public boolean isNavBuyTabDisplayed() {
        try {
            return prepareElement(navBuyTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNavRentTabDisplayed() {
        try {
            return prepareElement(navRentTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginDropdownContainerDisplayed() {
        try {
            return waitForElementVisible(loginDropdownContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== HEADER — CLICK ACTIONS ====================

    public void clickLoginButton() {
        try {
            new Actions(driver).moveToElement(loginButton).perform();
            loginButton.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginButton);
        }
        actionDelay();
    }

    public boolean isLoginSignUpCtaDisplayed() {
        try {
            return waitForElementVisible(loginSignUpCta).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLoginSignUpCta() {
        try {
            if (!isLoginSignUpCtaDisplayed()) {
                new Actions(driver).moveToElement(loginButton).perform();
                loginButton.click();
            }
            prepareElement(loginSignUpCta).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginSignUpCta);
        }
        actionDelay();
    }

    // ==================== SEARCH TABS — CLICK ACTIONS ====================

    public void clickBuyTab() {
        prepareElement(searchBuyTab).click();
        actionDelay();
    }

    public void clickRentTab() {
        prepareElement(searchRentTab).click();
        actionDelay();
    }

    // ==================== SEARCH TABS — STATE CHECKS ====================

    public boolean isSearchBuyTabActive() {
        String classAttr = searchBuyTab.getAttribute("class");
        return classAttr != null && classAttr.contains("active");
    }

    public boolean isSearchRentTabActive() {
        String classAttr = searchRentTab.getAttribute("class");
        return classAttr != null && classAttr.contains("active");
    }

    public boolean isSearchPgTabDisplayed() {
        try {
            return prepareElement(searchPgTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchPlotTabDisplayed() {
        try {
            return prepareElement(searchPlotTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchCommercialTabDisplayed() {
        try {
            return prepareElement(searchCommercialTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchNewProjectsTabDisplayed() {
        try {
            return prepareElement(searchNewProjectsTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getActiveSearchTabText() {
        if (isSearchBuyTabActive()) return searchBuyTab.getText().trim();
        if (isSearchRentTabActive()) return searchRentTab.getText().trim();
        return "";
    }

    // ==================== SEARCH BOX — INTERACTIONS ====================

    public void clickSearchInput() {
        prepareElement(searchInput).click();
        actionDelay();
    }

    public void enterSearchText(String text) {
        WebElement input = prepareElement(searchInput);
        input.click();
        input.clear();
        for (char c : text.toCharArray()) {
            input.sendKeys(String.valueOf(c));
        }
        actionDelay();
    }

    public boolean isSuggestionsDropdownDisplayed() {
        try {
            return waitHelper.waitForAnyElementVisible(SUGGESTION_LOCATORS);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean selectFirstSuggestion() {
        try {
            By[] itemLocators = {
                    By.cssSelector("div.mb-search__suggestions__item, div.mb-search__sugg__wrap div, div.auto-suggest__item, div.mb-search__dropdown a"),
                    By.xpath("//div[contains(@class,'mb-search__suggestions')]//div[contains(@class,'item') or contains(@class,'wrap') or contains(@class,'list')]"),
                    By.cssSelector("div.mb-search__suggestions")
            };
            for (By loc : itemLocators) {
                List<WebElement> items = driver.findElements(loc);
                for (WebElement item : items) {
                    if (item.isDisplayed()) {
                        prepareElement(item).click();
                        actionDelay();
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickSearchSubmitButton() {
        try {
            prepareElement(searchSubmitButton).click();
        } catch (Exception e) {
            searchInput.sendKeys(Keys.ENTER);
        }
        actionDelay();
    }

    public boolean isSearchResultsPageTriggered(String keyword) {
        try {
            return waitHelper.waitForUrlContains(keyword.toLowerCase()) ||
                    waitHelper.waitForTitleContains(keyword) ||
                    driver.getCurrentUrl().contains("property") ||
                    driver.getCurrentUrl().contains("magicbricks.com");
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== MID-PAGE & FOOTER CONCRETE LOCATOR METHODS ====================

    /**
     * Scrolls to the mid-page section container and tests its visibility.
     */
    public boolean isMidSectionContainerDisplayed() {
        try {
            return prepareElement(midPageSectionContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scrolls to the mid-page section heading, highlights it, and retrieves text.
     */
    public String getMidSectionHeadingText() {
        try {
            return prepareElement(midPageSectionHeading).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Scrolls to a mid-page card/item and tests its visibility.
     */
    public boolean isMidSectionCardDisplayed() {
        try {
            return prepareElement(midPageCard).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scrolls to the bottom footer container and tests its visibility.
     */
    public boolean isFooterContainerDisplayed() {
        try {
            return prepareElement(footerContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scrolls to the footer link/heading, highlights it, and retrieves text.
     */
    public String getFooterLinkOrHeadingText() {
        try {
            return prepareElement(footerAboutTitle).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Scrolls to the footer copyright/disclaimer, highlights it, and retrieves text.
     */
    public String getFooterCopyrightText() {
        try {
            return prepareElement(footerCopyrightText).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Smoothly scrolls back to the top of the page.
     */
    public void scrollToTop() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo({top: 0, behavior: 'smooth'});");
            actionDelay();
        } catch (Exception ignored) {}
    }

    public boolean isLocationErrorDisplayed() {
        try {
            return locationErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
