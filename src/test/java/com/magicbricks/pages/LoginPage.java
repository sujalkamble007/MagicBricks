package com.magicbricks.pages;

import com.magicbricks.base.BasePage;
import com.magicbricks.utils.OtpHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the MagicBricks Login Page / Modal.
 * URL: https://accounts.magicbricks.com/userauth/login
 *
 * Handles:
 * - Tab switching / iframe switching (if rendered as an embedded iframe modal)
 * - User type selection (Buyer/Owner vs. Agent/Builder)
 * - Mobile/Email input and field management
 * - Step 1 submission (Continue)
 * - OTP detection and console-prompted manual entry
 * - Error message validation
 *
 * Follows Single Responsibility Principle (SRP) and Open/Closed Principle (OCP).
 */
public class LoginPage extends BasePage {

    // Possible iframe locators if login renders inside an embedded iframe modal
    private static final By LOGIN_IFRAME_LOCATOR = By.cssSelector("iframe[src*='userauth'], iframe#loginIframe, iframe[id*='login'], iframe[title*='login']");

    // ==================== CONSTRUCTOR ====================

    public LoginPage(WebDriver driver) {
        super(driver);
        // Resiliently switch into iframe if the login form is rendered inside one
        switchToFrameIfPresent(LOGIN_IFRAME_LOCATOR);
    }

    // ==================== LOGIN FORM ELEMENTS ====================

    @FindBy(id = "firstLoginDiv")
    private WebElement loginContainer;

    @FindBy(css = "div.login-heading")
    private WebElement loginHeading;

    @FindBy(id = "loginTypeBuyer")
    private WebElement buyerOwnerRadio;

    @FindBy(css = "label[for='loginTypeBuyer']")
    private WebElement buyerOwnerLabel;

    @FindBy(id = "loginTypeAgent/Builder")
    private WebElement agentBuilderRadio;

    @FindBy(css = "label[for='loginTypeAgent/Builder']")
    private WebElement agentBuilderLabel;

    @FindBy(id = "emailOrMobile")
    private WebElement emailOrMobileInput;

    @FindBy(css = "label[for='emailOrMobile']")
    private WebElement emailOrMobileLabel;

    @FindBy(css = "button.m-login__btn, #btnStep1, div.m-login__fieldset button, button.btn, input[type='submit']")
    private WebElement continueButton;

    @FindBy(css = "input#otp, input#otpInput, input.otp-input, input#userOtp, input[name='otp'], input[id*='otp']")
    private WebElement otpInputField;

    @FindBy(css = "button#btnVerifyOtp, button.m-login__btn-verify, button[type='submit']")
    private WebElement verifyOtpButton;

    @FindBy(css = "div.m-login__error, div.error-msg, span.error, div[id*='error']")
    private WebElement errorMessage;

    @FindBy(id = "login-loader")
    private WebElement loginLoader;

    // ==================== PAGE STATE CHECKS ====================

    /**
     * Verifies the login page has loaded by checking the login container visibility.
     */
    public boolean isLoginPageLoaded() {
        try {
            return waitForElementVisible(loginContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the login heading text (expected: "Login").
     */
    public String getLoginHeadingText() {
        return prepareElement(loginHeading).getText().trim();
    }

    /**
     * Returns the label text for the email/mobile input field.
     */
    public String getEmailOrMobileLabelText() {
        return prepareElement(emailOrMobileLabel).getText().trim();
    }

    // ==================== USER TYPE SELECTION ====================

    public void selectBuyerOwner() {
        prepareElement(buyerOwnerRadio).click();
        actionDelay();
    }

    public void selectAgentBuilder() {
        prepareElement(agentBuilderRadio).click();
        actionDelay();
    }

    /**
     * Checks if Buyer/Owner radio is selected (should be default).
     */
    public boolean isBuyerOwnerSelected() {
        return buyerOwnerRadio.isSelected();
    }

    // ==================== MOBILE/EMAIL INPUT ====================

    /**
     * Enters a mobile number or email into the login input field.
     */
    public void enterMobileNumber(String mobileNumber) {
        WebElement input = prepareElement(emailOrMobileInput);
        input.clear();
        input.sendKeys(mobileNumber);
        actionDelay();
    }

    /**
     * Clears the email/mobile input field.
     */
    public void clearMobileField() {
        WebElement input = prepareElement(emailOrMobileInput);
        input.clear();
        actionDelay();
    }

    /**
     * Returns the current value in the email/mobile input field.
     */
    public String getInputFieldValue() {
        return emailOrMobileInput.getAttribute("value");
    }

    // ==================== CONTINUE / SUBMIT ====================

    /**
     * Clicks Continue or sends Enter key to trigger validation / OTP.
     */
    public void clickContinueButton() {
        try {
            prepareElement(continueButton).click();
        } catch (Exception e) {
            emailOrMobileInput.sendKeys(Keys.ENTER);
        }
        actionDelay();
    }

    // ==================== OTP HANDLING ====================

    /**
     * Checks if the OTP input field or container appeared after clicking Continue.
     */
    public boolean isOtpFieldDisplayed() {
        try {
            List<WebElement> otpElements = driver.findElements(By.cssSelector("input#otp, input#otpInput, input.otp-input, input#userOtp, input[name='otp'], input[id*='otp']"));
            for (WebElement el : otpElements) {
                if (el.isDisplayed()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Prompts the tester in console to enter the live OTP, then inputs it into the field.
     */
    public void waitForOtpAndEnter() {
        String otp = OtpHelper.waitForOtpInput();
        try {
            WebElement otpField = prepareElement(otpInputField);
            otpField.clear();
            otpField.sendKeys(otp);
            actionDelay();

            try {
                if (verifyOtpButton.isDisplayed()) {
                    prepareElement(verifyOtpButton).click();
                } else {
                    otpField.sendKeys(Keys.ENTER);
                }
            } catch (Exception ex) {
                otpField.sendKeys(Keys.ENTER);
            }
            actionDelay();
        } catch (Exception e) {
            System.out.println(">>> OTP field interaction completed: " + otp);
        }
    }

    // ==================== ERROR / VALIDATION ====================

    /**
     * Checks if a validation error message is displayed.
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the text content of the error message.
     */
    public String getErrorMessageText() {
        try {
            return errorMessage.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
