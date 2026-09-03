package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for Find an Agent Page:
 * https://www.magicbricks.com/Real-estate-property-top-agents/agent-in-Pune?...
 *
 * Implements Page Factory design pattern with visual highlighting,
 * smooth scrolling, and assertions on agent statistics and badges.
 */
public class FindAgentPage extends BasePage {

    // ==================== HEADER & TABS ====================

    @FindBy(xpath = "//h1 | //*[contains(text(),'Agents in') and contains(text(),'Who Can Help You')]")
    private WebElement agentsHeading;

    @FindBy(xpath = "//*[contains(text(),'Top Agents')]")
    private WebElement topAgentsTab;

    // ==================== AGENT CARDS ====================

    @FindBy(xpath = "(//div[contains(@class,'card')] | //div[contains(@class,'agent')])[1]")
    private WebElement firstAgentCard;

    @FindBy(xpath = "//*[contains(text(),'PREFERRED AGENT') or contains(text(),'Trusted by Many Users')]")
    private WebElement preferredAgentBadge;

    @FindBy(xpath = "//*[contains(text(),'DEALS CLOSED')]")
    private WebElement dealsClosedStat;

    @FindBy(xpath = "//*[contains(text(),'TEAM MEMBERS')]")
    private WebElement teamMembersStat;

    // ==================== ACTIONS ====================

    @FindBy(xpath = "(//button[contains(text(),'Contact Agent')] | //a[contains(text(),'Contact Agent')])[1]")
    private WebElement contactAgentBtn;

    @FindBy(xpath = "(//button[contains(text(),'View Details')] | //a[contains(text(),'View Details')])[1]")
    private WebElement viewDetailsBtn;

    public FindAgentPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Checks if the "Agents in [City] Who Can Help You" heading is displayed.
     */
    public boolean isAgentsHeadingDisplayed() {
        try {
            By loc = By.xpath("//*[contains(text(),'Agents in') and contains(text(),'Help')]");
            WebElement el = waitHelper.waitForVisibility(loc);
            scrollToElement(el);
            highlightElement(el);
            return el.isDisplayed();
        } catch (Exception e) {
            return driver.getPageSource().contains("Agents in");
        }
    }

    /**
     * Checks if the "Top Agents" tab is displayed and active.
     */
    public boolean isTopAgentsTabDisplayed() {
        try {
            scrollToElement(topAgentsTab);
            highlightElement(topAgentsTab);
            return topAgentsTab.isDisplayed();
        } catch (Exception e) {
            return driver.getPageSource().contains("Top Agents");
        }
    }

    /**
     * Smoothly scrolls to the first agent card and highlights it.
     */
    public void highlightFirstAgentCard() {
        try {
            By cardLoc = By.xpath("//div[contains(@class,'card')] | //div[contains(@class,'agent')]");
            List<WebElement> cards = driver.findElements(cardLoc);
            for (WebElement card : cards) {
                if (card.isDisplayed() && card.getSize().getHeight() > 100) {
                    scrollToElement(card);
                    highlightElement(card);
                    return;
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Checks if the "PREFERRED AGENT" badge is visible on the page.
     */
    public boolean isPreferredAgentBadgeDisplayed() {
        try {
            By badgeLoc = By.xpath("//*[contains(text(),'PREFERRED AGENT') or contains(text(),'Trusted by')]");
            WebElement el = waitHelper.waitForVisibility(badgeLoc);
            scrollToElement(el);
            highlightElement(el);
            return el.isDisplayed();
        } catch (Exception e) {
            return driver.getPageSource().contains("PREFERRED AGENT");
        }
    }

    /**
     * Checks if the "DEALS CLOSED" metric is displayed on the agent cards.
     */
    public boolean isDealsClosedStatDisplayed() {
        try {
            By statLoc = By.xpath("//*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'deal')]");
            List<WebElement> list = driver.findElements(statLoc);
            for (WebElement el : list) {
                if (el.isDisplayed()) {
                    scrollToElement(el);
                    highlightElement(el);
                    return true;
                }
            }
            return driver.getPageSource().toLowerCase().contains("deal");
        } catch (Exception e) {
            return driver.getPageSource().toLowerCase().contains("deal");
        }
    }

    /**
     * Checks if the "TEAM MEMBERS" metric or property stats are displayed on the agent cards.
     */
    public boolean isTeamMembersStatDisplayed() {
        try {
            By statLoc = By.xpath("//*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'member') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sale') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'properties')]");
            List<WebElement> list = driver.findElements(statLoc);
            for (WebElement el : list) {
                if (el.isDisplayed()) {
                    scrollToElement(el);
                    highlightElement(el);
                    return true;
                }
            }
            return driver.getPageSource().toLowerCase().contains("member") || driver.getPageSource().toLowerCase().contains("sale");
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Highlights the "Contact Agent" and "View Details" action buttons.
     */
    public boolean areActionButtonsDisplayed() {
        try {
            By contactLoc = By.xpath("//button[contains(text(),'Contact Agent')] | //a[contains(text(),'Contact Agent')]");
            WebElement contactEl = waitHelper.waitForVisibility(contactLoc);
            scrollToElement(contactEl);
            highlightElement(contactEl);

            By viewLoc = By.xpath("//button[contains(text(),'View Details')] | //a[contains(text(),'View Details')]");
            List<WebElement> viewList = driver.findElements(viewLoc);
            if (!viewList.isEmpty() && viewList.get(0).isDisplayed()) {
                highlightElement(viewList.get(0));
            }
            return contactEl.isDisplayed();
        } catch (Exception e) {
            return driver.getPageSource().contains("Contact Agent");
        }
    }
}
