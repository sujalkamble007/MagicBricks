package com.magicbricks.tests;

import com.magicbricks.base.BaseTest;
import com.magicbricks.pages.HomePage;
import com.magicbricks.utils.ConsoleLogger;
import com.magicbricks.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Production Test Suite for MagicBricks Home Page.
 * Covers 8 functional test scenarios (10 total test executions):
 * 1. TC_HP_001: Page title validation
 * 2. TC_HP_002: Header elements visibility (Logo, Login, Post Property, City)
 * 3. TC_HP_003: Search category tabs presence & Buy tab default active state
 * 4. TC_HP_004: Autocomplete suggestions trigger on input (Data-Driven: Whitefield, Koramangala, Indiranagar)
 * 5. TC_HP_005: Tab switching between Buy and Rent
 * 6. TC_HP_006: Mid-page section scroll & container/heading locators
 * 7. TC_HP_007: Bottom footer section scroll & container/link locators
 * 8. TC_HP_008: Full page traversal (Scroll to bottom, return to top, logo assertion)
 */
public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        homePage = new HomePage(driver);
    }

    /**
     * TC_HP_001: Verify Home Page Title
     * Section: Top of Page | Type: Positive | Priority: P0 | Groups: homepage, smoke
     */
    @Test(priority = 1, groups = {"homepage", "smoke"},
            description = "TC_HP_001: Verify home page title contains MagicBricks")
    public void verifyHomePageTitle() {
        ConsoleLogger.logStep(1, "Retrieving MagicBricks home page title...");
        String title = homePage.getPageTitle();
        ConsoleLogger.logStep(2, "Asserting title contains 'MagicBricks' or 'Real Estate' (Actual: " + title + ")...");
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

        ConsoleLogger.logStep(1, "Validating MagicBricks header logo visibility...");
        softAssert.assertTrue(homePage.isLogoDisplayed(),
                "MagicBricks Logo must be visible in the header");

        ConsoleLogger.logStep(2, "Validating Login button visibility in header...");
        softAssert.assertTrue(homePage.isLoginButtonDisplayed(),
                "Login button must be visible in the header");

        ConsoleLogger.logStep(3, "Validating 'Post Property' free listing CTA...");
        softAssert.assertTrue(homePage.isPostPropertyDisplayed(),
                "Post Property link must be visible in the header");

        String cityText = homePage.getCitySelectorText();
        ConsoleLogger.logStep(4, "Validating default City selector (Current City: " + cityText + ")...");
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

        ConsoleLogger.logStep(1, "Verifying 'Buy' search tab is active by default...");
        softAssert.assertTrue(homePage.isSearchBuyTabActive(),
                "Buy tab must be marked active by default on home page load");

        ConsoleLogger.logStep(2, "Verifying 'PG', 'Plot', and 'Commercial' search category tabs are visible...");
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
        ConsoleLogger.logStep(1, "Clicking search input field...");
        homePage.clickSearchInput();

        ConsoleLogger.logStep(2, "Typing locality query: '" + locality + "' into search...");
        homePage.enterSearchText(locality);

        ConsoleLogger.logStep(3, "Verifying autocomplete suggestions dropdown is displayed...");
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
        ConsoleLogger.logStep(1, "Verifying 'Buy' tab is active initially...");
        Assert.assertTrue(homePage.isSearchBuyTabActive(),
                "Buy tab must be active initially before any click");

        ConsoleLogger.logStep(2, "Switching to 'Rent' tab and asserting toggle state...");
        homePage.clickRentTab();
        Assert.assertTrue(homePage.isSearchRentTabActive(),
                "Rent tab must become active after being clicked");
        Assert.assertFalse(homePage.isSearchBuyTabActive(),
                "Buy tab must not remain active after Rent is selected");

        ConsoleLogger.logStep(3, "Switching back to 'Buy' tab and confirming state...");
        homePage.clickBuyTab();
        Assert.assertTrue(homePage.isSearchBuyTabActive(),
                "Buy tab must become active again after being clicked");
        Assert.assertFalse(homePage.isSearchRentTabActive(),
                "Rent tab must not remain active after Buy is selected");
    }

    /**
     * TC_HP_006: Verify Mid-Page Section Scroll & Content Locators
     * Section: Mid-Page Content Section | Type: Positive | Priority: P1 | Groups: homepage, regression
     */
    @Test(priority = 6, groups = {"homepage", "regression"},
            description = "TC_HP_006: Verify scroll to mid-page and validate section container and heading locators")
    public void verifyMidPageSectionScrollAndContent() {
        SoftAssert softAssert = new SoftAssert();

        ConsoleLogger.logStep(1, "Scrolling smoothly down to mid-page section container...");
        boolean isContainerVisible = homePage.isMidSectionContainerDisplayed();
        softAssert.assertTrue(isContainerVisible, "Mid-page section container must be visible after scrolling");

        ConsoleLogger.logStep(2, "Retrieving and validating mid-page section heading...");
        String headingText = homePage.getMidSectionHeadingText();
        softAssert.assertFalse(headingText.isEmpty(), "Mid-page section heading text must not be empty");

        softAssert.assertAll();
    }

    /**
     * TC_HP_007: Verify Bottom Footer Section Scroll & Content Locators
     * Section: Bottom Footer Area | Type: Positive | Priority: P1 | Groups: homepage, regression
     */
    @Test(priority = 7, groups = {"homepage", "regression"},
            description = "TC_HP_007: Verify scroll to bottom and validate footer container and link locators")
    public void verifyFooterSectionScrollAndLinks() {
        SoftAssert softAssert = new SoftAssert();

        ConsoleLogger.logStep(1, "Scrolling all the way down to bottom footer area...");
        boolean isFooterVisible = homePage.isFooterContainerDisplayed();
        softAssert.assertTrue(isFooterVisible, "Footer container must be visible after scrolling to the bottom");

        ConsoleLogger.logStep(2, "Validating footer links and copyright / title locators...");
        String footerLinkText = homePage.getFooterLinkOrHeadingText();
        softAssert.assertFalse(footerLinkText.isEmpty(), "Footer title or link text must not be empty");

        softAssert.assertAll();
    }

    /**
     * TC_HP_008: Verify Scroll To Bottom and Return To Top Behavior
     * Section: Full Page Traversal | Type: Positive | Priority: P1 | Groups: homepage, regression
     */
    @Test(priority = 8, groups = {"homepage", "regression"},
            description = "TC_HP_008: Verify smooth scroll to bottom and return to top")
    public void verifyScrollToTopBehavior() {
        ConsoleLogger.logStep(1, "Scrolling smoothly to bottom footer...");
        homePage.isFooterContainerDisplayed();

        ConsoleLogger.logStep(2, "Smoothly scrolling all the way back up to top of page...");
        homePage.scrollToTop();

        ConsoleLogger.logStep(3, "Asserting MagicBricks header logo is in full viewport view...");
        Assert.assertTrue(homePage.isLogoDisplayed(),
                "MagicBricks Logo must be visible after returning to top of page");
    }
}
