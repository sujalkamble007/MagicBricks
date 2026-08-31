package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import com.magicbricks.utils.OtpHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the MagicBricks Login Page.
 * URL: https://accounts.magicbricks.com/userauth/login
 *
 * This page opens in a NEW TAB when clicking "Login/Sign Up" from the header.
 * The test must switch to the new window handle before interacting with this page.
 *
 * Locators primarily use ID since the login form has well-defined id attributes.
 */
public class LoginPage extends BasePage {

    // ==================== LOGIN FORM ELEMENTS ====================

    @FindBy(id = "firstLoginDiv")
    private WebElement loginContainer;

    @FindBy(css = "div.login-heading")
    private WebElement loginHeading;

    @FindBy(id = "loginTypeBuyer")
    private WebElement buyerOwnerRadio;

    @FindBy(id = "loginTypeAgent/Builder")
    private WebElement agentBuilderRadio;

    @FindBy(id = "emailOrMobile")
    private WebElement emailOrMobileInput;

    @FindBy(css = "label[for='emailOrMobile']")
    private WebElement emailOrMobileLabel;

    // TODO: verify against live DOM — continue/submit button after entering mobile
    @FindBy(css = "div.m-login__fieldset button")
    private WebElement continueButton;

    // TODO: verify against live DOM — OTP input field (appears after clicking Continue)
    @FindBy(id = "otpInput")
    private WebElement otpInputField;

    // TODO: verify against live DOM — error message for invalid input
    @FindBy(css = "div.m-login__error")
    private WebElement errorMessage;

    @FindBy(id = "login-loader")
    private WebElement loginLoader;

    // ==================== INTERACTION METHODS (stubs — logic in Day 2+) ====================

    public boolean isLoginPageLoaded() {
        try {
            return waitForElementVisible(loginContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoginHeadingText() {
        return prepareElement(loginHeading).getText().trim();
    }

    public void selectBuyerOwner() {
        prepareElement(buyerOwnerRadio).click();
        actionDelay();
    }

    public void selectAgentBuilder() {
        prepareElement(agentBuilderRadio).click();
        actionDelay();
    }

    public void enterMobileNumber(String mobileNumber) {
        WebElement input = prepareElement(emailOrMobileInput);
        input.clear();
        input.sendKeys(mobileNumber);
        actionDelay();
    }

    public void clickContinueButton() {
        prepareElement(continueButton).click();
        actionDelay();
    }

    public boolean isOtpFieldDisplayed() {
        try {
            return waitForElementVisible(otpInputField).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for the tester to manually enter the OTP from their phone.
     * Pauses execution, prompts via console, then enters the OTP and submits.
     */
    public void waitForOtpAndEnter() {
        String otp = OtpHelper.waitForOtpInput();
        WebElement otpField = prepareElement(otpInputField);
        otpField.clear();
        otpField.sendKeys(otp);
        actionDelay();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            return errorMessage.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getEmailOrMobileLabelText() {
        return prepareElement(emailOrMobileLabel).getText().trim();
    }

    // ==================== CONSTRUCTOR ====================

    public LoginPage(WebDriver driver) {
        super(driver);
    }
}
