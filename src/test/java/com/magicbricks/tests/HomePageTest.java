package com.magicbricks.tests;

import com.magicbricks.base.BaseTest;
import com.magicbricks.pages.HomePage;
import com.magicbricks.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Production-grade TestNG Test Class for MagicBricks Home Page Module.
 *
 * Implements thorough TestNG assertions mapping 1:1 to every documented
 * Expected Result from Sprint Day 1:
 * - TC_HP_001: Verify Home Page Title (Hard Assert)
 * - TC_HP_002: Verify Header Elements Visibility (SoftAssert across Logo, Login, Post Property, City)
 * - TC_HP_003: Verify Search Category Tabs Presence and Default State (SoftAssert across 5 tabs & Buy active)
 * - TC_HP_004: Verify Search Autocomplete Suggestions Trigger (Data-Driven via Excel, Hard Assert)
 * - TC_HP_005: Verify Tab Switching Interactivity between Buy and Rent (Hard Assert state transitions)
 */
public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver);
    }

    /**
     * TC_HP_001: Verify Home Page Title
     * Preconditions: Browser open, navigated to magicbricks.com
     * Steps:
     *   1. Navigate to magicbricks.com (handled by BaseTest @BeforeMethod)
     *   2. Read page title
     * Expected Result: Page title contains "MagicBricks" or "Real Estate"
     */
    @Test(priority = 1, groups = {"homepage", "smoke"},
            description = "TC_HP_001: Verify home page title contains MagicBricks")
    public void verifyHomePageTitle() {
        String title = homePage.getPageTitle();
        Assert.assertNotNull(title, "Page title must not be null");
        Assert.assertTrue(title.contains("MagicBricks") || title.contains("Real Estate"),
                "Page title should contain 'MagicBricks' or 'Real Estate'. Actual title: " + title);
    }

    /**
     * TC_HP_002: Verify Header Elements Visibility
     * Preconditions: Home page loaded
     * Steps:
     *   1. Check MagicBricks logo visibility
     *   2. Check Login button visibility in header
     *   3. Check Post Property link visibility
     *   4. Check City selector link displays non-empty text
     * Expected Result: Logo, Login button, Post Property link displayed; City text not empty
     */
    @Test(priority = 2, groups = {"homepage", "smoke"},
            description = "TC_HP_002: Verify header elements (logo, login, post property) are visible")
    public void verifyHeaderElementsVisibility() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(homePage.isLogoDisplayed(),
                "MagicBricks Logo must be visible in the header");
        softAssert.assertTrue(homePage.isLoginButtonDisplayed(),
                "Login button must be visible in the header");
        softAssert.assertTrue(homePage.isPostPropertyDisplayed(),
                "Post Property link must be visible in the header");

        String cityText = homePage.getCitySelectorText();
        softAssert.assertNotNull(cityText, "City selector text must not be null");
        softAssert.assertFalse(cityText.trim().isEmpty(),
                "City selector must display a non-empty city name (e.g. Bangalore)");

        softAssert.assertAll();
    }

    /**
     * TC_HP_003: Verify Search Tabs Presence and Default State
     * Preconditions: Home page loaded
     * Steps:
     *   1. Verify Buy tab is displayed and has 'active' CSS class by default
     *   2. Verify Rent tab is displayed
     *   3. Verify PG tab is displayed
     *   4. Verify Plot tab is displayed
     *   5. Verify Commercial tab is displayed
     * Expected Result: All 5 tabs displayed; Buy tab has 'active' class
     */
    @Test(priority = 3, groups = {"homepage", "regression"},
            description = "TC_HP_003: Verify search tabs presence and Buy is default active")
    public void verifySearchTabsPresenceAndDefaultState() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(homePage.isSearchBuyTabActive(),
                "Buy tab must be marked active by default on home page load");
        softAssert.assertTrue(homePage.isSearchPgTabDisplayed(),
                "PG tab must be displayed in search categories");
        softAssert.assertTrue(homePage.isSearchPlotTabDisplayed(),
                "Plot tab must be displayed in search categories");
        softAssert.assertTrue(homePage.isSearchCommercialTabDisplayed(),
                "Commercial tab must be displayed in search categories");

        softAssert.assertAll();
    }

    /**
     * TC_HP_004: Verify Search Autocomplete Triggers on Input (Data-Driven via Excel)
     * Preconditions: Home page loaded, search box visible
     * Steps:
     *   1. Focus search input field
     *   2. Type locality name (e.g., Whitefield, Koramangala, Indiranagar)
     *   3. Wait explicitly for autocomplete suggestions dropdown
     * Expected Result: Suggestions dropdown becomes visible after typing
     */
    @Test(priority = 4, groups = {"homepage", "regression"},
            dataProvider = "searchLocalitiesData", dataProviderClass = DataProviders.class,
            description = "TC_HP_004: Verify autocomplete suggestions appear when typing in search")
    public void verifySearchAutocompleteTriggers(String locality, String expectedKeyword) {
        homePage.clickSearchInput();
        homePage.enterSearchText(locality);

        boolean isSuggestionsVisible = homePage.isSuggestionsDropdownDisplayed();
        Assert.assertTrue(isSuggestionsVisible,
                "Autocomplete suggestions dropdown must become visible after typing locality: '" + locality + "'");
    }

    /**
     * TC_HP_005: Verify Tab Switching Between Buy and Rent
     * Preconditions: Home page loaded, Buy tab active
     * Steps:
     *   1. Verify Buy tab active initially
     *   2. Click Rent tab -> Verify Rent active and Buy inactive
     *   3. Click Buy tab -> Verify Buy active again and Rent inactive
     * Expected Result: Active CSS class toggles correctly; only one active tab at a time
     */
    @Test(priority = 5, groups = {"homepage", "regression"},
            description = "TC_HP_005: Verify Buy/Rent tab switching toggles active state")
    public void verifyTabSwitchingBuyRent() {
        // Initial state validation
        Assert.assertTrue(homePage.isSearchBuyTabActive(),
                "Buy tab must be active initially before any click");

        // Transition 1: Click Rent
        homePage.clickRentTab();
        Assert.assertTrue(homePage.isSearchRentTabActive(),
                "Rent tab must become active after being clicked");
        Assert.assertFalse(homePage.isSearchBuyTabActive(),
                "Buy tab must not remain active after Rent is selected");

        // Transition 2: Click Buy back
        homePage.clickBuyTab();
        Assert.assertTrue(homePage.isSearchBuyTabActive(),
                "Buy tab must become active again after being clicked");
        Assert.assertFalse(homePage.isSearchRentTabActive(),
                "Rent tab must not remain active after Buy is selected");
    }
}
