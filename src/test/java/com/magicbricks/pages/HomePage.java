package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.Set;

/**
 * Page Object for the MagicBricks Home Page (https://www.magicbricks.com/).
 *
 * Contains all locators (via @FindBy PageFactory) and interaction methods
 * for: header elements, navigation tabs, search category tabs, search box,
 * autocomplete suggestions, and login dropdown.
 *
 * Locator strategy rationale:
 * - ID: used where available (tabBUY, tabRENT, keyword, etc.) — most reliable
 * - CSS: used for class-based elements without IDs (logo, login button, post property)
 * - XPath: used only for the New Projects tab which lacks a stable ID and is
 *   an anchor (<a>) among sibling divs, making pure CSS insufficient
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
    private WebElement loginButton; // TODO: verify against live DOM — child combinator specificity

    @FindBy(css = "a.mb-login__drop-cta")
    private WebElement loginSignUpCta;

    @FindBy(css = "div.mb-header__main__dropdown.mb-login")
    private WebElement loginDropdownContainer; // TODO: verify against live DOM — dropdown panel classes

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
     * Used by TC_LG_001 after clicking the Login header button.
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
            return searchPgTab.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchPlotTabDisplayed() {
        try {
            return searchPlotTab.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchCommercialTabDisplayed() {
        try {
            return searchCommercialTab.isDisplayed();
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
     * Some sites require explicit focus to activate autocomplete listeners.
     */
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
        org.openqa.selenium.By[] locators = {
            org.openqa.selenium.By.cssSelector("div.mb-search__dropdown"),
            org.openqa.selenium.By.cssSelector("div.mb-search__suggestions"),
            org.openqa.selenium.By.cssSelector("div[class*='auto-suggest']"),
            org.openqa.selenium.By.cssSelector("div[class*='suggestion']"),
            org.openqa.selenium.By.cssSelector("div.mb-search__suggestions__item")
        };
        long endTime = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < endTime) {
            for (org.openqa.selenium.By loc : locators) {
                java.util.List<WebElement> elements = driver.findElements(loc);
                for (WebElement el : elements) {
                    try {
                        if (el.isDisplayed()) {
                            return true;
                        }
                    } catch (Exception ignored) {}
                }
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {}
        }
        return false;
    }

    public boolean isLocationErrorDisplayed() {
        try {
            return locationErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== WINDOW HANDLE HELPERS ====================

    /**
     * Switches the WebDriver focus to a newly opened tab.
     * Used when Login/Sign Up CTA opens the login page in a new browser tab.
     * Waits for the second window handle to become available.
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
     * Returns to the original (first) browser tab.
     */
    public void switchToOriginalTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (!tabs.isEmpty()) {
            driver.switchTo().window(tabs.get(0));
        }
    }
}
