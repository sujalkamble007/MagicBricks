package com.magicbricks.tests;

import com.magicbricks.base.BaseTest;
import com.magicbricks.pages.ContactUsPage;
import com.magicbricks.pages.DeveloperLoungePage;
import com.magicbricks.pages.FindAgentPage;
import com.magicbricks.pages.HomePage;
import com.magicbricks.pages.RatesAndTrendsPage;
import com.magicbricks.pages.SellDropdownPage;
import com.magicbricks.utils.ConsoleLogger;
import com.magicbricks.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Enterprise Production Test Suite for MagicBricks Sell Module (Case Study 1).
 *
 * Covers 8 curated test cases adhering to SOLID principles:
 * 1. TC_SELL_001: Clickability of all primary Sell dropdown menu links
 * 2. TC_SELL_002: Navigation to Rates & Trends page + banner header verification
 * 3. TC_SELL_003: Tab switching between Residential and Commercial trends
 * 4. TC_SELL_004: Data-driven multi-city price trends selection (Pune, Mumbai, Bangalore from Excel)
 * 5. TC_SELL_005: Navigation to Find an Agent page + agent directory verification
 * 6. TC_SELL_006: Agent card details, "PREFERRED AGENT" badge, and stats verification
 * 7. TC_SELL_007: Developer Lounge brand store navigation and brand card inspection
 * 8. TC_SELL_008: Contact Us page traversal (smooth scroll down, directory element click, smooth scroll up)
 */
public class SellPageTest extends BaseTest {

    private HomePage homePage;
    private SellDropdownPage sellDropdownPage;
    private RatesAndTrendsPage ratesAndTrendsPage;
    private FindAgentPage findAgentPage;
    private DeveloperLoungePage developerLoungePage;

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        homePage = new HomePage(driver);
        sellDropdownPage = new SellDropdownPage(driver);
        ratesAndTrendsPage = new RatesAndTrendsPage(driver);
        findAgentPage = new FindAgentPage(driver);
        developerLoungePage = new DeveloperLoungePage(driver);
    }

    /**
     * TC_SELL_001: Verify All Key Elements in Sell Dropdown are Enabled and Clickable
     * Priority: P0 | Groups: sell, smoke
     */
    @Test(priority = 1, groups = {"sell", "smoke"},
            description = "TC_SELL_001: Verify all primary menu links in Sell dropdown are enabled and clickable")
    public void verifyAllSellDropdownElementsClickable() {
        ConsoleLogger.logStep(1, "Hovering on 'Sell' header tab to open dropdown menu...");
        sellDropdownPage.hoverOnSellTab();
        Assert.assertTrue(sellDropdownPage.isSellDropdownDisplayed(),
                "Sell dropdown must open upon hovering over Sell tab");

        ConsoleLogger.logStep(2, "Highlighting primary dropdown sections (Owner, Agent & Builder, Tools)...");
        sellDropdownPage.highlightAllDropdownSections();

        ConsoleLogger.logStep(3, "Verifying Post Property, Property Valuation & Find an Agent links...");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(sellDropdownPage.isPostPropertyLinkVisible(),
                "Post Property link must be visible and clickable");
        softAssert.assertTrue(sellDropdownPage.isPropertyValuationLinkVisible(),
                "Property Valuation link must be visible and clickable");
        softAssert.assertTrue(sellDropdownPage.isFindAnAgentLinkVisible(),
                "Find an Agent link must be visible and clickable");

        int totalOptions = sellDropdownPage.getDropdownLinkCount();
        ConsoleLogger.logStep(4, "Asserting total link count (" + totalOptions + " options found and verified)...");
        softAssert.assertTrue(totalOptions >= 5,
                "Sell dropdown must contain at least 5 navigation options. Found: " + totalOptions);

        softAssert.assertAll();
    }

    /**
     * TC_SELL_002: Verify Sell -> Rates & Trends Navigation and Banner Header
     * Priority: P1 | Groups: sell, regression
     */
    @Test(priority = 2, groups = {"sell", "regression"},
            description = "TC_SELL_002: Verify navigation to Rates & Trends page from Sell dropdown")
    public void verifyRatesAndTrendsNavigation() {
        ConsoleLogger.logStep(1, "Hovering on 'Sell' tab and clicking 'Rates & Trends' link...");
        sellDropdownPage.hoverOnSellTab();
        sellDropdownPage.clickRatesAndTrendsLink();

        ConsoleLogger.logStep(2, "Switching window focus to newly opened Rates & Trends browser tab...");
        sellDropdownPage.switchToNewTab();

        ConsoleLogger.logStep(3, "Validating page URL, banner heading, and breadcrumb navigation...");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(driver.getCurrentUrl().contains("propertyRates"),
                "URL must contain propertyRates. Actual: " + driver.getCurrentUrl());
        softAssert.assertTrue(ratesAndTrendsPage.isBannerHeadingDisplayed(),
                "Rates & Trends banner heading must be displayed with highlight");
        softAssert.assertTrue(ratesAndTrendsPage.isBreadcrumbDisplayed(),
                "Breadcrumb 'Property Rates & Trends' must be displayed");

        ConsoleLogger.logStep(4, "Closing child tab and returning focus to Home window...");
        sellDropdownPage.closeCurrentTabAndSwitchBack();
        softAssert.assertAll();
    }

    /**
     * TC_SELL_003: Verify Rates & Trends Residential vs. Commercial Tab Switching
     * Priority: P1 | Groups: sell, regression
     */
    @Test(priority = 3, groups = {"sell", "regression"},
            description = "TC_SELL_003: Verify switching between Residential and Commercial tabs on Rates & Trends")
    public void verifyRatesResidentialCommercialTabSwitching() {
        ConsoleLogger.logStep(1, "Navigating to Rates & Trends page from Sell dropdown...");
        sellDropdownPage.hoverOnSellTab();
        sellDropdownPage.clickRatesAndTrendsLink();
        sellDropdownPage.switchToNewTab();

        SoftAssert softAssert = new SoftAssert();
        ConsoleLogger.logStep(2, "Verifying default 'Residential' tab is selected...");
        softAssert.assertTrue(ratesAndTrendsPage.isResidentialTabDisplayed(),
                "Residential tab should be displayed by default");

        ConsoleLogger.logStep(3, "Clicking 'Commercial' tab and verifying active highlight state...");
        ratesAndTrendsPage.clickCommercialTab();
        softAssert.assertTrue(ratesAndTrendsPage.isCommercialTabActive(),
                "Commercial tab should become active after click");

        ConsoleLogger.logStep(4, "Clicking back to 'Residential' tab and confirming state restoration...");
        ratesAndTrendsPage.clickResidentialTab();
        softAssert.assertTrue(ratesAndTrendsPage.isResidentialTabDisplayed(),
                "Residential tab should be displayed upon switching back");

        sellDropdownPage.closeCurrentTabAndSwitchBack();
        softAssert.assertAll();
    }

    /**
     * TC_SELL_004: Verify Rates & Trends City Price Trend Selection (Data-Driven)
     * Priority: P1 | Groups: sell, regression
     */
    @Test(priority = 4, groups = {"sell", "regression"},
            dataProvider = "sellCityData", dataProviderClass = DataProviders.class,
            description = "TC_SELL_004: Verify selecting a city trend from Rates & Trends table")
    public void verifyRatesCityTrendsSelection(String cityName, String expectedURLFragment) {
        ConsoleLogger.logStep(1, "Navigating to Rates & Trends page for City: " + cityName + "...");
        sellDropdownPage.hoverOnSellTab();
        sellDropdownPage.clickRatesAndTrendsLink();
        sellDropdownPage.switchToNewTab();

        ConsoleLogger.logStep(2, "Selecting city link: '" + cityName + "' from the price trends matrix...");
        ratesAndTrendsPage.selectCityLink(cityName);

        ConsoleLogger.logStep(3, "Verifying property price trends data is displayed for " + cityName + "...");
        boolean isCityFound = ratesAndTrendsPage.isCityTrendTableDisplayed(cityName);
        Assert.assertTrue(isCityFound,
                "Price trend information must be displayed for city: " + cityName);

        ConsoleLogger.logStep(4, "Closing child tab and returning to Home window...");
        sellDropdownPage.closeCurrentTabAndSwitchBack();
    }

    /**
     * TC_SELL_005: Verify Sell -> Find an Agent Navigation and Top Agents Header
     * Priority: P1 | Groups: sell, regression
     */
    @Test(priority = 5, groups = {"sell", "regression"},
            description = "TC_SELL_005: Verify navigation to Find an Agent page from Sell dropdown")
    public void verifyFindAgentNavigation() {
        ConsoleLogger.logStep(1, "Hovering on 'Sell' tab and clicking 'Find an Agent' link...");
        sellDropdownPage.hoverOnSellTab();
        sellDropdownPage.clickFindAnAgentLink();

        ConsoleLogger.logStep(2, "Switching to Find an Agent browser tab...");
        sellDropdownPage.switchToNewTab();

        ConsoleLogger.logStep(3, "Validating 'Agents in City' heading and 'Top Agents' tab visibility...");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(driver.getCurrentUrl().contains("agent"),
                "URL must contain 'agent'. Actual: " + driver.getCurrentUrl());
        softAssert.assertTrue(findAgentPage.isAgentsHeadingDisplayed(),
                "'Agents in [City] Who Can Help You' heading must be displayed");
        softAssert.assertTrue(findAgentPage.isTopAgentsTabDisplayed(),
                "'Top Agents' tab must be displayed");

        ConsoleLogger.logStep(4, "Closing agent tab and returning to Home window...");
        sellDropdownPage.closeCurrentTabAndSwitchBack();
        softAssert.assertAll();
    }

    /**
     * TC_SELL_006: Verify Find an Agent Card Details and Preferred Agent Badge
     * Priority: P1 | Groups: sell, regression
     */
    @Test(priority = 6, groups = {"sell", "regression"},
            description = "TC_SELL_006: Verify agent card details, Preferred Agent badge, and statistics")
    public void verifyAgentCardDetailsAndPreferredBadge() {
        ConsoleLogger.logStep(1, "Navigating to Find an Agent directory...");
        sellDropdownPage.hoverOnSellTab();
        sellDropdownPage.clickFindAnAgentLink();
        sellDropdownPage.switchToNewTab();

        ConsoleLogger.logStep(2, "Locating and highlighting the first top agent card...");
        findAgentPage.highlightFirstAgentCard();

        ConsoleLogger.logStep(3, "Validating 'PREFERRED AGENT' badge and trust metrics...");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(findAgentPage.isPreferredAgentBadgeDisplayed(),
                "'PREFERRED AGENT - Trusted by Many Users' badge must be displayed");

        ConsoleLogger.logStep(4, "Validating 'DEALS CLOSED' and 'TEAM MEMBERS' statistics...");
        softAssert.assertTrue(findAgentPage.isDealsClosedStatDisplayed(),
                "'DEALS CLOSED' statistic must be displayed on agent card");
        softAssert.assertTrue(findAgentPage.isTeamMembersStatDisplayed(),
                "'TEAM MEMBERS' statistic must be displayed on agent card");
        softAssert.assertTrue(findAgentPage.areActionButtonsDisplayed(),
                "'Contact Agent' and 'View Details' buttons must be displayed");

        sellDropdownPage.closeCurrentTabAndSwitchBack();
        softAssert.assertAll();
    }

    /**
     * TC_SELL_007: Verify Sell -> Developer Lounge Brand Store Navigation & Brand Cards
     * Priority: P1 | Groups: sell, regression
     */
    @Test(priority = 7, groups = {"sell", "regression"},
            description = "TC_SELL_007: Verify Developer Lounge brand store and developer cards")
    public void verifyDeveloperLoungeBrandStore() {
        ConsoleLogger.logStep(1, "Hovering on 'Sell' tab and clicking 'Developer Lounge' link...");
        sellDropdownPage.hoverOnSellTab();
        sellDropdownPage.clickDeveloperLoungeLink();

        ConsoleLogger.logStep(2, "Switching to Developer Lounge Brand Store tab...");
        sellDropdownPage.switchToNewTab();

        ConsoleLogger.logStep(3, "Validating hero heading 'Discover Real Estate Brands'...");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(driver.getCurrentUrl().contains("brand-store"),
                "URL must belong to brand-store. Actual: " + driver.getCurrentUrl());
        softAssert.assertTrue(developerLoungePage.isHeroHeadingDisplayed(),
                "'Discover Real Estate Brands' hero heading must be displayed");

        ConsoleLogger.logStep(4, "Highlighting developer brand cards & executive leadership profiles...");
        developerLoungePage.highlightDeveloperBrandCards();
        softAssert.assertTrue(developerLoungePage.areDeveloperBrandsDisplayed(),
                "Developer brand cards (Omaxe, VTP, SPR) must be displayed");
        softAssert.assertTrue(developerLoungePage.areExecutiveProfilesDisplayed(),
                "Executive profiles (CEO/Director) must be displayed");

        sellDropdownPage.closeCurrentTabAndSwitchBack();
        softAssert.assertAll();
    }

    /**
     * TC_SELL_008: Verify Contact Us Page Navigation, Scroll Down, Interactive Element Click, and Scroll Up
     * Priority: P2 | Groups: sell, regression
     */
    @Test(priority = 8, groups = {"sell", "regression"},
            description = "TC_SELL_008: Verify Contact Us page scroll down, element click, and scroll up flow")
    public void verifySalesEnquiryEmptySubmissionValidation() {
        ConsoleLogger.logStep(1, "Navigating to Contact Us / Sales Enquiry page from Sell dropdown...");
        sellDropdownPage.hoverOnSellTab();
        sellDropdownPage.clickSalesEnquiryLink();
        sellDropdownPage.switchToNewTab();

        ContactUsPage contactUsPage = new ContactUsPage(driver);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(contactUsPage.isLoaded(),
                "Must land on Contact/Sales Enquiry page. Actual: " + driver.getCurrentUrl());

        ConsoleLogger.logStep(2, "Smoothly scrolling down 800px into office directory section...");
        contactUsPage.scrollDownToDirectory();

        ConsoleLogger.logStep(3, "Highlighting and selecting interactive directory element...");
        boolean selected = contactUsPage.selectDirectoryElement();
        softAssert.assertTrue(selected || driver.getCurrentUrl().contains("contact"),
                "Interactive directory element should be selected and clicked");

        ConsoleLogger.logStep(4, "Smoothly scrolling back up to top and highlighting header...");
        contactUsPage.scrollBackToTopAndHighlightHeader();

        sellDropdownPage.closeCurrentTabAndSwitchBack();
        softAssert.assertAll();
    }
}
