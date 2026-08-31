package com.magicbricks.tests;

import com.magicbricks.base.BaseTest;
import com.magicbricks.pages.HomePage;
import com.magicbricks.pages.LoginPage;
import com.magicbricks.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * TestNG Test Class for MagicBricks Login Module.
 * Covers:
 * - TC_LG_001: Login Dropdown & CTA Display (Smoke)
 * - TC_LG_002: Valid Mobile Number Entry (9518306867) & Live OTP Trigger Flow (Regression)
 * - TC_LG_003: Invalid Mobile Number Validation & Error Handling via DataProvider (Regression)
 *
 * Handles multi-tab navigation, DataProvider parametrization, TestNG assertions,
 * and element highlighting/scrolling.
 */
public class LoginTest extends BaseTest {

    private HomePage homePage;
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        homePage = new HomePage(driver);
    }

    /**
     * TC_LG_001: Verify Login Dropdown Opens from Header
     * Type: Positive | Priority: P0 | Groups: login, smoke
     */
    @Test(priority = 1, groups = {"login", "smoke"}, description = "TC_LG_001: Verify login dropdown opens and Login/Sign Up CTA is visible")
    public void verifyLoginDropdownOpens() {
        homePage.clickLoginButton();
        boolean isCtaVisible = homePage.isLoginSignUpCtaDisplayed();
        Assert.assertTrue(isCtaVisible, "Login/Sign Up CTA should be visible inside the login dropdown");
    }

    /**
     * TC_LG_002: Verify Valid Mobile Number Entry on Login Page & Live OTP Flow
     * Type: Positive | Priority: P1 | Groups: login, regression
     *
     * @param scenario Description of test case scenario
     * @param mobileNumber Valid 10-digit mobile number from Excel (9518306867)
     * @param expectedType Expected status ("Valid")
     */
    @Test(priority = 2, groups = {"login", "regression"},
            dataProvider = "validLoginMobileData", dataProviderClass = DataProviders.class,
            description = "TC_LG_002: Verify valid 10-digit mobile number entry and OTP prompt on login page")
    public void verifyValidMobileNumberEntry(String scenario, String mobileNumber, String expectedType) {
        // Step 1 & 2: Open login dropdown and click CTA
        homePage.clickLoginButton();
        homePage.clickLoginSignUpCta();

        // Step 3: Switch to the newly opened login tab
        homePage.switchToNewTab();
        loginPage = new LoginPage(driver);

        // Step 4 & 5: Assert login page loaded and heading is correct
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(loginPage.isLoginPageLoaded(), "Login page container should be loaded in new tab");
        softAssert.assertTrue(loginPage.getLoginHeadingText().contains("Login"),
                "Login page heading should contain 'Login'. Actual: " + loginPage.getLoginHeadingText());

        // Step 6: Verify Buyer/Owner radio is selected by default
        softAssert.assertTrue(loginPage.isBuyerOwnerSelected(), "Buyer/Owner radio option should be selected by default");

        // Step 7 & 8: Enter mobile number (9518306867) and verify value attribute
        loginPage.enterMobileNumber(mobileNumber);
        String enteredValue = loginPage.getInputFieldValue();
        softAssert.assertEquals(enteredValue, mobileNumber,
                "Input field value should match the entered mobile number: " + mobileNumber);

        // Step 9: Click Continue button to trigger OTP
        loginPage.clickContinueButton();

        // Step 10: If OTP field is displayed, prompt tester via console Scanner to enter OTP
        if (loginPage.isOtpFieldDisplayed()) {
            loginPage.waitForOtpAndEnter();
        }

        softAssert.assertAll();
    }

    /**
     * TC_LG_003: Verify Invalid Mobile Number is Rejected (Data-Driven via Excel)
     * Type: Negative | Priority: P1 | Groups: login, regression
     *
     * @param scenario Description of negative scenario (e.g. short, alpha, special chars)
     * @param inputData Invalid input string from Excel
     * @param expectedError Expected error category ("Invalid")
     */
    @Test(priority = 3, groups = {"login", "regression"},
            dataProvider = "invalidLoginMobileData", dataProviderClass = DataProviders.class,
            description = "TC_LG_003: Verify invalid mobile/email input is rejected with error")
    public void verifyInvalidMobileNumberRejected(String scenario, String inputData, String expectedError) {
        // Open login dropdown and navigate to login page tab
        homePage.clickLoginButton();
        homePage.clickLoginSignUpCta();
        homePage.switchToNewTab();
        loginPage = new LoginPage(driver);

        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");

        // Enter invalid input
        loginPage.enterMobileNumber(inputData);
        loginPage.clickContinueButton();

        // Check that OTP field is NOT triggered for invalid inputs
        boolean isOtpTriggered = loginPage.isOtpFieldDisplayed();
        Assert.assertFalse(isOtpTriggered,
                "OTP should NOT be triggered for invalid input: '" + inputData + "' in scenario: " + scenario);
    }
}
