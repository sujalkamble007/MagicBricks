package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the MagicBricks Home Page.
 * Contains all locators (via @FindBy PageFactory) and interaction methods
 * for header, navigation tabs, search box, and autocomplete suggestions.
 *
 * Locators use a mix of ID, CSS, and XPath based on what the DOM provides:
 * - ID where available (most reliable): tabBUY, tabRENT, keyword, etc.
 * - CSS for class-based elements: logo link, login button, post property
 * - XPath only where CSS cannot distinguish siblings or dynamic text
 */
public class HomePage extends BasePage {

    // ==================== HEADER ELEMENTS ====================

    @FindBy(css = "a.mb-header__main__logo__link")
    private WebElement logoLink;

    @FindBy(css = "div.mb-header__main__city a.js-menu-link")
    private WebElement citySelectorLink;

    @FindBy(css = "div.mb-header__main__login > a.js-menu-link")
    private WebElement loginButton; // TODO: verify against live DOM — child combinator specificity

    @FindBy(css = "a.mb-login__drop-cta")
    private WebElement loginSignUpCta;

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
    private WebElement searchNewProjectsTab; // TODO: verify against live DOM — dynamic animated tab

    // ==================== SEARCH BOX ====================

    @FindBy(id = "keyword")
    private WebElement searchInput;

    @FindBy(css = "div.mb-search__suggestions")
    private WebElement suggestionsContainer; // TODO: verify against live DOM — JS-rendered after typing

    @FindBy(css = "div.mb-search__location__error")
    private WebElement locationErrorMessage;

    // ==================== INTERACTION METHODS (stubs — logic in Day 2+) ====================

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
        return prepareElement(citySelectorLink).getText();
    }

    public void clickLoginButton() {
        prepareElement(loginButton).click();
        actionDelay();
    }

    public void clickLoginSignUpCta() {
        prepareElement(loginSignUpCta).click();
        actionDelay();
    }

    public void clickBuyTab() {
        prepareElement(searchBuyTab).click();
        actionDelay();
    }

    public void clickRentTab() {
        prepareElement(searchRentTab).click();
        actionDelay();
    }

    public void enterSearchText(String text) {
        WebElement input = prepareElement(searchInput);
        input.clear();
        input.sendKeys(text);
        actionDelay();
    }

    public boolean isSuggestionsDropdownDisplayed() {
        try {
            return waitForElementVisible(suggestionsContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchBuyTabActive() {
        String classAttr = searchBuyTab.getAttribute("class");
        return classAttr != null && classAttr.contains("active");
    }

    public boolean isSearchRentTabActive() {
        String classAttr = searchRentTab.getAttribute("class");
        return classAttr != null && classAttr.contains("active");
    }

    public String getActiveSearchTabText() {
        if (isSearchBuyTabActive()) return searchBuyTab.getText();
        if (isSearchRentTabActive()) return searchRentTab.getText();
        return "";
    }

    public boolean isLocationErrorDisplayed() {
        try {
            return locationErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== CONSTRUCTOR ====================

    public HomePage(WebDriver driver) {
        super(driver);
    }
}
