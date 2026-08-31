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
 * Implements individual test cases for each progressive section with concrete locator validation:
 * - TC_HP_001: Verify Home Page Title (Top Section - Smoke)
 * - TC_HP_002: Verify Header Elements Visibility (Top Section - Smoke)
 * - TC_HP_003: Verify Search Category Tabs Presence and Default State (Search Hero - Regression)
 * - TC_HP_004: Verify Search Autocomplete Suggestions Trigger (Search Hero - Data-Driven Regression)
 * - TC_HP_005: Verify Tab Switching Interactivity between Buy and Rent (Search Hero - Regression)
 * - TC_HP_006: Verify Mid-Page Section Scroll & Content Locators (Mid Page Scroll - Regression)
 * - TC_HP_007: Verify Bottom Footer Section Scroll & Content Locators (Bottom Page Scroll - Regression)
 * - TC_HP_008: Verify Scroll To Bottom and Return To Top Behavior (Full Page Traversal - Regression)
 */
public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver);
    }

    /**
     * TC_HP_001: Verify Home Page Title
     * Section: Top of Page | Type: Positive | Priority: P0 | Groups: homepage, smoke
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
     * Section: Top Header Bar | Type: Positive | Priority: P0 | Groups: homepage, smoke
     */
    @Test(priority = 2, groups = {"homepage", "smoke"},
            description = "TC_HP_002: Verify header elements (logo, login, post property, city) are visible")
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
     * Section: Search Hero Container | Type: Positive | Priority: P0 | Groups: homepage, regression
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
     * Section: Search Input Field | Type: Positive | Priority: P1 | Groups: homepage, regression
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
     * Section: Search Category Tabs | Type: Positive | Priority: P1 | Groups: homepage, regression
     */
    @Test(priority = 5, groups = {"homepage", "regression"},
            description = "TC_HP_005: Verify Buy/Rent tab switching toggles active state")
    public void verifyTabSwitchingBuyRent() {
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

    /**
     * TC_HP_006: Verify Mid-Page Section Scroll & Content Locators
     * Section: Mid-Page Content Section | Type: Positive | Priority: P1 | Groups: homepage, regression
     * Steps:
     *   1. Land on Home Page.
     *   2. Scroll down to mid-page section container.
     *   3. Highlight and assert container visibility.
     *   4. Highlight and assert heading text is not empty.
     */
    @Test(priority = 6, groups = {"homepage", "regression"},
            description = "TC_HP_006: Verify scroll to mid-page and validate section container and heading locators")
    public void verifyMidPageSectionScrollAndContent() {
        SoftAssert softAssert = new SoftAssert();

        boolean isContainerVisible = homePage.isMidSectionContainerDisplayed();
        softAssert.assertTrue(isContainerVisible, "Mid-page section container must be visible after scrolling");

        String headingText = homePage.getMidSectionHeadingText();
        softAssert.assertFalse(headingText.isEmpty(), "Mid-page section heading text must not be empty");

        softAssert.assertAll();
    }

    /**
     * TC_HP_007: Verify Bottom Footer Section Scroll & Content Locators
     * Section: Bottom Footer Area | Type: Positive | Priority: P1 | Groups: homepage, regression
     * Steps:
     *   1. Land on Home Page.
     *   2. Scroll all the way down to footer area.
     *   3. Highlight and assert footer container visibility.
     *   4. Highlight and assert footer links / text presence.
     */
    @Test(priority = 7, groups = {"homepage", "regression"},
            description = "TC_HP_007: Verify scroll to bottom and validate footer container and link locators")
    public void verifyFooterSectionScrollAndLinks() {
        SoftAssert softAssert = new SoftAssert();

        boolean isFooterVisible = homePage.isFooterContainerDisplayed();
        softAssert.assertTrue(isFooterVisible, "Footer container must be visible after scrolling to the bottom");

        String footerLinkText = homePage.getFooterLinkOrHeadingText();
        softAssert.assertFalse(footerLinkText.isEmpty(), "Footer title or link text must not be empty");

        softAssert.assertAll();
    }

    /**
     * TC_HP_008: Verify Scroll To Bottom and Return To Top Behavior
     * Section: Full Page Traversal | Type: Positive | Priority: P1 | Groups: homepage, regression
     * Steps:
     *   1. Scroll to footer container at the bottom.
     *   2. Scroll smoothly all the way back to the top.
     *   3. Assert MagicBricks Logo is visible at top.
     */
    @Test(priority = 8, groups = {"homepage", "regression"},
            description = "TC_HP_008: Verify smooth scroll to bottom and return to top")
    public void verifyScrollToTopBehavior() {
        homePage.isFooterContainerDisplayed();
        homePage.scrollToTop();
        Assert.assertTrue(homePage.isLogoDisplayed(),
                "MagicBricks Logo must be visible after returning to top of page");
    }
}
