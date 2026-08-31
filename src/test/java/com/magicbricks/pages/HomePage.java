package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the MagicBricks Home Page (https://www.magicbricks.com/).
 *
 * Contains PageFactory locators (@FindBy) and interaction methods for:
 * - Header navigation and utility links (Logo, City Selector, Login, Post Property)
 * - Search category tabs (Buy, Rent, PG, Plot, Commercial, New Projects)
 * - Search input field with autocomplete suggestions
 * - Login dropdown menu
 *
 * Follows Single Responsibility Principle (SRP) and Open/Closed Principle (OCP).
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

    @FindBy(css = "div.mb-header__main__login > a.js-menu-link")
    private WebElement loginButton;

    @FindBy(css = "a.mb-login__drop-cta")
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

    // ==================== SEARCH BOX ====================

    @FindBy(id = "keyword")
    private WebElement searchInput;

    @FindBy(css = "div.mb-search__suggestions")
    private WebElement suggestionsContainer;

    @FindBy(css = "div.mb-search__location__error")
    private WebElement locationErrorMessage;

    // Suggestion container locators for explicit wait polling
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

    // ==================== HEADER — CLICK ACTIONS ====================

    public void clickLoginButton() {
        prepareElement(loginButton).click();
        actionDelay();
    }

    /**
     * Checks if the Login/Sign Up CTA inside the login dropdown is visible.
     */
    public boolean isLoginSignUpCtaDisplayed() {
        try {
            return waitForElementVisible(loginSignUpCta).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLoginSignUpCta() {
        prepareElement(loginSignUpCta).click();
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

    public String getActiveSearchTabText() {
        if (isSearchBuyTabActive()) return searchBuyTab.getText().trim();
        if (isSearchRentTabActive()) return searchRentTab.getText().trim();
        return "";
    }

    // ==================== SEARCH BOX — INTERACTIONS ====================

    /**
     * Clicks on the search input to focus it before typing.
     */
    public void clickSearchInput() {
        prepareElement(searchInput).click();
        actionDelay();
    }

    /**
     * Enters search text character by character to trigger live keyup listeners.
     */
    public void enterSearchText(String text) {
        WebElement input = prepareElement(searchInput);
        input.click();
        input.clear();
        for (char c : text.toCharArray()) {
            input.sendKeys(String.valueOf(c));
        }
        actionDelay();
    }

    /**
     * Waits explicitly for the autocomplete suggestions dropdown to appear.
     */
    public boolean isSuggestionsDropdownDisplayed() {
        try {
            return waitHelper.waitForAnyElementVisible(SUGGESTION_LOCATORS);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLocationErrorDisplayed() {
        try {
            return locationErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
