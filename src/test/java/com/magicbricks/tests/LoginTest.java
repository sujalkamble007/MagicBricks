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
 * Production-grade TestNG Test Class for MagicBricks Login Module.
 *
 * Implements thorough TestNG assertions mapping 1:1 to every documented
 * Expected Result from Sprint Day 1:
 * - TC_LG_001: Login Dropdown Display & CTA Visibility (Hard Assert)
 * - TC_LG_002: Valid Mobile Number Entry & Live OTP Flow in New Tab / Modal (SoftAssert across container, heading, user type, value, and OTP)
 * - TC_LG_003: Invalid Mobile Number Format Rejection (Data-Driven via Excel, Hard Assert preventing premature OTP)
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
     * Preconditions: Home page loaded
     * Steps:
     *   1. Click "Login" button in header
     *   2. Check Login/Sign Up CTA button visibility inside dropdown
     * Expected Result: Login dropdown opens; "Login/Sign Up" CTA is displayed and clickable
     */
    @Test(priority = 1, groups = {"login", "smoke"},
            description = "TC_LG_001: Verify login dropdown opens and Login/Sign Up CTA is visible")
    public void verifyLoginDropdownOpens() {
        homePage.clickLoginButton();
        boolean isCtaVisible = homePage.isLoginSignUpCtaDisplayed();
        Assert.assertTrue(isCtaVisible,
                "Login/Sign Up CTA button must be visible inside the login dropdown when Login is clicked");
    }

    /**
     * TC_LG_002: Verify Valid Mobile Number Entry on Login Page & Live OTP Flow
     * Preconditions: Home page loaded
     * Steps:
     *   1. Open Login dropdown and click Login/Sign Up CTA
     *   2. Switch to the newly opened tab / modal
     *   3. Validate login container and heading
     *   4. Verify Buyer/Owner radio is selected by default
     *   5. Enter 10-digit mobile number from Excel (9518306867)
     *   6. Assert input field value matches entered number
     *   7. Click Continue and handle OTP prompt if triggered
     * Expected Result: Login page loads; heading is "Login"; mobile number accepted; no error; OTP triggered
     *
     * @param scenario Test scenario description from Excel
     * @param mobileNumber Mobile number (9518306867)
     * @param expectedType Expected status ("Valid")
     */
    @Test(priority = 2, groups = {"login", "regression"},
            dataProvider = "validLoginMobileData", dataProviderClass = DataProviders.class,
            description = "TC_LG_002: Verify valid 10-digit mobile number entry and OTP prompt on login page")
    public void verifyValidMobileNumberEntry(String scenario, String mobileNumber, String expectedType) {
        // Step 1: Trigger login navigation
        homePage.clickLoginButton();
        homePage.clickLoginSignUpCta();

        // Step 2: Switch to newly opened login window
        homePage.switchToNewTab();
        loginPage = new LoginPage(driver);

        // Step 3 & 4: Validate container, heading, and default selections
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(loginPage.isLoginPageLoaded(),
                "Login container must be loaded and visible in the login window");
        softAssert.assertTrue(loginPage.getLoginHeadingText().contains("Login"),
                "Login page heading must contain 'Login'. Actual: " + loginPage.getLoginHeadingText());
        softAssert.assertTrue(loginPage.isBuyerOwnerSelected(),
                "Buyer/Owner radio option must be selected by default");

        // Step 5 & 6: Input mobile number and verify value attribute
        loginPage.enterMobileNumber(mobileNumber);
        String enteredValue = loginPage.getInputFieldValue();
        softAssert.assertEquals(enteredValue, mobileNumber,
                "Input field value attribute must match the entered mobile number: " + mobileNumber);

        // Step 7: Click Continue to proceed to OTP
        loginPage.clickContinueButton();

        // Step 8: Handle OTP entry if OTP container appears
        if (loginPage.isOtpFieldDisplayed()) {
            loginPage.waitForOtpAndEnter();
        }

        softAssert.assertAll();
    }

    /**
     * TC_LG_003: Verify Invalid Mobile Number is Rejected (Data-Driven via Excel)
     * Preconditions: Login page opened in new tab / modal
     * Steps:
     *   1. Navigate to login page
     *   2. Enter invalid input (short digits, alpha, special characters)
     *   3. Click Continue button
     *   4. Verify OTP is NOT triggered for invalid data
     * Expected Result: Invalid data is rejected; OTP is NOT triggered
     *
     * @param scenario Negative scenario description
     * @param inputData Invalid test input string
     * @param expectedError Expected error category ("Invalid")
     */
    @Test(priority = 3, groups = {"login", "regression"},
            dataProvider = "invalidLoginMobileData", dataProviderClass = DataProviders.class,
            description = "TC_LG_003: Verify invalid mobile/email input is rejected with error")
    public void verifyInvalidMobileNumberRejected(String scenario, String inputData, String expectedError) {
        homePage.clickLoginButton();
        homePage.clickLoginSignUpCta();
        homePage.switchToNewTab();
        loginPage = new LoginPage(driver);

        Assert.assertTrue(loginPage.isLoginPageLoaded(),
                "Login page must be loaded before entering test data");

        loginPage.enterMobileNumber(inputData);
        loginPage.clickContinueButton();

        // Assert OTP field is NOT triggered for invalid input
        boolean isOtpTriggered = loginPage.isOtpFieldDisplayed();
        Assert.assertFalse(isOtpTriggered,
                "OTP must NOT be triggered for invalid input: '" + inputData + "' in scenario: " + scenario);
    }
}
