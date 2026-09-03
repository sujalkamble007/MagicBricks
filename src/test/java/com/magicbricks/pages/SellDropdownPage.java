package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the Sell dropdown navigation menu in the MagicBricks homepage header.
 *
 * Handles the header "Sell" tab hover interaction and verification of dropdown sections:
 * - "For Owner" section (Post Property, My Dashboard, Sell/Rent Ad Packages)
 * - "For Agent & Builder" section (My Dashboard, Developer Lounge, Sales Enquiry, Ad Packages)
 * - "Selling Tools" section (Property Valuation, Find an Agent, Rates & Trends, PropWorth, Digipin)
 *
 * SRP: This class handles ONLY the Sell dropdown menu locators and actions.
 *      Search result filter interactions live in SellPage.java.
 * OCP: Extends BasePage without modifying any existing page class.
 * LSP: Can be used wherever BasePage is expected — all inherited helpers work correctly.
 * DIP: Depends on WebDriver interface, never instantiates ChromeDriver directly.
 */
public class SellDropdownPage extends BasePage {

    // ==================== CONSTRUCTOR ====================

    public SellDropdownPage(WebDriver driver) {
        super(driver);
    }

    // ==================== SELL TAB & DROPDOWN CONTAINER ====================

    /**
     * Sell tab link in the header sub-navigation bar.
     * Located via link text within the header navigation tabs.
     */
    @FindBy(xpath = "//a[contains(@class,'mb-header__sub__tabs__link') and text()='Sell']")
    private WebElement sellTabLink;

    /**
     * Sell dropdown container that appears on hover/click of the Sell tab.
     */
    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div[contains(@class,'mb-header__sub__tabs__dropdown')]")
    private WebElement sellDropdownContainer;

    // ==================== "FOR OWNER" SECTION ====================

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//div[@class='drop-heading' and text()='For Owner']")
    private WebElement forOwnerHeading;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//a[contains(text(),'Post Property')]")
    private WebElement postPropertyLink;

    // ==================== "FOR AGENT & BUILDER" SECTION ====================

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//div[@class='drop-heading' and contains(text(),'Agent')]")
    private WebElement forAgentBuilderHeading;

    // ==================== "SELLING TOOLS" SECTION ====================

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//div[@class='drop-heading' and text()='Selling Tools']")
    private WebElement sellingToolsHeading;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//a[contains(text(),'Property Valuation')]")
    private WebElement propertyValuationLink;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//a[contains(text(),'Find an Agent')]")
    private WebElement findAnAgentLink;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//a[contains(text(),'Rates')]")
    private WebElement ratesAndTrendsLink;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//a[contains(text(),'PropWorth')]")
    private WebElement propWorthLink;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//a[contains(text(),'Digipin')]")
    private WebElement digipinLink;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//a[contains(text(),'Ad Packages')]")
    private WebElement adPackagesLink;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//a[contains(text(),'Developer Lounge')]")
    private WebElement developerLoungeLink;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//a[contains(text(),'Sales Enquiry')]")
    private WebElement salesEnquiryLink;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//div[contains(@class,'drop-label') and contains(text(),'+91')]")
    private WebElement ownerHelplineLabel;

    @FindBy(xpath = "//a[text()='Sell']/following-sibling::div//div[contains(@class,'drop-label') and contains(text(),'0120')]")
    private WebElement agentHelplineLabel;

    // ==================== DROPDOWN ACTIONS ====================

    /**
     * Hovers over the Sell tab link to trigger the dropdown menu visibility.
     * Uses Actions class for reliable hover interaction.
     */
    public void hoverOnSellTab() {
        try {
            prepareElement(sellTabLink);
            new Actions(driver).moveToElement(sellTabLink).perform();
            actionDelay();
        } catch (Exception e) {
            // Fallback: trigger hover via JavaScript mouseover event
            ((JavascriptExecutor) driver).executeScript(
                    "var evt = new MouseEvent('mouseover', {bubbles: true}); arguments[0].dispatchEvent(evt);",
                    sellTabLink);
            actionDelay();
        }
    }

    /**
     * Clicks the Sell tab link (some layouts require click instead of hover).
     */
    public void clickSellTab() {
        try {
            prepareElement(sellTabLink).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sellTabLink);
        }
        actionDelay();
    }

    // ==================== DROPDOWN VISIBILITY CHECKS ====================

    /**
     * Checks if the Sell dropdown container is visible after hover/click.
     */
    public boolean isSellDropdownDisplayed() {
        try {
            if (sellDropdownContainer != null && sellDropdownContainer.isDisplayed()) {
                return true;
            }
            return waitForElementVisible(sellDropdownContainer).isDisplayed();
        } catch (Exception e) {
            hoverOnSellTab();
            try {
                return sellDropdownContainer != null && sellDropdownContainer.isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Highlights all 3 dropdown sections and key links so the tester can visually observe
     * every section in the Sell dropdown menu.
     */
    public void highlightAllDropdownSections() {
        try {
            if (forOwnerHeading != null && forOwnerHeading.isDisplayed()) {
                highlightElement(forOwnerHeading);
            }
            if (postPropertyLink != null && postPropertyLink.isDisplayed()) {
                highlightElement(postPropertyLink);
            }
            if (forAgentBuilderHeading != null && forAgentBuilderHeading.isDisplayed()) {
                highlightElement(forAgentBuilderHeading);
            }
            if (sellingToolsHeading != null && sellingToolsHeading.isDisplayed()) {
                highlightElement(sellingToolsHeading);
            }
            if (propertyValuationLink != null && propertyValuationLink.isDisplayed()) {
                highlightElement(propertyValuationLink);
            }
            if (findAnAgentLink != null && findAnAgentLink.isDisplayed()) {
                highlightElement(findAnAgentLink);
            }
        } catch (Exception ignored) {}
    }

    /**
     * Highlights all Selling Tools links so the tester sees each tool highlighted in the menu.
     */
    public void highlightSellingTools() {
        try {
            if (sellingToolsHeading != null && sellingToolsHeading.isDisplayed()) {
                highlightElement(sellingToolsHeading);
            }
            if (propertyValuationLink != null && propertyValuationLink.isDisplayed()) {
                highlightElement(propertyValuationLink);
            }
            if (ratesAndTrendsLink != null && ratesAndTrendsLink.isDisplayed()) {
                highlightElement(ratesAndTrendsLink);
            }
            if (propWorthLink != null && propWorthLink.isDisplayed()) {
                highlightElement(propWorthLink);
            }
        } catch (Exception ignored) {}
    }

    /**
     * Highlights Find an Agent link in the Selling Tools column.
     */
    public void highlightFindAnAgentLink() {
        try {
            if (findAnAgentLink != null && findAnAgentLink.isDisplayed()) {
                highlightElement(findAnAgentLink);
            }
        } catch (Exception ignored) {}
    }

    /**
     * Checks if the "For Owner" section heading is visible in the dropdown.
     */
    public boolean isForOwnerSectionVisible() {
        try {
            return forOwnerHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the "For Agent & Builder" section heading is visible in the dropdown.
     */
    public boolean isForAgentBuilderSectionVisible() {
        try {
            return forAgentBuilderHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the "Selling Tools" section heading is visible in the dropdown.
     */
    public boolean isSellingToolsSectionVisible() {
        try {
            return sellingToolsHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== LINK VISIBILITY CHECKS ====================

    /**
     * Checks if the Post Property link is visible inside the dropdown.
     */
    public boolean isPostPropertyLinkVisible() {
        try {
            return postPropertyLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the Property Valuation link is visible inside the dropdown.
     */
    public boolean isPropertyValuationLinkVisible() {
        try {
            return propertyValuationLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the Find an Agent link is visible inside the dropdown.
     */
    public boolean isFindAnAgentLinkVisible() {
        try {
            return findAnAgentLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== LINK URL RETRIEVAL ====================

    /**
     * Returns the href attribute of the Post Property link.
     */
    public String getPostPropertyLinkUrl() {
        try {
            return postPropertyLink.getAttribute("href");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns the href attribute of the Property Valuation link.
     */
    public String getPropertyValuationLinkUrl() {
        try {
            return propertyValuationLink.getAttribute("href");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns the total count of links visible inside the Sell dropdown.
     */
    public int getDropdownLinkCount() {
        try {
            List<WebElement> links = sellDropdownContainer.findElements(By.tagName("a"));
            int visibleCount = 0;
            for (WebElement link : links) {
                if (link.isDisplayed()) {
                    visibleCount++;
                }
            }
            return visibleCount;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Clicks the Post Property link inside the Sell dropdown.
     * This link opens in a new tab (target="_blank").
     */
    public void clickPostPropertyLink() {
        hoverOnSellTab();
        try {
            prepareElement(postPropertyLink).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", postPropertyLink);
        }
        actionDelay();
    }

    /**
     * Clicks the Property Valuation link inside the Sell dropdown.
     */
    public void clickPropertyValuationLink() {
        hoverOnSellTab();
        try {
            highlightElement(propertyValuationLink);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", propertyValuationLink);
        } catch (Exception e) {
            prepareElement(propertyValuationLink).click();
        }
        actionDelay();
    }

    /**
     * Clicks the Find an Agent link inside the Sell dropdown.
     */
    public void clickFindAnAgentLink() {
        hoverOnSellTab();
        try {
            highlightElement(findAnAgentLink);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", findAnAgentLink);
        } catch (Exception e) {
            prepareElement(findAnAgentLink).click();
        }
        actionDelay();
    }

    /**
     * Clicks the Rates & Trends link inside the Sell dropdown.
     */
    public void clickRatesAndTrendsLink() {
        hoverOnSellTab();
        try {
            highlightElement(ratesAndTrendsLink);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ratesAndTrendsLink);
        } catch (Exception e) {
            prepareElement(ratesAndTrendsLink).click();
        }
        actionDelay();
    }


    /**
     * Clicks the PropWorth link inside the Sell dropdown.
     */
    public void clickPropWorthLink() {
        hoverOnSellTab();
        try {
            highlightElement(propWorthLink);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", propWorthLink);
        } catch (Exception e) {
            prepareElement(propWorthLink).click();
        }
        actionDelay();
    }

    /**
     * Clicks the Sell / Rent Ad Packages link inside the Sell dropdown.
     */
    public void clickSellRentAdPackagesLink() {
        hoverOnSellTab();
        try {
            highlightElement(adPackagesLink);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", adPackagesLink);
        } catch (Exception e) {
            prepareElement(adPackagesLink).click();
        }
        actionDelay();
    }

    /**
     * Clicks the Sales Enquiry link inside the Sell dropdown.
     */
    public void clickSalesEnquiryLink() {
        hoverOnSellTab();
        try {
            highlightElement(salesEnquiryLink);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", salesEnquiryLink);
        } catch (Exception e) {
            prepareElement(salesEnquiryLink).click();
        }
        actionDelay();
    }

    /**
     * Returns the helpline text for Property Owners displayed in Sell dropdown.
     */
    public String getOwnerHelplineText() {
        try {
            highlightElement(ownerHelplineLabel);
            return ownerHelplineLabel.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns the helpline text for Agents & Builders displayed in Sell dropdown.
     */
    public String getAgentHelplineText() {
        try {
            highlightElement(agentHelplineLabel);
            return agentHelplineLabel.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Clicks the Developer Lounge link inside the Sell dropdown.
     */
    public void clickDeveloperLoungeLink() {
        hoverOnSellTab();
        try {
            highlightElement(developerLoungeLink);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", developerLoungeLink);
        } catch (Exception e) {
            prepareElement(developerLoungeLink).click();
        }
        actionDelay();
    }
}




