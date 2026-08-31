package com.magicbricks.tests;

import com.magicbricks.base.BaseTest;
import com.magicbricks.pages.HomePage;
import com.magicbricks.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * TestNG Test Class for MagicBricks Home Page Module.
 * Covers:
 * - TC_HP_001: Home Page Title Verification (Smoke)
 * - TC_HP_002: Header Elements Visibility (Smoke)
 * - TC_HP_003: Search Category Tabs Presence & Default Active State (Regression)
 * - TC_HP_004: Search Autocomplete Dropdown Trigger via DataProvider (Regression)
 * - TC_HP_005: Tab Switching Interactivity between Buy and Rent (Regression)
 *
 * Implements real assertions (hard & soft), explicit waits, and visible element scrolling.
 */
public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver);
    }

    /**
     * TC_HP_001: Verify Home Page Title
     * Type: Positive | Priority: P0 | Groups: homepage, smoke
     */
    @Test(priority = 1, groups = {"homepage", "smoke"}, description = "TC_HP_001: Verify home page title contains MagicBricks")
    public void verifyHomePageTitle() {
        String title = homePage.getPageTitle();
        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertTrue(title.contains("MagicBricks") || title.contains("Real Estate"),
                "Page title should contain 'MagicBricks' or 'Real Estate'. Actual title: " + title);
    }

    /**
     * TC_HP_002: Verify Header Elements Visibility
     * Type: Positive | Priority: P0 | Groups: homepage, smoke
     */
    @Test(priority = 2, groups = {"homepage", "smoke"}, description = "TC_HP_002: Verify header elements (logo, login, post property) are visible")
    public void verifyHeaderElementsVisibility() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(homePage.isLogoDisplayed(), "MagicBricks Logo should be visible in the header");
        softAssert.assertTrue(homePage.isLoginButtonDisplayed(), "Login button should be visible in the header");
        softAssert.assertTrue(homePage.isPostPropertyDisplayed(), "Post Property link should be visible in the header");

        String cityText = homePage.getCitySelectorText();
        softAssert.assertNotNull(cityText, "City selector text should not be null");
        softAssert.assertFalse(cityText.trim().isEmpty(), "City selector text should not be empty");

        softAssert.assertAll();
    }

    /**
     * TC_HP_003: Verify Search Tabs Presence and Default State
     * Type: Positive | Priority: P0 | Groups: homepage, regression
     */
    @Test(priority = 3, groups = {"homepage", "regression"}, description = "TC_HP_003: Verify search tabs presence and Buy is default active")
    public void verifySearchTabsPresenceAndDefaultState() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(homePage.isSearchBuyTabActive(), "Buy tab should be active by default");
        softAssert.assertTrue(homePage.isSearchPgTabDisplayed(), "PG tab should be displayed");
        softAssert.assertTrue(homePage.isSearchPlotTabDisplayed(), "Plot tab should be displayed");
        softAssert.assertTrue(homePage.isSearchCommercialTabDisplayed(), "Commercial tab should be displayed");

        softAssert.assertAll();
    }

    /**
     * TC_HP_004: Verify Search Autocomplete Triggers on Input (Data-Driven via Excel)
     * Type: Positive | Priority: P1 | Groups: homepage, regression
     *
     * @param locality Locality name read from Excel SearchData sheet
     * @param expectedKeyword Expected keyword identifier
     */
    @Test(priority = 4, groups = {"homepage", "regression"},
            dataProvider = "searchLocalitiesData", dataProviderClass = DataProviders.class,
            description = "TC_HP_004: Verify autocomplete suggestions appear when typing in search")
    public void verifySearchAutocompleteTriggers(String locality, String expectedKeyword) {
        homePage.clickSearchInput();
        homePage.enterSearchText(locality);

        boolean isSuggestionsVisible = homePage.isSuggestionsDropdownDisplayed();
        Assert.assertTrue(isSuggestionsVisible,
                "Suggestions dropdown should be displayed after typing locality: '" + locality + "'");
    }

    /**
     * TC_HP_005: Verify Tab Switching Between Buy and Rent
     * Type: Positive | Priority: P1 | Groups: homepage, regression
     */
    @Test(priority = 5, groups = {"homepage", "regression"}, description = "TC_HP_005: Verify Buy/Rent tab switching toggles active state")
    public void verifyTabSwitchingBuyRent() {
        Assert.assertTrue(homePage.isSearchBuyTabActive(), "Buy tab should be active initially");

        // Switch to Rent
        homePage.clickRentTab();
        Assert.assertTrue(homePage.isSearchRentTabActive(), "Rent tab should be active after clicking Rent");
        Assert.assertFalse(homePage.isSearchBuyTabActive(), "Buy tab should not be active after clicking Rent");

        // Switch back to Buy
        homePage.clickBuyTab();
        Assert.assertTrue(homePage.isSearchBuyTabActive(), "Buy tab should be active again after clicking Buy");
        Assert.assertFalse(homePage.isSearchRentTabActive(), "Rent tab should not be active after clicking Buy");
    }
}
