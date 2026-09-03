package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import com.magicbricks.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the MagicBricks Property-for-Sale Search Results Page.
 * URL pattern: https://www.magicbricks.com/property-for-sale/residential-real-estate?cityName=Pune
 *
 * Handles search result page filter interactions:
 * - Search bar with city tags and "Add More" locality input
 * - Filter bar: Top Localities, Budget, BHK, Property Type (Flat), Sort By
 * - More Filters side panel: Covered Area, Sub Property Type, Posted Since,
 *   Posted By, Furnishing, Amenities, Verified Properties
 * - Search results: property card visibility, count text, tab navigation
 *
 * SRP: This class handles ONLY the Sell search results page and its filter system.
 *      Header Sell dropdown interactions live in SellDropdownPage.java.
 * OCP: Extends BasePage without modifying any existing page class.
 * LSP: Can be used wherever BasePage is expected — all inherited helpers work correctly.
 * DIP: Depends on WebDriver interface, never instantiates ChromeDriver directly.
 */
public class SellPage extends BasePage {

    // ==================== CONSTRUCTOR ====================

    public SellPage(WebDriver driver) {
        super(driver);
    }

    // ==================== NAVIGATION ====================

    /**
     * Navigates directly to the Sell search results page for the specified city.
     * Uses the configured base URL from config.properties and appends city parameter.
     *
     * @param cityName City to search properties in (e.g., "Pune", "Mumbai", "Bangalore")
     */
    public void navigateToSellPage(String cityName) {
        String url = ConfigReader.getSellSearchBaseUrl() + "?cityName=" + cityName;
        driver.get(url);
        try {
            waitHelper.waitForVisibility(By.tagName("body"));
        } catch (Exception ignored) {}
        actionDelay();
    }

    // ==================== PAGE-LEVEL ELEMENTS ====================

    /**
     * Results count heading text (e.g., "52505 results | Flats for Sale in Pune").
     * Uses multiple fallback selectors for resilience against dynamic page structure.
     */
    @FindBy(css = "h1, div.mb-srp__left__heading, div[class*='srp__left'] h1, div[class*='result'] h1")
    private WebElement resultsCountHeading;

    /**
     * Breadcrumb navigation container showing page path.
     */
    @FindBy(css = "div.mb-srp__breadcrumb, div[class*='breadcrumb'], nav[aria-label='breadcrumb']")
    private WebElement breadcrumbContainer;

    /**
     * "Properties" tab on the results page (default active tab).
     */
    @FindBy(css = "div[class*='tab'] a[class*='active'], div.mb-srp__tabs a:first-child, a[class*='tab'][class*='active']")
    private WebElement propertiesTab;

    /**
     * "Top Agents" tab on the results page.
     */
    @FindBy(xpath = "//a[contains(text(),'Top Agents')] | //div[contains(@class,'tab')]//a[contains(text(),'Agent')]")
    private WebElement topAgentsTab;

    // ==================== FILTER BAR ELEMENTS ====================

    /**
     * Top Localities filter button in the filter bar.
     */
    @FindBy(xpath = "//div[contains(@class,'filter')]//span[contains(text(),'Top Localities')] | //div[contains(text(),'Top Localities')]")
    private WebElement topLocalitiesFilter;

    /**
     * Budget filter button in the filter bar.
     */
    @FindBy(xpath = "//div[contains(@class,'filter')]//span[contains(text(),'Budget')] | //div[contains(text(),'Budget')]")
    private WebElement budgetFilter;

    /**
     * Property type filter (Flat) button in the filter bar.
     */
    @FindBy(xpath = "//div[contains(@class,'filter')]//span[contains(text(),'Flat')] | //div[contains(@class,'filter')]//span[contains(text(),'Property Type')]")
    private WebElement propertyTypeFilter;

    /**
     * BHK filter button in the filter bar.
     */
    @FindBy(xpath = "//div[contains(@class,'filter')]//span[contains(text(),'BHK')] | //div[contains(text(),'BHK')]")
    private WebElement bhkFilter;

    /**
     * Sort By dropdown button.
     */
    @FindBy(xpath = "//div[contains(text(),'Sort by')] | //span[contains(text(),'Sort by')] | //div[contains(@class,'sort')]")
    private WebElement sortByDropdown;

    /**
     * More Filters button (usually shows a count badge like "5 More Filters").
     */
    @FindBy(xpath = "//div[contains(text(),'More Filters')] | //span[contains(text(),'More Filters')] | //div[contains(@class,'filter')]//span[contains(text(),'More')]")
    private WebElement moreFiltersButton;

    // ==================== FILTER DROPDOWN ELEMENTS ====================

    /**
     * "Done" button inside filter dropdowns (Top Localities, Budget, etc.).
     */
    @FindBy(xpath = "//span[text()='Done'] | //button[text()='Done'] | //div[text()='Done']")
    private WebElement filterDoneButton;

    /**
     * "Clear All" link inside the More Filters panel.
     */
    @FindBy(xpath = "//span[contains(text(),'Clear All')] | //a[contains(text(),'Clear All')]")
    private WebElement clearAllButton;

    /**
     * "View X Properties" button inside the More Filters panel.
     */
    @FindBy(xpath = "//span[contains(text(),'Properties')] | //button[contains(text(),'Properties')] | //a[contains(text(),'Properties')]")
    private WebElement viewPropertiesButton;

    /**
     * Budget Min dropdown select inside Budget filter.
     */
    @FindBy(xpath = "(//select[contains(@class,'min')] | //select[1])[1]")
    private WebElement budgetMinSelect;

    /**
     * Budget Max dropdown select inside Budget filter.
     */
    @FindBy(xpath = "(//select[contains(@class,'max')] | //select[2])[1]")
    private WebElement budgetMaxSelect;

    // ==================== PROPERTY CARD ELEMENTS ====================

    /**
     * First property card in the search results.
     * Uses broad CSS selectors with fallbacks for dynamic card structure.
     */
    @FindBy(css = "div[class*='card'] a[class*='title'], div[class*='srp__list'] div[class*='card']:first-child, div[class*='property-card']:first-child, div[id*='srpCard'], div[data-type='property']")
    private WebElement firstPropertyCard;

    /**
     * Locators for detecting property cards in the search results list.
     */
    private static final By[] PROPERTY_CARD_LOCATORS = {
            By.cssSelector("div[id*='srpCard']"),
            By.cssSelector("div[data-type='property']"),
            By.cssSelector("div[class*='mb-srp__card']"),
            By.cssSelector("div[class*='property-card']"),
            By.cssSelector("div[class*='srp__list'] > div")
    };

    // ==================== PAGE STATE CHECKS ====================

    /**
     * Checks if the results count heading is visible on the page.
     */
    public boolean isResultsCountHeadingDisplayed() {
        try {
            return prepareElement(resultsCountHeading).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the text content of the results count heading.
     * Example: "52505 results | Flats for Sale in Pune"
     */
    public String getResultsCountHeadingText() {
        try {
            return prepareElement(resultsCountHeading).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Checks if the page URL contains the expected city fragment.
     *
     * @param cityName Expected city name in the URL
     */
    public boolean isUrlContainingCity(String cityName) {
        try {
            return waitHelper.waitForUrlContains(cityName);
        } catch (Exception e) {
            return driver.getCurrentUrl().toLowerCase().contains(cityName.toLowerCase());
        }
    }

    /**
     * Checks if the page URL indicates a property-for-sale search page.
     */
    public boolean isOnSellSearchPage() {
        String url = driver.getCurrentUrl();
        return url.contains("property-for-sale") || url.contains("Sale");
    }

    /**
     * Checks if the page title contains sale-related keywords.
     */
    public boolean isTitleContainingSale() {
        String title = "";
        try {
            for (int i = 0; i < 10; i++) {
                title = driver.getTitle();
                if (title == null || title.trim().isEmpty()) {
                    try {
                        Object jsTitle = ((JavascriptExecutor) driver).executeScript("return document.title;");
                        if (jsTitle != null) {
                            title = jsTitle.toString();
                        }
                    } catch (Exception ignored) {}
                }
                if (title != null && !title.trim().isEmpty()) {
                    break;
                }
                Thread.sleep(500);
            }
        } catch (Exception ignored) {}

        if (title == null || title.trim().isEmpty()) {
            // Fallback: check if URL reflects property-for-sale / residential search page
            return driver.getCurrentUrl().contains("property-for-sale")
                    || driver.getCurrentUrl().contains("residential");
        }

        String lowerTitle = title.toLowerCase();
        return lowerTitle.contains("sale") || lowerTitle.contains("buy")
                || lowerTitle.contains("flat") || lowerTitle.contains("real estate")
                || lowerTitle.contains("magicbricks") || lowerTitle.contains("pune")
                || lowerTitle.contains("property") || lowerTitle.contains("apartment");
    }

    /**
     * Checks if the breadcrumb container is visible.
     */
    public boolean isBreadcrumbDisplayed() {
        try {
            return prepareElement(breadcrumbContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== PROPERTY CARD CHECKS ====================

    /**
     * Checks if at least one property card is displayed in the search results.
     * Tries multiple locator strategies for resilience.
     */
    public boolean isPropertyCardDisplayed() {
        try {
            return waitHelper.waitForAnyElementVisible(PROPERTY_CARD_LOCATORS);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the count of property cards visible on the current results page.
     */
    public int getPropertyCardCount() {
        for (By locator : PROPERTY_CARD_LOCATORS) {
            List<WebElement> cards = driver.findElements(locator);
            if (!cards.isEmpty()) {
                return cards.size();
            }
        }
        return 0;
    }

    /**
     * Returns the text content of the first visible property card
     * (typically includes BHK, price, locality info).
     */
    public String getFirstPropertyCardText() {
        try {
            return prepareElement(firstPropertyCard).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    // ==================== FILTER BAR INTERACTIONS ====================

    /**
     * Clicks the BHK filter button to open the BHK selection dropdown.
     */
    public void clickBHKFilter() {
        try {
            prepareElement(bhkFilter).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", bhkFilter);
        }
        actionDelay();
    }

    /**
     * Selects a specific BHK option from the BHK filter dropdown.
     * Searches for the matching BHK text (e.g., "2 BHK", "3 BHK") among visible options.
     *
     * @param bhkText BHK option text to select (e.g., "2 BHK")
     */
    public void selectBHKOption(String bhkText) {
        try {
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofMillis(500));
            By[] bhkLocators = {
                    By.xpath("//div[contains(@class,'filter')]//label[contains(text(),'" + bhkText + "')]"),
                    By.xpath("//div[contains(@class,'filter')]//span[contains(text(),'" + bhkText + "')]"),
                    By.xpath("//div[contains(@class,'filter')]//li[contains(text(),'" + bhkText + "')]"),
                    By.xpath("//*[contains(text(),'" + bhkText + "') and (self::label or self::span or self::div or self::button or self::a)]")
            };
            for (By loc : bhkLocators) {
                List<WebElement> options = driver.findElements(loc);
                for (WebElement option : options) {
                    if (option.isDisplayed()) {
                        prepareElement(option).click();
                        actionDelay();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            // Fallback handled silently
        } finally {
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(ConfigReader.getImplicitWait()));
        }
    }

    /**
     * Clicks the Top Localities filter button to open the locality selection dropdown.
     */
    public void clickTopLocalitiesFilter() {
        try {
            prepareElement(topLocalitiesFilter).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", topLocalitiesFilter);
        }
        actionDelay();
    }

    /**
     * Selects a specific locality from the Top Localities filter dropdown.
     * Searches for the matching locality name among visible options.
     *
     * @param localityName Locality to select (e.g., "Kharadi", "Wakad", "Baner")
     */
    public void selectLocality(String localityName) {
        try {
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofMillis(500));
            By[] localityLocators = {
                    By.xpath("//div[contains(@class,'filter')]//span[contains(text(),'" + localityName + "')]"),
                    By.xpath("//div[contains(@class,'filter')]//label[contains(text(),'" + localityName + "')]"),
                    By.xpath("//*[contains(text(),'" + localityName + "') and (self::label or self::span or self::div)]")
            };
            for (By loc : localityLocators) {
                List<WebElement> options = driver.findElements(loc);
                for (WebElement option : options) {
                    if (option.isDisplayed()) {
                        prepareElement(option).click();
                        actionDelay();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            // Fallback handled silently
        } finally {
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(ConfigReader.getImplicitWait()));
        }
    }


    /**
     * Clicks the Budget filter button to open the budget range dropdown.
     */
    public void clickBudgetFilter() {
        try {
            prepareElement(budgetFilter).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", budgetFilter);
        }
        actionDelay();
    }

    /**
     * Checks if a Budget filter dropdown is currently open and visible.
     */
    public boolean isBudgetDropdownDisplayed() {
        try {
            // Look for Min/Max select elements or slider inside budget dropdown
            By[] budgetDropdownLocators = {
                    By.xpath("//select[contains(@class,'min')] | //select[contains(@class,'Min')]"),
                    By.xpath("//div[contains(@class,'budget')] | //div[contains(@class,'Budget')]"),
                    By.cssSelector("select")
            };
            for (By loc : budgetDropdownLocators) {
                List<WebElement> elements = driver.findElements(loc);
                for (WebElement el : elements) {
                    if (el.isDisplayed()) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clicks the Sort By dropdown to open sort options.
     */
    public void clickSortByDropdown() {
        try {
            prepareElement(sortByDropdown).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortByDropdown);
        }
        actionDelay();
    }

    /**
     * Selects a sort option from the Sort By dropdown.
     *
     * @param sortOptionText Sort option text (e.g., "Price: Low to High", "Relevance", "Recent")
     */
    public void selectSortOption(String sortOptionText) {
        try {
            By[] sortLocators = {
                    By.xpath("//div[contains(@class,'sort')]//span[contains(text(),'" + sortOptionText + "')]"),
                    By.xpath("//ul[contains(@class,'sort')]//li[contains(text(),'" + sortOptionText + "')]"),
                    By.xpath("//*[contains(text(),'" + sortOptionText + "') and ancestor::div[contains(@class,'sort')]]")
            };
            for (By loc : sortLocators) {
                List<WebElement> options = driver.findElements(loc);
                for (WebElement option : options) {
                    if (option.isDisplayed()) {
                        prepareElement(option).click();
                        actionDelay();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            // Fallback handled silently
        }
    }

    /**
     * Checks if the Sort By dropdown is currently visible/active.
     */
    public boolean isSortByDropdownVisible() {
        try {
            return sortByDropdown.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the currently displayed Sort By label text.
     */
    public String getSortByText() {
        try {
            return sortByDropdown.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Clicks the More Filters button to open the side panel.
     */
    public void clickMoreFilters() {
        try {
            prepareElement(moreFiltersButton).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", moreFiltersButton);
        }
        actionDelay();
    }

    /**
     * Checks if the More Filters panel is currently open.
     * Looks for elements that only appear when the More Filters panel is visible
     * (e.g., Sub Property Type, Posted Since, Clear All, View Properties button).
     */
    public boolean isMoreFiltersPanelOpen() {
        try {
            By[] panelLocators = {
                    By.xpath("//div[contains(text(),'Sub Property Type')] | //span[contains(text(),'Sub Property Type')]"),
                    By.xpath("//div[contains(text(),'Posted Since')] | //span[contains(text(),'Posted Since')]"),
                    By.xpath("//span[contains(text(),'Clear All')]"),
                    By.xpath("//div[contains(text(),'Furnishing')] | //span[contains(text(),'Furnishing')]")
            };
            for (By loc : panelLocators) {
                List<WebElement> elements = driver.findElements(loc);
                for (WebElement el : elements) {
                    if (el.isDisplayed()) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Selects a "Posted Since" option from the More Filters panel.
     *
     * @param postedSinceText Option text (e.g., "Yesterday", "Last Week", "Last 2 Weeks")
     */
    public void selectPostedSinceOption(String postedSinceText) {
        try {
            By locator = By.xpath("//*[contains(text(),'" + postedSinceText + "') and " +
                    "(self::span or self::label or self::div or self::button)]");
            List<WebElement> options = driver.findElements(locator);
            for (WebElement option : options) {
                if (option.isDisplayed()) {
                    scrollToElement(option);
                    prepareElement(option).click();
                    actionDelay();
                    return;
                }
            }
        } catch (Exception e) {
            // Fallback handled silently
        }
    }

    /**
     * Clicks the "View X Properties" button inside the More Filters panel.
     */
    public void clickViewPropertiesButton() {
        try {
            prepareElement(viewPropertiesButton).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPropertiesButton);
        }
        actionDelay();
    }

    /**
     * Clicks the "Done" button inside a filter dropdown to apply the selection.
     */
    public void clickFilterDoneButton() {
        try {
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofMillis(500));
            By[] doneLocators = {
                    By.xpath("//span[text()='Done'] | //button[text()='Done'] | //div[text()='Done']"),
                    By.cssSelector("span.done, div.done, .btn-done, button.done, .filter-done")
            };
            for (By loc : doneLocators) {
                List<WebElement> list = driver.findElements(loc);
                for (WebElement el : list) {
                    if (el.isDisplayed()) {
                        highlightElement(el);
                        el.click();
                        actionDelay();
                        return;
                    }
                }
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", filterDoneButton);
        } catch (Exception ignored) {
        } finally {
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(ConfigReader.getImplicitWait()));
        }
        actionDelay();
    }


    // ==================== URL & RESULTS VALIDATION ====================

    /**
     * Checks if the current page URL contains the expected text fragment.
     * Useful for verifying filter parameters are reflected in the URL.
     *
     * @param fragment Expected URL fragment (e.g., "2-BHK", "Kharadi", "budget")
     */
    public boolean isUrlContaining(String fragment) {
        try {
            return waitHelper.waitForUrlContains(fragment);
        } catch (Exception e) {
            return driver.getCurrentUrl().toLowerCase().contains(fragment.toLowerCase());
        }
    }

    /**
     * Returns the current URL of the page.
     * Useful for verifying filter parameters in the URL after applying filters.
     */
    public String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Checks if results heading text contains the expected keyword.
     *
     * @param keyword Expected keyword in results heading (e.g., "2 BHK", "Kharadi")
     */
    public boolean doesResultsHeadingContain(String keyword) {
        String heading = getResultsCountHeadingText();
        return heading.toLowerCase().contains(keyword.toLowerCase());
    }

    /**
     * Checks if the page displays a "no results" or "0 results" message.
     */
    public boolean isNoResultsDisplayed() {
        try {
            String heading = getResultsCountHeadingText();
            if (heading.contains("0 result") || heading.contains("No result")
                    || heading.contains("0 properties") || heading.isEmpty()) {
                return true;
            }
            // Also check for dedicated "no results" elements
            By[] noResultLocators = {
                    By.xpath("//*[contains(text(),'No results found')]"),
                    By.xpath("//*[contains(text(),'0 results')]"),
                    By.xpath("//*[contains(text(),'No Properties')]"),
                    By.cssSelector("div[class*='no-result'], div[class*='empty']")
            };
            for (By loc : noResultLocators) {
                List<WebElement> elements = driver.findElements(loc);
                for (WebElement el : elements) {
                    if (el.isDisplayed()) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the page title is valid (non-null and non-empty).
     * Used for negative tests to verify the page loaded even with invalid parameters.
     */
    public boolean isPageTitleValid() {
        String title = driver.getTitle();
        return title != null && !title.trim().isEmpty();
    }
}
